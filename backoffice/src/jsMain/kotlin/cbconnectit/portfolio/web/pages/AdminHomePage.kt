package cbconnectit.portfolio.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cbconnectit.portfolio.web.components.layouts.AdminPageLayout
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import cbconnectit.portfolio.web.navigation.logout
import com.materialkobweb.components.widgets.FilledButton
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text

@Page("/admin/index")
@Composable
fun AdminPage() = authenticatedGuard {
    val ctx = rememberPageContext()

    val viewModel = remember { AdminHomeViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is AdminHomeContract.Effect.Navigate -> ctx.router.navigateTo(it.route)
            }
        }
    }

    AdminPageContent(state = state, sendIntent = viewModel::sendIntent)
}

@Composable
fun AdminPageContent(
    state: AdminHomeContract.State,
    sendIntent: (AdminHomeContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    val colorScheme = ColorMode.current.toColorScheme

    AdminPageLayout("Admin") {
        Column(
            modifier = Modifier
                .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
                .padding(top = if (breakpoint >= Breakpoint.MD) 58.px else 36.px),
            verticalArrangement = Arrangement.spacedBy(120.px)
        ) {
            val greeting = if (state.isLoading) {
                "Hello"
            } else {
                "Hello, ${state.currentUser?.fullName ?: "Admin"}!"
            }

            H1 { Text(greeting) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.px)
            ) {
                FilledButton(
                    onClick = { logout() },
                    content = { SpanText("Logout!") }
                )
            }
        }
    }
}
