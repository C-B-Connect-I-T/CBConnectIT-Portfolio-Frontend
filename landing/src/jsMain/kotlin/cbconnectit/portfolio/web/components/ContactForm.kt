package cbconnectit.portfolio.web.components

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.autoComplete
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.data1PasswordIgnore
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.method
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.name
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.required
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputEmail
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputMessage
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputName
import cbconnectit.portfolio.web.utils.Res
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.DsBorderRadius
import com.materialdesignsystem.components.widgets.DsEditableArea
import com.materialdesignsystem.components.widgets.DsEditableField
import com.materialdesignsystem.components.widgets.FilledButton
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.Text

@Composable
fun ContactForm(breakpoint: Breakpoint) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val maxWidth = when {
        breakpoint > Breakpoint.MD -> 700.px
        breakpoint == Breakpoint.MD -> 570.px
        else -> 490.px
    }

    // TODO: remove Formspree and use something custom since my account has been deleted...
    Form(
        action = "https://formspree.io/f/maygebgo", // the navigation after this form is filled in
        attrs = Modifier
            .fillMaxWidth(80.percent)
            .alignContent(AlignContent.Center)
            .maxWidth(maxWidth)
            .attrsModifier {
                attr(method, "POST")
            }.toAttrs()
    ) {

        DsEditableField(
            modifier = Modifier
                .ariaRequired(true)
                .attrsModifier {
                    attr(autoComplete, "off")
                    attr(name, "name")
                    attr(required, "true")
                    attr(data1PasswordIgnore, "") // Add this so 1Password will ignore this field
                },
            placeholder = Res.String.FullName,
            id = inputName,
            label = Res.String.Name,
            value = fullName,
            onValueChange = { fullName = it }
        )

        Spacer(Modifier.height(16.px))

        DsEditableField(
            modifier = Modifier
                .ariaRequired(true)
                .attrsModifier {
                    attr(autoComplete, "off")
                    attr(name, "email")
                    attr(required, "true")
                    attr(data1PasswordIgnore, "") // Add this so 1Password will ignore this field
                },
            placeholder = Res.String.EmailAddress,
            id = inputEmail,
            label = Res.String.Email,
            value = email,
            onValueChange = { email = it }
        )

        Spacer(Modifier.height(16.px))

        DsEditableArea(
            id = inputMessage,
            label = Res.String.Message,
            placeholder = Res.String.YourMessage,
            value = message,
            onValueChange = { message = it }
        )
        Spacer(Modifier.height(24.px))

        FilledButton(
            borderRadius = DsBorderRadius(5.px),
            type = ButtonType.Submit,
            onClick = {}
        ) {
            Text(Res.String.Submit)
        }
    }
}