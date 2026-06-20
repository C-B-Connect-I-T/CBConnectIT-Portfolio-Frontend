package cbconnectit.portfolio.web.pages.jobpositions

import cbconnectit.portfolio.web.data.models.domain.JobPosition
import cbconnectit.portfolio.web.utils.MVI

interface JobPositionsContract :
    MVI<JobPositionsContract.State, JobPositionsContract.Intent, JobPositionsContract.Effect> {

    data class State(
        val jobPositions: List<JobPosition> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data object LoadJobPositions : Intent()
        data class NavigateToManage(val jobPositionId: String? = null) : Intent()
    }

    sealed class Effect {
        data class NavigateToManage(val jobPositionId: String? = null) : Effect()
    }
}
