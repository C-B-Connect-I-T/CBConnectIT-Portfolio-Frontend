package cbconnectit.portfolio.web.admin.pages.jobpositions.manage

import cbconnectit.portfolio.web.data.models.dto.requests.jobPosition.InsertJobPosition
import cbconnectit.portfolio.web.data.models.dto.requests.jobPosition.UpdateJobPosition
import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.JobPositionRepo
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

class ManageJobPositionsViewModel(
    private val jobPositionId: String?,
    private val jobPositionRepo: JobPositionRepo = JobPositionRepo
) : ViewModel(), ManageJobPositionsContract {
    private val _state = MutableStateFlow(ManageJobPositionsContract.State())
    override val state: StateFlow<ManageJobPositionsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ManageJobPositionsContract.Effect>()
    override val effect: SharedFlow<ManageJobPositionsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ManageJobPositionsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ManageJobPositionsContract.Intent.LoadInitialData -> loadJobPositionIfNeeded()
            is ManageJobPositionsContract.Intent.UpdateName -> updateState { it.copy(name = intent.name) }
            is ManageJobPositionsContract.Intent.SaveJobPosition -> saveJobPosition()
            is ManageJobPositionsContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageJobPositionsContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageJobPositionsContract.Intent.ConfirmDelete -> deleteJobPosition()
            is ManageJobPositionsContract.Intent.Cancel -> emitEffect(ManageJobPositionsContract.Effect.NavigateBackToJobPositions)
        }
    }

    private suspend fun loadJobPositionIfNeeded() {
        if (jobPositionId == null) return

        updateState { it.copy(isLoading = true) }
        jobPositionRepo.getJobPositionById(jobPositionId).fold(
            onSuccess = { jobPosition ->
                updateState {
                    it.copy(
                        jobPosition = jobPosition,
                        name = jobPosition.name,
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

    private suspend fun saveJobPosition() {
        val currentState = state.value
        updateState { it.copy(hasAttemptedSave = true) }

        val name = currentState.name.trim()
        if (name.isBlank()) {
            ToastManager.warning("Vul een geldige naam in voor de jobpositie")
            return
        }

        updateState { it.copy(isSaving = true) }
        if (jobPositionId != null && currentState.jobPosition?.name == name) {
            updateState { it.copy(isSaving = false) }
            emitEffect(ManageJobPositionsContract.Effect.NavigateBackToJobPositions)
            return
        }

        val saveResult = if (jobPositionId != null) {
            jobPositionRepo.updateJobPosition(jobPositionId, UpdateJobPosition(name = name))
        } else {
            jobPositionRepo.insertJobPosition(InsertJobPosition(name = name))
        }

        saveResult.fold(
            onSuccess = {
                val successMessage = if (jobPositionId != null) {
                    "Jobpositie succesvol bijgewerkt"
                } else {
                    "Jobpositie succesvol aangemaakt"
                }
                ToastManager.success(successMessage)
                emitEffect(ManageJobPositionsContract.Effect.NavigateBackToJobPositions)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isSaving = false) }
    }

    private suspend fun deleteJobPosition() {
        if (jobPositionId == null) return

        updateState { it.copy(isDeleting = true) }
        jobPositionRepo.deleteJobPosition(jobPositionId).fold(
            onSuccess = {
                ToastManager.success("Jobpositie succesvol verwijderd")
                updateState { it.copy(showDeleteDialog = false) }
                emitEffect(ManageJobPositionsContract.Effect.NavigateBackToJobPositions)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isDeleting = false) }
    }

    override fun emitEffect(effect: ManageJobPositionsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageJobPositionsContract.State) -> ManageJobPositionsContract.State) {
        _state.update(block)
    }
}
