package cbconnectit.portfolio.web.admin.pages.companies.manage

import cbconnectit.portfolio.web.data.models.dto.requests.company.InsertCompany
import cbconnectit.portfolio.web.data.models.dto.requests.company.UpdateCompany
import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.CompanyRepo
import cbconnectit.portfolio.web.utils.ViewModel
import com.materialkobweb.components.toast.ToastManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.ifEmpty

class ManageCompaniesViewModel(
    private val companyId: String?,
    private val companyRepo: CompanyRepo = CompanyRepo
) : ViewModel(), ManageCompaniesContract {
    private val _state = MutableStateFlow(ManageCompaniesContract.State())
    override val state: StateFlow<ManageCompaniesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ManageCompaniesContract.Effect>()
    override val effect: SharedFlow<ManageCompaniesContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ManageCompaniesContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ManageCompaniesContract.Intent.LoadInitialData -> loadCompanyIfNeeded()
            is ManageCompaniesContract.Intent.UpdateName -> updateState { it.copy(name = intent.name) }
            is ManageCompaniesContract.Intent.UpdateLinksInput -> updateState { it.copy(linksInput = intent.linksInput) }
            is ManageCompaniesContract.Intent.SaveCompany -> saveCompany()
            is ManageCompaniesContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageCompaniesContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageCompaniesContract.Intent.ConfirmDelete -> deleteCompany()
            is ManageCompaniesContract.Intent.Cancel -> emitEffect(ManageCompaniesContract.Effect.NavigateBackToCompanies)
        }
    }

    private suspend fun loadCompanyIfNeeded() {
        if (companyId == null) return

        updateState { it.copy(isLoading = true) }
        companyRepo.getCompanyById(companyId).fold(
            onSuccess = { company ->
                updateState {
                    it.copy(
                        company = company,
                        name = company.name,
                        linksInput = company.links.joinToString(", ") { link -> link.url },
                        isLoading = false
                    )
                }
            },
            onError = { error ->
                updateState { it.copy(isLoading = false) }
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun saveCompany() {
        val currentState = state.value
        updateState { it.copy(hasAttemptedSave = true) }

        val name = currentState.name.trim()
        if (name.isBlank()) {
            ToastManager.warning("Vul een geldige naam in voor het bedrijf")
            return
        }

        val links = parseLinks(currentState.linksInput)

        updateState { it.copy(isSaving = true) }
        if (companyId != null && isCompanyUnchanged(currentState, name, links)) {
            updateState { it.copy(isSaving = false) }
            emitEffect(ManageCompaniesContract.Effect.NavigateBackToCompanies)
            return
        }

        val saveResult = if (companyId != null) {
            companyRepo.updateCompany(companyId, UpdateCompany(name = name, links = links.ifEmpty { null }))
        } else {
            companyRepo.insertCompany(InsertCompany(name = name, links = links.ifEmpty { null }))
        }

        saveResult.fold(
            onSuccess = {
                val successMessage = if (companyId != null) {
                    "Bedrijf succesvol bijgewerkt"
                } else {
                    "Bedrijf succesvol aangemaakt"
                }
                ToastManager.success(successMessage)
                emitEffect(ManageCompaniesContract.Effect.NavigateBackToCompanies)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isSaving = false) }
    }

    private suspend fun deleteCompany() {
        if (companyId == null) return

        updateState { it.copy(isDeleting = true) }
        companyRepo.deleteCompany(companyId).fold(
            onSuccess = {
                ToastManager.success("Bedrijf succesvol verwijderd")
                updateState { it.copy(showDeleteDialog = false) }
                emitEffect(ManageCompaniesContract.Effect.NavigateBackToCompanies)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isDeleting = false) }
    }

    private fun isCompanyUnchanged(
        currentState: ManageCompaniesContract.State,
        normalizedName: String,
        normalizedLinks: List<String>
    ): Boolean {
        val currentCompany = currentState.company ?: return false
        val initialLinks = currentCompany.links.map { it.url.trim() }
        return currentCompany.name == normalizedName && initialLinks == normalizedLinks
    }

    private fun parseLinks(linksInput: String): List<String> =
        linksInput
            .split(",", "\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()

    override fun emitEffect(effect: ManageCompaniesContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageCompaniesContract.State) -> ManageCompaniesContract.State) {
        _state.update(block)
    }
}
