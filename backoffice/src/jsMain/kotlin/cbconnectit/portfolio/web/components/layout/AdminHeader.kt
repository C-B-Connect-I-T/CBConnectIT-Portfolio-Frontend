package cbconnectit.portfolio.web.components.layout

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.logoImage
import cbconnectit.portfolio.web.utils.withAlpha
import com.materialkobweb.components.sections.BaseHeader
import com.materialkobweb.components.svg.overflowMenuSvg
import com.materialkobweb.styles.MaterialColorVars
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px

private const val HEADER_BACKGROUND_ALPHA = 0.6f

@Composable
fun AdminHeader(
    onMenuClick: () -> Unit
) {
    val ctx = rememberPageContext()
    val colorMode = ColorMode.current

    BaseHeader(
        modifier = Modifier
            .padding(leftRight = 24.px)
            .backgroundImage(
                linearGradient(
                    MaterialColorVars.Background.value(),
                    MaterialColorVars.Background.withAlpha(HEADER_BACKGROUND_ALPHA),
                    LinearGradient.Direction.ToBottom
                )
            )
    ) {
        overflowMenuSvg(
            modifier = Modifier
                .margin(right = 24.px, left = 24.px)
                .cursor(Cursor.Pointer)
                .onClick {
                    onMenuClick()
                },
            fill = MaterialColorVars.OnSurface.value()
        )

        // Logo
        Image(
            src = logoImage(colorMode),
            alt = "Logo image",
            modifier = Modifier
                .fillMaxHeight()
                .padding(topBottom = 24.px)
                .cursor(Cursor.Pointer)
                .onClick {
                    ctx.router.navigateTo(Navigation.Screen.Admin.Home.route)
                }
        )

    }
}

