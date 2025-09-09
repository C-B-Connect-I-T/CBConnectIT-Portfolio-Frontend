package cbconnectit.portfolio.web.styles

import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px

val InputStyle = CssStyle {
    base {
        Modifier
            .border(width = 2.px, style = LineStyle.Solid, color = colorMode.toColorScheme.surface)
            .transition(Transition.of(property = "border", duration = 200.ms))
    }

    focus {
        Modifier.border(width = 2.px, style = LineStyle.Solid, color = colorMode.toColorScheme.primary)
    }

    hover {
        Modifier.border(width = 2.px, style = LineStyle.Solid, color = colorMode.toColorScheme.primary)
    }
}