package cbconnectit.portfolio.web.admin.pages.services

import cbconnectit.portfolio.web.data.models.domain.ServiceAdmin
import cbconnectit.portfolio.web.utils.MVI

interface ServicesContract :
    MVI<ServicesContract.State, ServicesContract.Intent, ServicesContract.Effect> {

    data class State(
        val services: List<ServiceAdmin> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data object LoadServices : Intent()
        data class NavigateToManage(val serviceId: String? = null) : Intent()
    }

    sealed class Effect {
        data class NavigateToManage(val serviceId: String? = null) : Effect()
    }
}
