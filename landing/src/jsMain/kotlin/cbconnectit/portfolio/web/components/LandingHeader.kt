package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.pages.sections.NavigationItems
import cbconnectit.portfolio.web.svg.darkModeSvg
import cbconnectit.portfolio.web.svg.lightModeSvg
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.logoImage
import com.materialdesignsystem.components.sections.BaseHeader
import com.materialdesignsystem.components.widgets.BorderRadius
import com.materialdesignsystem.components.widgets.DsMaterialSymbols
import com.materialdesignsystem.components.widgets.FilledIconButton
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.style.breakpoint.displayUntil
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.w3c.dom.events.Event

@Composable
fun LandingHeader(
    showMenu: Boolean = true,
    onMenuClicked: () -> Unit
) {
    var colorMode by ColorMode.currentState

    BaseHeader(
        modifier = Modifier.padding(leftRight = 24.px),
        backgroundColor = colorMode.toColorScheme.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LeftSide(showMenu, colorMode, onMenuClicked)

            if (showMenu) {
                Row(
                    modifier = Modifier.displayIfAtLeast(Breakpoint.LG).margin(right = 30.px),
                    horizontalArrangement = Arrangement.spacedBy(30.px, Alignment.End),
                    content = { NavigationItems() }
                )
            }
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
private fun LeftSide(
    showMenu: Boolean,
    colorMode: ColorMode,
    onMenuClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showMenu) {
            DsMaterialSymbols(
                modifier = Modifier
                    .displayUntil(Breakpoint.LG)
                    .cursor(Cursor.Pointer)
                    .onClick { onMenuClicked() },
                icon = "menu",
                color = colorMode.toColorScheme.onBackground
            )
        }

        A(
            href = Navigation.Screen.Home.route,
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.height(40.px),
                    src = logoImage(colorMode),
                    alt = "Logo Image"
                )
            }
        }
    }
}
