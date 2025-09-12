package cbconnectit.portfolio.web.styles

import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.filter
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

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