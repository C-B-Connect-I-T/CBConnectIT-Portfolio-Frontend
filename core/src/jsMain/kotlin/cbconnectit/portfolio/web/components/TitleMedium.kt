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

val TitleMediumStyle = CssStyle {
    base {
        Modifier
            .fontSize(20.px)
            .lineHeight(20.px * FONT_SIZE_MULTIPLIER)
    }

    // From MD to above
    Breakpoint.MD {
        Modifier
            .fontSize(24.px)
            .lineHeight(24.px * FONT_SIZE_MULTIPLIER)
    }

    // From LG to above
    Breakpoint.LG {
        Modifier
            .fontSize(28.px)
            .lineHeight(28.px * FONT_SIZE_MULTIPLIER)
    }
}

@Composable
fun TitleMedium(
    modifier: Modifier = Modifier,
    text: String,
    color: CSSColorValue
) {
    SpanText(
        modifier = TitleMediumStyle.toModifier()
            .then(modifier)
            .fontWeight(FontWeight.Medium)
            .color(color),
        text = text
    )
}
