package cbconnectit.portfolio.web.pages.services

import cbconnectit.portfolio.web.data.repos.ServiceRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServicesViewModel(
    private val serviceRepo: ServiceRepo = ServiceRepo,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : ServicesContract {
    private val _state = MutableStateFlow(ServicesContract.State())
    override val state: StateFlow<ServicesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ServicesContract.Effect>()
    override val effect: SharedFlow<ServicesContract.Effect> = _effect.asSharedFlow()

    init {
        sendIntent(ServicesContract.Intent.LoadInitialData)
    }

    override fun sendIntent(intent: ServicesContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ServicesContract.Intent.LoadInitialData -> loadInitialData()
        }
    }

    private suspend fun loadInitialData() = withContext(Dispatchers.Default) {
        val services = serviceRepo.getServices()

        updateState {
            it.copy(
                services = services
            )
        }
    }

    override fun updateState(block: (ServicesContract.State) -> ServicesContract.State) {
        _state.value = block(_state.value)
    }

    override fun emitEffect(effect: ServicesContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }
}
