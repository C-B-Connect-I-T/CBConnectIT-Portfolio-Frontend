package cbconnectit.portfolio.web.pages.projects

import cbconnectit.portfolio.web.data.repos.ProjectRepo
import cbconnectit.portfolio.web.data.repos.TagRepo
import cbconnectit.portfolio.web.utils.MVI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectsViewModel(
    queryTagIds: List<String> = emptyList(),
    private val projectRepo: ProjectRepo = ProjectRepo,
    private val tagRepo: TagRepo = TagRepo,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : MVI<ProjectsContract.State, ProjectsContract.Intent, ProjectsContract.Effect> {
    private val _state = MutableStateFlow(ProjectsContract.State(filterTags = queryTagIds))
    override val state: StateFlow<ProjectsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProjectsContract.Effect>()
    override val effect: SharedFlow<ProjectsContract.Effect> = _effect.asSharedFlow()

    init {
        sendIntent(ProjectsContract.Intent.LoadInitialData)
    }

    override fun sendIntent(intent: ProjectsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ProjectsContract.Intent.LoadInitialData -> loadInitialData()
            is ProjectsContract.Intent.UpdateFilterTags -> updateFilterTags(intent.tagId)
        }
    }

    private suspend fun loadInitialData() = withContext(Dispatchers.Default) {
        val projectsDeferred = async { projectRepo.getProjects() }
        val tagsDeferred = async { tagRepo.getTags() }

        val projects = projectsDeferred.await()
        val tags = tagsDeferred.await()

        updateState {
            it.copy(
                projects = projects,
                tags = tags,
            )
        }
    }

    private fun updateFilterTags(tagId: String) {
        val tempFilterTags = _state.value.filterTags.toMutableList()
        if (tempFilterTags.contains(tagId)) {
            tempFilterTags.remove(tagId)
        } else {
            tempFilterTags.add(tagId)
        }
        updateState { it.copy(filterTags = tempFilterTags) }
    }

    override fun emitEffect(effect: ProjectsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ProjectsContract.State) -> ProjectsContract.State) {
        _state.update(block)
    }
}