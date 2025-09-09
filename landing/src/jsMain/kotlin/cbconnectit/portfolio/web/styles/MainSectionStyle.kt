package cbconnectit.portfolio.web.styles

import cbconnectit.portfolio.web.utils.Identifiers.SocialBar.socialIcon
import cbconnectit.portfolio.web.utils.Identifiers.SocialBar.socialLink
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.anyLink
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.theme.colors.palette.link
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*

val CbNavigationItemStyle = CssStyle {
    base {
        Modifier
            .color(colorMode.toPalette().link.default)
            .transition(Transition.of(property = "color", duration = 200.ms))
    }

    anyLink {
        Modifier
            .color(colorMode.toPalette().link.default)
    }

    hover {
        Modifier
            .color(colorMode.toColorScheme.primary)
    }
}

@OptIn(ExperimentalComposeWebApi::class)
val LogoStyle = CssStyle {
    base {
        Modifier
            .transform { rotate(0.deg) }
            .transition(Transition.of(property = "transform", duration = 200.ms))
    }

    hover {
        Modifier
            .transform { rotate((-10).deg) }
    }
}

val SocialLinkStyle = CssStyle {
    cssRule(" > #$socialLink > #$socialIcon") {
        Modifier
            .color(colorMode.toColorScheme.onSurface)
            .transition(Transition.of(property = "color", duration = 200.ms))
    }

    cssRule(":hover > #$socialLink > #$socialIcon") {
        Modifier.color(colorMode.toColorScheme.primary)
    }
}

val MainButtonStyle = CssStyle {
    base {
        Modifier.width(100.px)
            .color(colorMode.toColorScheme.onPrimary)
            .transition(Transition.of(property = "width", duration = 200.ms))
    }

    hover {
        Modifier.width(120.px)
    }
}

@OptIn(ExperimentalComposeWebApi::class)
val MainImageStyle = CssStyle {
    base {
        Modifier
            .styleModifier {
                filter { grayscale(100.percent) }
            }
            .transition(Transition.of(property = "filter", duration = 200.ms))
    }

    hover {
        Modifier
            .styleModifier {
                filter { grayscale(0.percent) }
            }
    }
}