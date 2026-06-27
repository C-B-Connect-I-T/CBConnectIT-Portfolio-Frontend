package cbconnectit.portfolio.web.admin.pages.projects

import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.ProjectRepo
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

class ProjectsViewModel(
    private val projectRepo: ProjectRepo = ProjectRepo
) : ViewModel(), ProjectsContract {
    private val _state = MutableStateFlow(ProjectsContract.State())
    override val state: StateFlow<ProjectsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProjectsContract.Effect>()
    override val effect: SharedFlow<ProjectsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ProjectsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ProjectsContract.Intent.LoadProjects -> loadProjects()
            is ProjectsContract.Intent.NavigateToManage ->
                emitEffect(ProjectsContract.Effect.NavigateToManage(intent.projectId))
        }
    }

    private suspend fun loadProjects() {
        updateState { it.copy(isLoading = true) }

        projectRepo.getProjects().fold(
            onSuccess = { projects ->
                updateState {
                    it.copy(
                        projects = projects.sortedByDescending { project -> project.updatedAt },
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

    override fun emitEffect(effect: ProjectsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ProjectsContract.State) -> ProjectsContract.State) {
        _state.update(block)
    }
}
