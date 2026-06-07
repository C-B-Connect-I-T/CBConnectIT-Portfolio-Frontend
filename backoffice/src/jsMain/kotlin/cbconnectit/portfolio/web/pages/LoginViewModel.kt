package cbconnectit.portfolio.web.pages

import cbconnectit.portfolio.web.components.toast.ToastManager
import cbconnectit.portfolio.web.data.repos.AuthRepo
import cbconnectit.portfolio.web.utils.MVI
import cbconnectit.portfolio.web.utils.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val AUTO_HIDE_TIME = 3000L

class LoginViewModel(
    private val authRepo: AuthRepo = AuthRepo
) : ViewModel(), MVI<LoginContract.State, LoginContract.Intent, LoginContract.Effect> {

    private val _state = MutableStateFlow(LoginContract.State())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginContract.Effect>()
    override val effect: SharedFlow<LoginContract.Effect> = _effect.asSharedFlow()

    init {
        coroutineScope.launch {
            state.collectLatest { currentState ->
                if (currentState.errorText.isNotBlank()) {
                    delay(AUTO_HIDE_TIME) // Delay before auto-resetting error
                    _state.update {
                        // Only clear if error hasn't changed
                        if (it.errorText == currentState.errorText) {
                            it.copy(errorText = "")
                        } else it
                    }
                }
            }
        }
    }


    override fun sendIntent(intent: LoginContract.Intent) = coroutineScope.launch {
        when (intent) {
            is LoginContract.Intent.Login -> login()
            is LoginContract.Intent.UpdateEmail -> _state.update { it.copy(emailAddress = intent.value) }
            is LoginContract.Intent.UpdatePassword -> _state.update { it.copy(password = intent.value) }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun login() {
        val email = _state.value.emailAddress
        val password = _state.value.password

        updateState { it.copy(hasAttemptedLogin = true) }

        if (email.isEmpty() || password.isEmpty()) {
            ToastManager.warning("Vul alle velden in", title = "Ongeldige invoer")
            return
        }

        try {
            authRepo.login(email, password)
            ToastManager.success("Je bent succesvol ingelogd!", title = "Welkom")
            emitEffect(LoginContract.Effect.NavigateToNextScreen)
        } catch (e: Exception) {
            ToastManager.error(e.message ?: "Er is een fout opgetreden bij het inloggen", title = "Inloggen mislukt")
        }
    }

    override fun emitEffect(effect: LoginContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (LoginContract.State) -> LoginContract.State) {
        _state.update(block)
    }
}

interface LoginContract : MVI<LoginContract.State, LoginContract.Intent, LoginContract.Effect> {
    data class State(
        val hasAttemptedLogin: Boolean = false,
        val emailAddress: String = "",
        val password: String = "",
        val errorText: String = ""
    ) {
        fun isValid(): Boolean {
            return emailAddress.isNotEmpty() && password.isNotEmpty()
        }
    }

    sealed class Intent {
        data object Login : Intent()
        data class UpdateEmail(val value: String) : Intent()
        data class UpdatePassword(val value: String) : Intent()
    }

    sealed class Effect {
        data object NavigateToNextScreen : Effect()
    }
}
