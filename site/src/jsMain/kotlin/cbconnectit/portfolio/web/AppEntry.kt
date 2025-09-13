package cbconnectit.portfolio.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.styles.CbDarkColorScheme
import cbconnectit.portfolio.web.styles.CbLightColorScheme
import com.materialdesignsystem.MaterialTheme
import com.materialdesignsystem.constants.Constants
import com.materialdesignsystem.extensions.ButtonSizeXL
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.lightened
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.core.AppGlobals
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.button
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.link
import com.varabyte.kobweb.silk.theme.colors.systemPreference
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.vh

private const val COLOR_MODE_KEY = "cbconnectit:app:colorMode"

@InitSilk
fun updateTheme(ctx: InitSilkContext) {
    ctx.config.initialColorMode = localStorage.getItem(COLOR_MODE_KEY)?.let { ColorMode.valueOf(it) } ?: ColorMode.systemPreference

    ctx.theme.registerStyle("silk-button-size_xl", ButtonSizeXL)

    val lightColorScheme = ColorMode.LIGHT.toColorScheme
    val darkColorScheme = ColorMode.DARK.toColorScheme

    // Background
    ctx.theme.palettes.light.background = lightColorScheme.background
    ctx.theme.palettes.dark.background = darkColorScheme.background

    // Color
    ctx.theme.palettes.light.color = lightColorScheme.onBackground
    ctx.theme.palettes.dark.color = darkColorScheme.onBackground

    // Button
    ctx.theme.palettes.light.button.set(
        default = lightColorScheme.primary,
        hover = lightColorScheme.primary.darkened(0.08f),
        focus = lightColorScheme.primary.darkened(0.12f),
        pressed = lightColorScheme.primary.darkened(0.12f),
    )
    ctx.theme.palettes.dark.button.set(
        default = darkColorScheme.primary,
        hover = darkColorScheme.primary.lightened(0.08f),
        focus = darkColorScheme.primary.lightened(0.12f),
        pressed = darkColorScheme.primary.lightened(0.12f),
    )

    // Link
    ctx.theme.palettes.light.link.set(
        lightColorScheme.onBackground,
        lightColorScheme.onBackground
    )
    ctx.theme.palettes.dark.link.set(
        darkColorScheme.onBackground,
        darkColorScheme.onBackground
    )

    ctx.stylesheet.apply {
        val colorMode = localStorage.getItem(COLOR_MODE_KEY)?.let { ColorMode.valueOf(it) } ?: ColorMode.DARK

        registerStyleBase("body") {
            Modifier
                .fontFamily(
                    "-apple-system", "BlinkMacSystemFont", "Segoe UI", "Roboto", "Oxygen", "Ubuntu",
                    "Cantarell", "Fira Sans", "Droid Sans", "Helvetica Neue", "sans-serif"
                )
                .lineHeight(1.4)
                .backgroundColor(colorMode.toColorScheme.background)
        }
    }
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    MaterialTheme.setSchemes(lightScheme = CbLightColorScheme, darkScheme = CbDarkColorScheme)
    NetworkingConfig.init(AppGlobals[Constants.BASE_URL] ?: "")

    SilkApp {
        val colorMode = ColorMode.current

        LaunchedEffect(colorMode) {
            localStorage.setItem(COLOR_MODE_KEY, colorMode.name)
        }

        Surface(modifier = SmoothColorStyle.toModifier()) {
            Box(modifier = Modifier.minHeight(100.vh)) {
                content()
            }
        }
    }
}
