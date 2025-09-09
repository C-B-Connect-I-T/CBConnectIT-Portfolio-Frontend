package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.styles.InputStyle
import cbconnectit.portfolio.web.styles.MainButtonStyle
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.autoComplete
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.data1PasswordIgnore
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.method
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.name
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.placeholder
import cbconnectit.portfolio.web.utils.Identifiers.AttributeName.required
import cbconnectit.portfolio.web.utils.Identifiers.ClassNames.formControl
import cbconnectit.portfolio.web.utils.Identifiers.ClassNames.formLabel
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputEmail
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputMessage
import cbconnectit.portfolio.web.utils.Identifiers.ContactForm.inputName
import cbconnectit.portfolio.web.utils.Res
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*

@Composable
fun ContactForm(breakpoint: Breakpoint) {
    val maxWidth = when {
        breakpoint > Breakpoint.MD -> 700.px
        breakpoint == Breakpoint.MD -> 570.px
        else -> 490.px
    }

    Form(
        action = "https://formspree.io/f/maygebgo", // the navigation after this form is filled in
        attrs = Modifier
            .fillMaxWidth(80.percent)
            .maxWidth(maxWidth)
            .attrsModifier {
                attr(method, "POST")
            }.toAttrs()
    ) {
        Label(
            attrs = Modifier
                .classNames(formLabel)
                .toAttrs(),
            forId = inputName
        ) {
            Text(Res.String.Name)
        }

        Input(
            type = InputType.Text,
            attrs = InputStyle.toModifier()
                .id(inputName)
                .fillMaxWidth()
                .backgroundColor(ColorMode.current.toColorScheme.surface)
                .color(ColorMode.current.toColorScheme.onSurface)
                .boxShadow(0.px, 0.px, 0.px, 0.px, null) // overrides the default behaviour of the bootstrap
                .classNames(formControl)
                .attrsModifier {
                    attr(autoComplete, "off")
                    attr(placeholder, Res.String.FullName)
                    attr(name, "name")
                    attr(required, "true")
                    attr(data1PasswordIgnore, "") // Add this so 1Password will ignore this field
                }
                .toAttrs()
        )

        Spacer(Modifier.height(16.px))

        Label(
            attrs = Modifier
                .classNames(formLabel)
                .toAttrs(),
            forId = inputEmail
        ) {
            Text(Res.String.Email)
        }

        Input(
            type = InputType.Email,
            attrs = InputStyle.toModifier()
                .id(inputEmail)
                .fillMaxWidth()
                .backgroundColor(ColorMode.current.toColorScheme.surface)
                .color(ColorMode.current.toColorScheme.onSurface)
                .boxShadow(0.px, 0.px, 0.px, 0.px, null) // overrides the default behaviour of the bootstrap
                .classNames(formControl)
                .attrsModifier {
                    attr(autoComplete, "off")
                    attr(placeholder, Res.String.EmailAddress)
                    attr(name, "email")
                    attr(required, "true")
                    attr(data1PasswordIgnore, "") // Add this so 1Password will ignore this field
                }
                .toAttrs()
        )

        Spacer(Modifier.height(16.px))

        Label(
            attrs = Modifier
                .classNames(formLabel)
                .toAttrs(),
            forId = inputMessage
        ) {
            Text(Res.String.Message)
        }

        TextArea(
            attrs = InputStyle.toModifier()
                .id(inputMessage)
                .fillMaxWidth()
                .backgroundColor(ColorMode.current.toColorScheme.surface)
                .color(ColorMode.current.toColorScheme.onSurface)
                .classNames(formControl)
                .boxShadow(0.px, 0.px, 0.px, 0.px, null) // overrides the default behaviour of the bootstrap
                .height(150.px)
                .attrsModifier {
                    attr(placeholder, Res.String.YourMessage)
                    attr(name, "message")
                    attr(required, "true")
                }
                .toAttrs()
        )

        Spacer(Modifier.height(24.px))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            com.varabyte.kobweb.silk.components.forms.Button(
                type = ButtonType.Submit,
                onClick = {},
                modifier = MainButtonStyle.toModifier()
                    .height(40.px)
                    .border(width = 0.px)
                    .borderRadius(r = 5.px)
                    .cursor(Cursor.Pointer),
            ) {
                Text(Res.String.Submit)
            }
        }
    }
}