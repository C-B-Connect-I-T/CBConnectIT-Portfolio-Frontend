package cbconnectit.portfolio.web.admin.pages.experiences

import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.ExperienceRepo
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

class ExperiencesViewModel(
    private val experienceRepo: ExperienceRepo = ExperienceRepo
) : ViewModel(), ExperiencesContract {
    private val _state = MutableStateFlow(ExperiencesContract.State())
    override val state: StateFlow<ExperiencesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ExperiencesContract.Effect>()
    override val effect: SharedFlow<ExperiencesContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ExperiencesContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ExperiencesContract.Intent.LoadExperiences -> loadExperiences()
            is ExperiencesContract.Intent.NavigateToManage ->
                emitEffect(ExperiencesContract.Effect.NavigateToManage(intent.experienceId))
        }
    }

    private suspend fun loadExperiences() {
        updateState { it.copy(isLoading = true) }

        experienceRepo.getExperiences().fold(
            onSuccess = { experiences ->
                updateState { it.copy(experiences = experiences, isLoading = false) }
            },
            onError = { error ->
                updateState { it.copy(isLoading = false) }
                ToastManager.error(error.message)
            }
        )
    }

    override fun emitEffect(effect: ExperiencesContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ExperiencesContract.State) -> ExperiencesContract.State) {
        _state.update(block)
    }
}
