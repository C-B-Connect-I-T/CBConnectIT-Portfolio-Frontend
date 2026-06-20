package cbconnectit.portfolio.web.pages.experiences

import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.utils.MVI

interface ExperiencesContract :
    MVI<ExperiencesContract.State, ExperiencesContract.Intent, ExperiencesContract.Effect> {

    data class State(
        val experiences: List<Experience> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data object LoadExperiences : Intent()
        data class NavigateToManage(val experienceId: String? = null) : Intent()
    }

    sealed class Effect {
        data class NavigateToManage(val experienceId: String? = null) : Effect()
    }
}
