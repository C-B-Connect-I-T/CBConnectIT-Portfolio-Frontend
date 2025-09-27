package cbconnectit.portfolio.web.pages.services

import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.utils.MVI

interface ServicesContract : MVI<ServicesContract.State, ServicesContract.Intent, ServicesContract.Effect> {
    data class State(
        val services: List<Service> = emptyList()
    )

    sealed class Intent {
        object LoadInitialData : Intent()
    }

    sealed class Effect
}
