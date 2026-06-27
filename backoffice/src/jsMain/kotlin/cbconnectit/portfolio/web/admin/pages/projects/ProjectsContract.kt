package cbconnectit.portfolio.web.admin.pages.projects

import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.utils.MVI

interface ProjectsContract :
    MVI<ProjectsContract.State, ProjectsContract.Intent, ProjectsContract.Effect> {

    data class State(
        val projects: List<Project> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data object LoadProjects : Intent()
        data class NavigateToManage(val projectId: String? = null) : Intent()
    }

    sealed class Effect {
        data class NavigateToManage(val projectId: String? = null) : Effect()
    }
}
