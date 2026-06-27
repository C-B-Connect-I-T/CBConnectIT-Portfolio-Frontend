package cbconnectit.portfolio.web.admin.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.unauthenticatedGuard
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.logoImage
import cbconnectit.portfolio.web.utils.rememberViewModel
import com.materialkobweb.components.widgets.DsEditableField
import com.materialkobweb.components.widgets.FilledButton
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.UpdateHistoryMode
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Page("/admin/login")
@Composable
fun LoginPage() = unauthenticatedGuard {
    val pageContext = rememberPageContext()

    val viewModel = rememberViewModel(cached = false) { LoginViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { event ->
            when (event) {
                is LoginContract.Effect.NavigateToNextScreen -> {
                    pageContext.router.navigateTo(
                        pathQueryAndFragment = Navigation.Screen.Admin.Home.route,
                        updateHistoryMode = UpdateHistoryMode.REPLACE
                    )
                }
            }
        }
    }

    LoginPageContent(
        state = state,
        sendIntent = viewModel::sendIntent
    )
}

@Composable
private fun LoginPageContent(
    state: LoginContract.State,
    sendIntent: (LoginContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    val colorScheme = ColorMode.current.toColorScheme

    Box(
        modifier = Modifier.fillMaxSize().padding(topBottom = 50.px),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(
                    when (breakpoint) {
                        Breakpoint.XL, Breakpoint.XXL -> 33.percent
                        Breakpoint.LG, Breakpoint.MD -> 50.percent
                        else -> 77.percent
                    }
                )
                .padding(
                    leftRight = if (breakpoint >= Breakpoint.SM) 50.px else 20.px,
                    top = 40.px,
                    bottom = 24.px
                )
                .backgroundColor(colorScheme.surfaceVariant)
                .borderRadius(20.px)
                .border(1.px, style = LineStyle.Solid, color = colorScheme.outlineVariant),
            verticalArrangement = Arrangement.spacedBy(24.px, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(75.percent),
                src = logoImage(ColorMode.current),
                description = Res.String.LogoImageAlt
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.px, Alignment.CenterVertically)
            ) {
                DsEditableField(
                    modifier = Modifier.fillMaxWidth(),
                    id = "inputEmail",
                    label = Res.String.Email,
                    placeholder = Res.String.EnterEmail,
                    value = state.emailAddress,
                    valid = !state.hasAttemptedLogin || state.emailAddress.isNotBlank(),
                    onValueChange = { sendIntent(LoginContract.Intent.UpdateEmail(it)) },
                    required = true,
                    backgroundColor = colorScheme.surfaceContainer,
                    focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
                )

                DsEditableField(
                    modifier = Modifier.fillMaxWidth(),
                    id = "inputPassword",
                    label = Res.String.Password,
                    placeholder = Res.String.EnterPassword,
                    value = state.password,
                    valid = !state.hasAttemptedLogin || state.password.isNotBlank(),
                    onValueChange = { sendIntent(LoginContract.Intent.UpdatePassword(it)) },
                    type = InputType.Password,
                    required = true,
                    backgroundColor = colorScheme.surfaceContainer,
                    focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
                ) { sendIntent(LoginContract.Intent.Login) }
            }

            FilledButton(
                type = ButtonType.Submit,
                onClick = { sendIntent(LoginContract.Intent.Login) }
            ) {
                Text(Res.String.SignIn)
            }

            if (state.errorText.isNotEmpty()) {
                SpanText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .color(colorScheme.error)
                        .textAlign(TextAlign.Center),
                    text = state.errorText
                )
            }
        }
    }
}
