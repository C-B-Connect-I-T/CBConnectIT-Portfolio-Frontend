package cbconnectit.portfolio.web.pages.projects

import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.utils.MVI

interface ProjectsContract : MVI<ProjectsContract.State, ProjectsContract.Intent, ProjectsContract.Effect> {
    data class State(
        val projects: List<Project> = emptyList(),
        val tags: List<Tag> = emptyList(),
        val filterTags: List<String> = emptyList(),
    ) {
        val filteredProjects: List<Project> =
            if (filterTags.isEmpty()) {
                projects
            } else {
                projects.filter { project ->
                    filterTags.all { tagId -> project.tags.any { it.id == tagId } }
                }
            }
    }

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class UpdateFilterTags(val tagId: String) : Intent()
    }

    sealed class Effect
}