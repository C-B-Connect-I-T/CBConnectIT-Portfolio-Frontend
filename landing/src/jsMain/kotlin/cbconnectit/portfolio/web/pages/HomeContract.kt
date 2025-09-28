package cbconnectit.portfolio.web.pages

import cbconnectit.portfolio.web.data.models.domain.*
import cbconnectit.portfolio.web.utils.MVI

interface HomeContract : MVI<HomeContract.State, HomeContract.Intent, HomeContract.Effect> {

    data class State(
        val services: List<Service> = emptyList(),
        val projects: List<Project> = emptyList(),
        val tags: List<Tag> = emptyList(),
        val experiences: List<Experience> = emptyList(),
        val testimonials: List<Testimonial> = emptyList(),
        val selectedWork: Project? = null,
        val yearsOfExperience: Int = 0
    )

    sealed class Intent {
        data object LoadInitialData : Intent()
        data object LetsChatClicked : Intent()
        data class UpdateSelectedWork(val project: Project) : Intent()
    }

    sealed class Effect {
        data object NavigateToContactSection : Effect()
    }
}
