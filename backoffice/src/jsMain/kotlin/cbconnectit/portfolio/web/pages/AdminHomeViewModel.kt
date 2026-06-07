package cbconnectit.portfolio.web.pages

import cbconnectit.portfolio.web.data.models.domain.User
import cbconnectit.portfolio.web.data.repos.UserRepo
import cbconnectit.portfolio.web.utils.MVI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminHomeViewModel(
    private val userRepo: UserRepo = UserRepo,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : MVI<AdminHomeContract.State, AdminHomeContract.Intent, AdminHomeContract.Effect> {
    private val _state = MutableStateFlow(AdminHomeContract.State())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AdminHomeContract.Effect>()
    override val effect: SharedFlow<AdminHomeContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: AdminHomeContract.Intent) = coroutineScope.launch {
        when (intent) {
            is AdminHomeContract.Intent.Navigate -> emitEffect(AdminHomeContract.Effect.Navigate(intent.route))

            is AdminHomeContract.Intent.LoadInitialData -> {
                _state.update { it.copy(isLoading = true) }

                val currentUserDeferred = async { userRepo.getCurrentUser() }

                val currentUser = currentUserDeferred.await()

                _state.update {
                    it.copy(
                        isLoading = false,
                        currentUser = currentUser,
                    )
                }
            }
        }
    }

    init {
        sendIntent(AdminHomeContract.Intent.LoadInitialData)
    }

    override fun emitEffect(effect: AdminHomeContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (AdminHomeContract.State) -> AdminHomeContract.State) {
        _state.update(block)
    }
}

interface AdminHomeContract : MVI<AdminHomeContract.State, AdminHomeContract.Intent, AdminHomeContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val currentUser: User? = null,
        val errorText: String = ""
    )

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class Navigate(val route: String) : Intent()
    }

    sealed class Effect {
        data class Navigate(val route: String) : Effect()
    }
}
