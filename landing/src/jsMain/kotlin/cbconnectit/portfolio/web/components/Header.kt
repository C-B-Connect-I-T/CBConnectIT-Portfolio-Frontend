package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.svg.darkModeSvg
import cbconnectit.portfolio.web.svg.lightModeSvg
import cbconnectit.portfolio.web.svg.overflowMenuSvg
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.logoImage
import com.materialdesignsystem.components.sections.BaseHeader
import com.materialdesignsystem.components.sections.NavigationItem
import com.materialdesignsystem.components.widgets.BorderRadius
import com.materialdesignsystem.components.widgets.FilledIconButton
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.w3c.dom.events.Event

@Composable
fun Header(
    showMenu: Boolean = true,
    onMenuClicked: () -> Unit
) {
    val breakpoint = rememberBreakpoint()
    var colorMode by ColorMode.currentState

    BaseHeader(
        modifier = Modifier.padding(left = 24.px, right = 24.px),
        backgroundColor = colorMode.toColorScheme.background
    ) {
        LeftSide(showMenu, breakpoint, colorMode, onMenuClicked)

        if (breakpoint > Breakpoint.MD && showMenu) {
            RightSide()
        }

        FilledIconButton(
            modifier = Modifier.setVariable(ButtonVars.BorderRadius, 999.px),
            onClick = {
                // Toggle the color mode
                colorMode = colorMode.opposite
                // Trigger a custom event, so we can listen to this change in order to recalculate grid item size for the testimonials
                window.dispatchEvent(Event("update-color-mode"))
            }
        ) {
            when (colorMode) {
                ColorMode.DARK -> lightModeSvg(colorMode.toColorScheme.background)
                ColorMode.LIGHT -> darkModeSvg(colorMode.toColorScheme.background)
            }
        }
        Tooltip(modifier = Modifier.zIndex(10), target = ElementTarget.PreviousSibling, text = Res.String.ToggleColorMode)
    }
}

@Composable
fun LeftSide(
    showMenu: Boolean,
    breakpoint: Breakpoint,
    colorMode: ColorMode,
    onMenuClicked: () -> Unit
) {
    val ctx = rememberPageContext()

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (breakpoint <= Breakpoint.MD && showMenu) {
            overflowMenuSvg(
                modifier = Modifier
                    .margin(right = 24.px)
                    .cursor(Cursor.Pointer)
                    .onClick {
                        onMenuClicked()
                    },
                fill = colorMode.toColorScheme.onBackground
            )
        }

        A(
            href = Navigation.Screen.Home.route,
            attrs = {
                onClick {
                    ctx.router.navigateTo(Navigation.Screen.Home.route)
                }
            }
        ) {
            Image(
                modifier = Modifier.height(40.px),
                src = logoImage(colorMode),
                alt = "Logo Image"
            )
        }
    }
}

@Composable
fun RightSide() {
    Row(
        modifier = Modifier
            .margin(right = 30.px)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(30.px, Alignment.End)
    ) {
        NavigationItem(
            href = Navigation.Screen.Home.HomeSection.path,
            title = Navigation.Screen.Home.HomeSection.title
        )

        NavigationItem(
            href = Navigation.Screen.Home.AboutSection.path,
            title = Navigation.Screen.Home.AboutSection.title
        )

        NavigationItem(
            href = Navigation.Screen.Home.ServiceSection.path,
            title = Navigation.Screen.Home.ServiceSection.title
        )

        NavigationItem(
            href = Navigation.Screen.Home.PortfolioSection.path,
            title = Navigation.Screen.Home.PortfolioSection.title
        )

        NavigationItem(
            href = Navigation.Screen.Home.ExperienceSection.path,
            title = Navigation.Screen.Home.ExperienceSection.title
        )

        NavigationItem(
            href = Navigation.Screen.Home.ContactSection.path,
            title = Navigation.Screen.Home.ContactSection.title
        )
    }
}