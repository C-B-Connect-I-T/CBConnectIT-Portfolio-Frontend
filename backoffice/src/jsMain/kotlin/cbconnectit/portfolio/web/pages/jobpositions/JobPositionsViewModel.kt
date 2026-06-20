package cbconnectit.portfolio.web.pages.jobpositions

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

class JobPositionsViewModel(
    private val jobPositionRepo: JobPositionRepo = JobPositionRepo
) : ViewModel(), JobPositionsContract {
    private val _state = MutableStateFlow(JobPositionsContract.State())
    override val state: StateFlow<JobPositionsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<JobPositionsContract.Effect>()
    override val effect: SharedFlow<JobPositionsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: JobPositionsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is JobPositionsContract.Intent.LoadJobPositions -> loadJobPositions()
            is JobPositionsContract.Intent.NavigateToManage ->
                emitEffect(JobPositionsContract.Effect.NavigateToManage(intent.jobPositionId))
        }
    }

    private suspend fun loadJobPositions() {
        updateState { it.copy(isLoading = true) }

        jobPositionRepo.getJobPositions().fold(
            onSuccess = { jobPositions ->
                updateState { it.copy(jobPositions = jobPositions, isLoading = false) }
            },
            onError = { error ->
                updateState { it.copy(isLoading = false) }
                ToastManager.error(error.message)
            }
        )
    }

    override fun emitEffect(effect: JobPositionsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (JobPositionsContract.State) -> JobPositionsContract.State) {
        _state.update(block)
    }
}
