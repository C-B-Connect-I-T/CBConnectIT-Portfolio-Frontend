package cbconnectit.portfolio.web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cbconnectit.portfolio.web.data.TokenManager
import cbconnectit.portfolio.web.data.models.enums.UserRole
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.SiteGlobals
import cbconnectit.portfolio.web.utils.format
import com.materialkobweb.components.DsPageLayout
import com.materialkobweb.components.sections.OverflowMenu
import com.materialkobweb.components.widgets.DsAddFloatingActionButton
import com.materialkobweb.components.widgets.FilledIconButtonStyle
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Header

@Composable
fun AdminPageLayout(
    title: String,
    fabOnClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val ctx = rememberPageContext()
    val authStatus by TokenManager.authStatus.collectAsState()
    val isAuthenticated = authStatus?.authenticated
    val userRole = authStatus?.roleType

    // Guard: skip auth checks during export, return early if checks fail
    if (!SiteGlobals.isExporting) {
        LaunchedEffect(isAuthenticated, userRole) {
            when {
                isAuthenticated == null -> return@LaunchedEffect // Still loading
                !isAuthenticated || (userRole != UserRole.Admin && userRole != UserRole.Moderator) -> {
                    ctx.router.navigateTo(Navigation.Screen.Admin.Login().route)
                    return@LaunchedEffect
                }
            }
        }
    }

    val breakpoint = rememberBreakpoint()
    var overflowMenuOpened by remember { mutableStateOf(false) }
    val sidePanelVisible = breakpoint >= Breakpoint.MD

    DsPageLayout(
        title = Res.String.DocumentTitle.format(title),
        header = {
            if (sidePanelVisible) return@DsPageLayout

            Header { overflowMenuOpened = true }
        },
        sidePanel = {
            if (!sidePanelVisible) return@DsPageLayout

//            SidePanel()
        },
        overflowMenu = {
            OverflowMenu(logoImage = "/logo.svg", onMenuClosed = { overflowMenuOpened = false }) {
//                NavigationItems()
            }
        },
        overflowMenuOpened = overflowMenuOpened,
        footer = {
//            FooterSection()
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                content()

                if (fabOnClick != null) {
                    DsAddFloatingActionButton(
                        modifier = Modifier
                            .position(Position.Fixed)
                            .right(if (breakpoint >= Breakpoint.MD) 50.px else 24.px)
                            .bottom(50.px),
                        onClick = fabOnClick,
                        variant = FilledIconButtonStyle
                    )
                }
            }
        }
    )
}
