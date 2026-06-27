package cbconnectit.portfolio.web.admin.pages.services

import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.ServiceRepo
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

class ServicesViewModel(
    private val serviceRepo: ServiceRepo = ServiceRepo
) : ViewModel(), ServicesContract {
    private val _state = MutableStateFlow(ServicesContract.State())
    override val state: StateFlow<ServicesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ServicesContract.Effect>()
    override val effect: SharedFlow<ServicesContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ServicesContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ServicesContract.Intent.LoadServices -> loadServices()
            is ServicesContract.Intent.NavigateToManage ->
                emitEffect(ServicesContract.Effect.NavigateToManage(intent.serviceId))
        }
    }

    private suspend fun loadServices() {
        updateState { it.copy(isLoading = true) }

        serviceRepo.getServicesOverview().fold(
            onSuccess = { services ->
                updateState {
                    it.copy(
                        services = services,
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

    override fun emitEffect(effect: ServicesContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ServicesContract.State) -> ServicesContract.State) {
        _state.update(block)
    }
}
