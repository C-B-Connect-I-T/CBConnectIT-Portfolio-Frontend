package cbconnectit.portfolio.web.pages.companies

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

class CompaniesViewModel(
    private val companyRepo: CompanyRepo = CompanyRepo
) : ViewModel(), CompaniesContract {
    private val _state = MutableStateFlow(CompaniesContract.State())
    override val state: StateFlow<CompaniesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CompaniesContract.Effect>()
    override val effect: SharedFlow<CompaniesContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: CompaniesContract.Intent) = coroutineScope.launch {
        when (intent) {
            is CompaniesContract.Intent.LoadCompanies -> loadCompanies()
            is CompaniesContract.Intent.NavigateToManage ->
                emitEffect(CompaniesContract.Effect.NavigateToManage(intent.companyId))
        }
    }

    private suspend fun loadCompanies() {
        updateState { it.copy(isLoading = true) }

        companyRepo.getCompanies().fold(
            onSuccess = { companies ->
                updateState { it.copy(companies = companies, isLoading = false) }
            },
            onError = { error ->
                updateState { it.copy(isLoading = false) }
                ToastManager.error(error.message)
            }
        )
    }

    override fun emitEffect(effect: CompaniesContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (CompaniesContract.State) -> CompaniesContract.State) {
        _state.update(block)
    }
}
