package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cbconnectit.portfolio.web.data.extensions.buildFormData
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.autoComplete
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.data1PasswordIgnore
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.name
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.required
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputEmail
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputMessage
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputName
import cbconnectit.portfolio.web.utils.MVI
import cbconnectit.portfolio.web.utils.Res
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.DsBorderRadius
import com.materialdesignsystem.components.widgets.DsEditableArea
import com.materialdesignsystem.components.widgets.DsEditableField
import com.materialdesignsystem.components.widgets.FilledButton
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text

@Composable
fun ContactForm() {
    val viewModel = remember { ContactFormViewModel() }
    val state by viewModel.state.collectAsState()

    ContactFormContent(state, viewModel::sendIntent)
}

@Composable
fun ContactFormContent(
    state: ContactFormContract.State,
    sendIntent: (ContactFormContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()

    val maxWidth = when {
        breakpoint > Breakpoint.MD -> 700.px
        breakpoint == Breakpoint.MD -> 570.px
        else -> 490.px
    }

    Column(
        modifier = Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .alignItems(AlignItems.Center)
            .fillMaxWidth(80.percent)
            .maxWidth(maxWidth)
    ) {
        DsEditableField(
            modifier = Modifier
                .fillMaxWidth()
                .attrsModifier {
                    attr(autoComplete, "off")
                    attr(name, "name")
                    attr(required, "true")
                    attr(data1PasswordIgnore, "") // Add this so 1Password will ignore this field
                },
            placeholder = Res.String.FullName,
            required = true,
            id = inputName,
            label = Res.String.Name,
            value = state.name,
            onValueChange = { sendIntent(ContactFormContract.Intent.UpdateName(it)) }
        )

        Spacer(Modifier.height(16.px))

        DsEditableField(
            modifier = Modifier
                .fillMaxWidth()
                .ariaInvalid(!state.isEmailValid)
                .attrsModifier {
                    attr(autoComplete, "off")
                    attr(name, "email")
                    attr(required, "true")
                    attr(data1PasswordIgnore, "") // Add this so 1Password will ignore this field
                },
            placeholder = Res.String.EmailAddress,
            id = inputEmail,
            required = true,
            label = Res.String.Email,
            valid = state.isEmailValid,
            value = state.email,
            onValueChange = { sendIntent(ContactFormContract.Intent.UpdateEmail(it)) }
        )

        Spacer(Modifier.height(16.px))

        DsEditableArea(
            modifier = Modifier.fillMaxWidth(),
            id = inputMessage,
            label = Res.String.Message,
            placeholder = Res.String.YourMessage,
            value = state.message,
            onValueChange = { sendIntent(ContactFormContract.Intent.UpdateMessage(it)) }
        )
        Spacer(Modifier.height(24.px))

        FilledButton(
            borderRadius = DsBorderRadius(5.px),
            onClick = { sendIntent(ContactFormContract.Intent.SubmitForm) }
        ) {
            Text(Res.String.Submit)
        }
    }
}

class ContactFormViewModel(
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : MVI<ContactFormContract.State, ContactFormContract.Intent, ContactFormContract.Effect> {
    private val _state = MutableStateFlow(ContactFormContract.State())
    override val state get() = _state

    private val _effect = MutableSharedFlow<ContactFormContract.Effect>()
    override val effect = _effect.asSharedFlow()

    override fun sendIntent(intent: ContactFormContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ContactFormContract.Intent.UpdateName -> updateState { it.copy(name = intent.name) }
            is ContactFormContract.Intent.UpdateEmail -> updateState { it.copy(email = intent.email) }
            is ContactFormContract.Intent.UpdateMessage -> updateState { it.copy(message = intent.message) }
            is ContactFormContract.Intent.SubmitForm -> {
                val state = _state.value

                if (!state.isFormValid) {
                    println("Form is not valid")
                    return@launch
                }

                val formData = buildFormData(
                    mapOf(
                        "Name" to state.name,
                        "Email" to state.email,
                        "Message" to state.message
                    )
                )

                try {
                    val response: NetworkResponse<Unit, ErrorResponse> = postRequest("https://formspree.io/f/xanpqwdz", formData)
                    if (response is NetworkResponse.Success && response.code == 302) {
                        println("Form submitted successfully")
                    } else {
                        println("Form submission failed: ${response}")
                    }

                    updateState { ContactFormContract.State() }
                } catch (e: Exception) {
                    println("Form submission failed exceptionally: ${e.message}")
                }
            }
        }
    }

    override fun emitEffect(effect: ContactFormContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(update: (ContactFormContract.State) -> ContactFormContract.State) {
        _state.value = update(_state.value)
    }
}

interface ContactFormContract : MVI<ContactFormContract.State, ContactFormContract.Intent, ContactFormContract.Effect> {
    data class State(
        val name: String = "",
        val email: String = "",
        val message: String = ""
    ) {
        val isFormValid: Boolean
            get() = name.isNotBlank() && isEmailValid && message.isNotBlank()

        val isEmailValid: Boolean
            get() = email.contains('@')
    }

    sealed class Intent {
        data class UpdateName(val name: String) : Intent()
        data class UpdateEmail(val email: String) : Intent()
        data class UpdateMessage(val message: String) : Intent()
        data object SubmitForm : Intent()
    }

    sealed class Effect
}
