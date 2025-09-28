package cbconnectit.portfolio.web.pages.services.service

import cbconnectit.portfolio.web.data.repos.ServiceRepo
import cbconnectit.portfolio.web.utils.MVI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceViewModel(
    private val serviceId: String? = null,
    private val serviceRepo: ServiceRepo = ServiceRepo,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : MVI<ServiceContract.State, ServiceContract.Intent, ServiceContract.Effect> {

    private val _state = MutableStateFlow(ServiceContract.State())
    override val state: StateFlow<ServiceContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ServiceContract.Effect>()
    override val effect: SharedFlow<ServiceContract.Effect> = _effect.asSharedFlow()

    init {
        sendIntent(ServiceContract.Intent.LoadInitialData)
    }

    override fun sendIntent(intent: ServiceContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ServiceContract.Intent.LoadInitialData -> loadInitialData()
        }
    }

    private suspend fun loadInitialData() = withContext(Dispatchers.Default) {
        serviceId ?: run {
            // TODO: show error when serviceId is null!!
            return@withContext
        }

        updateState { it.copy(isLoading = true, error = null) }

        try {
            val service = serviceRepo.getServiceById(serviceId)

            updateState {
                it.copy(
                    isLoading = false,
                    service = service
                )
            }
        } catch (e: Exception) {
            updateState {
                it.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    override fun emitEffect(effect: ServiceContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ServiceContract.State) -> ServiceContract.State) {
        _state.update(block)
    }
}
