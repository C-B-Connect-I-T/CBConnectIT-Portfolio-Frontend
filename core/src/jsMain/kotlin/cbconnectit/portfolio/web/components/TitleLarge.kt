package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times

const val FONT_SIZE_MULTIPLIER = 1.2

val TitleLargeStyle = CssStyle {
    base {
        Modifier
            .fontSize(26.px)
            .lineHeight(26.px * FONT_SIZE_MULTIPLIER)
    }

    // From MD to above
    Breakpoint.MD {
        Modifier
            .fontSize(32.px)
            .lineHeight(32.px * FONT_SIZE_MULTIPLIER)
    }

    // From LG to above
    Breakpoint.LG {
        Modifier
            .fontSize(36.px)
            .lineHeight(36.px * FONT_SIZE_MULTIPLIER)
    }
}

// TODO: replace this with the H1Heading everywhere. Do check the visuals if the UI is still correct!
@Composable
fun TitleLarge(
    modifier: Modifier = Modifier,
    text: String,
    color: CSSColorValue
) {
    SpanText(
        modifier = TitleLargeStyle.toModifier()
            .then(modifier)
            .fontWeight(FontWeight.Medium)
            .color(color),
        text = text
    )
}
