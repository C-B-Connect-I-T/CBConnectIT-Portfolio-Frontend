package cbconnectit.portfolio.web.styles

import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.active
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.selectors.hover

val ProjectNameStyle = CssStyle {
    hover {
        Modifier.backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 50))
    }

    focus {
        Modifier.backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 50))
    }

    active {
        Modifier.backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 100))
    }
}