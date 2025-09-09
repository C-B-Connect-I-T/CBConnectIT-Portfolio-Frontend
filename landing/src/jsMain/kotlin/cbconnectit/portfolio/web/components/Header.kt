package cbconnectit.portfolio.web.components

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.styles.LogoStyle
import cbconnectit.portfolio.web.svg.darkModeSvg
import cbconnectit.portfolio.web.svg.lightModeSvg
import cbconnectit.portfolio.web.svg.overflowMenuSvg
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.logoImage
import com.materialdesignsystem.components.sections.NavigationItem
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.events.Event

@Composable
fun Header(
    showMenu: Boolean = true,
    onMenuClicked: () -> Unit
) {
    val breakpoint = rememberBreakpoint()
    var scroll: Double? by remember { mutableStateOf(null) }
    var colorMode by ColorMode.currentState

    LaunchedEffect(Unit) {
        window.addEventListener(type = "scroll", callback = {
            scroll = document.documentElement?.scrollTop
        })
    }

    val backgroundColor = colorMode.toColorScheme.background

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.px)
            .top(0.percent) // Make it work with sticky!
            .position(Position.Sticky)
            .zIndex(1)
            .backgroundImage(
                linearGradient(LinearGradient.Direction.ToBottom) {
                    add(backgroundColor)
                    add(backgroundColor.toRgb().copyf(alpha = 0.5f))
                }
            )
            .backdropFilter(blur(5.px))
            .thenIf((scroll ?: 0.0) >= 50) {
                Modifier.boxShadow(0.px, 1.px, 5.px, 0.px, colorMode.toColorScheme.primary)
            }
            .padding(leftRight = 30.px),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeftSide(showMenu, breakpoint, colorMode, onMenuClicked)

        if (breakpoint > Breakpoint.MD && showMenu) {
            RightSide()
        }

        ThemedButton(
            onClick = {
                // Toggle the color mode
                colorMode = colorMode.opposite
                // Trigger a custom event, so we can listen to this change in order to recalculate grid item size for the testimonials
                window.dispatchEvent(Event("update-color-mode"))
            },
            primary = true,
            size = ButtonSize.SM,
            shape = ButtonShape.CIRCLE
        ) {
            when (colorMode) {
                ColorMode.DARK -> lightModeSvg(colorMode.toColorScheme.background)
                ColorMode.LIGHT -> darkModeSvg(colorMode.toColorScheme.background)
            }
        }
        Tooltip(ElementTarget.PreviousSibling, Res.String.ToggleColorMode)
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
                modifier = Modifier.cursor(Cursor.Pointer).onClick {
                    onMenuClicked()
                },
                fill = colorMode.toColorScheme.onBackground
            )

            Div(attrs = Modifier.width(24.px).toAttrs())
        }

        A(
            "/",
            attrs = {
                onClick {
                    ctx.router.navigateTo("/")
                }
            }) {
            Image(
                modifier = LogoStyle.toModifier().height(40.px),
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