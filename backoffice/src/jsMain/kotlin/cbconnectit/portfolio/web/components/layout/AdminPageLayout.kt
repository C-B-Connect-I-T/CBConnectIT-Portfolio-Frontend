package cbconnectit.portfolio.web.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cbconnectit.portfolio.web.components.AdminNavigationItems
import cbconnectit.portfolio.web.data.TokenManager
import cbconnectit.portfolio.web.data.models.enums.UserRole
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.logout
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.SiteGlobals
import cbconnectit.portfolio.web.utils.format
import cbconnectit.portfolio.web.utils.logoImage
import com.materialkobweb.components.DsPageLayout
import com.materialkobweb.components.sections.OverflowMenu
import com.materialkobweb.components.widgets.DsAddFloatingActionButton
import com.materialkobweb.components.widgets.FilledIconButtonStyle
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.OverscrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overscrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun AdminPageLayout(
    modifier: Modifier = Modifier,
    title: String,
    fabOnClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val authStatus by TokenManager.authStatus.collectAsState()
    val isAuthenticated = authStatus?.authenticated
    val userRole = authStatus?.roleType

    // Guard: skip auth checks during export, return early if checks fail
    if (!SiteGlobals.isExporting) {
        when {
            isAuthenticated == null -> return // Still loading
            !isAuthenticated || (userRole != UserRole.Admin && userRole != UserRole.Moderator) -> {
                rememberPageContext().router.navigateTo(Navigation.Screen.Home.route)
                return
            }
        }
    }

    val breakpoint = rememberBreakpoint()
    var overflowMenuOpened by remember { mutableStateOf(false) }
    val sidePanelVisible = breakpoint >= Breakpoint.MD

    DsPageLayout(
        modifier = modifier,
        title = Res.String.DocumentTitle.format(title),
        header = {
            if (sidePanelVisible) return@DsPageLayout
            AdminHeader { overflowMenuOpened = true }
        },
        sidePanel = {
            if (!sidePanelVisible) return@DsPageLayout
            overflowMenuOpened = false
            AdminSidePanel()
        },
        overflowMenu = {
            OverflowMenu(
                logoImage = logoImage(ColorMode.current),
                onMenuClosed = { overflowMenuOpened = false }
            ) {
                AdminNavigationItems()
            }
        },
        overflowMenuOpened = overflowMenuOpened,
        footer = {
            AdminFooter()
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
