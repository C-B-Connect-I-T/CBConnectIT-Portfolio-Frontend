package cbconnectit.portfolio.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cbconnectit.portfolio.web.components.toast.ToastContainer
import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.repos.AuthRepo
import cbconnectit.portfolio.web.styles.CbDarkColorScheme
import cbconnectit.portfolio.web.styles.CbLightColorScheme
import cbconnectit.portfolio.web.utils.Constants.COLOR_MODE_KEY
import cbconnectit.portfolio.web.utils.SiteGlobals
import com.materialdesignsystem.MaterialTheme
import com.materialdesignsystem.extensions.ButtonSizeXL
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.lightened
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.layout.SurfaceVars
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.loadFromLocalStorage
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.button
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.link
import com.varabyte.kobweb.silk.theme.colors.systemPreference
import kotlinx.browser.document
import org.jetbrains.compose.web.css.vh

@InitSilk
fun updateTheme(ctx: InitSilkContext) {
    MaterialTheme.setSchemes(lightScheme = CbLightColorScheme, darkScheme = CbDarkColorScheme)

    val colorMode = ColorMode.loadFromLocalStorage(COLOR_MODE_KEY) ?: ColorMode.systemPreference

    ctx.config.initialColorMode = colorMode

    // Set document language to Dutch so browsers and search engines know the language.
    document.documentElement?.setAttribute("lang", "nl")

    // Script which runs at load time that needs to be kept in sync with `initialColorMode` above. This code checks
    // if the user's local color mode preference is different from what was exported by Kobweb, replacing it if
    // different to prevent a flash of color after the page loads.
    if (SiteGlobals.isExporting) {
        val node = document.createElement("script").apply {
            textContent = """
                {
                    const storedColor = localStorage.getItem('${COLOR_MODE_KEY}'); // 'LIGHT', 'DARK', or null
                    const desiredColor = storedColor ? `silk-${'$'}{storedColor.toLowerCase()}` : 'silk-dark';
                    const oppositeColor = desiredColor === 'silk-dark' ? 'silk-light' : 'silk-dark';
                    document.documentElement.classList.replace(oppositeColor, desiredColor);
                }
                """.trimIndent()
        }
        document.head?.appendChild(node)
    }

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
        registerStyleBase("body") {
            Modifier
                .fontFamily(
                    "-apple-system", "BlinkMacSystemFont", "Segoe UI", "Roboto", "Oxygen", "Ubuntu",
                    "Cantarell", "Fira Sans", "Droid Sans", "Helvetica Neue", "sans-serif"
                )
                .lineHeight(1.4)
                .backgroundColor(SurfaceVars.BackgroundColor.value())
        }
    }
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    NetworkingConfig.init(
        SiteGlobals.baseUrl,
        {
            AuthRepo.refreshToken()
        })

    LaunchedEffect(Unit) {
        AuthRepo.checkAuthStatus()
    }

    SilkApp {
        Surface(modifier = SmoothColorStyle.toModifier()) {
            Box(
                modifier = Modifier
                    .minHeight(100.vh)
                    .scrollBehavior(ScrollBehavior.Smooth),
                content = { content() }
            )

            // Global toast container
            ToastContainer()
        }
    }
}
