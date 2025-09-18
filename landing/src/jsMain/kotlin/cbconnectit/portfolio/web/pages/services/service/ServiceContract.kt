package cbconnectit.portfolio.web.pages.services.service

import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.utils.MVI

interface ServiceContract : MVI<ServiceContract.State, ServiceContract.Intent, ServiceContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val service: Service? = null,
    )

    sealed class Intent {
        data object LoadInitialData : Intent()
    }

    sealed class Effect
}