package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.BackgroundColor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.selectors.active
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import com.varabyte.kobweb.silk.theme.colors.shifted
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px


enum class ButtonShape {
    RECTANGLE,
    CIRCLE
}

private fun getButtonModifier(shape: ButtonShape): Modifier {
    return Modifier.padding(0.px).then(
        if (shape == ButtonShape.CIRCLE) {
            Modifier.borderRadius(50.percent)
        } else {
            Modifier.padding(12.px).fontWeight(600).borderRadius(8.px)
        }
    )
}

val PrimaryButtonVariant = ButtonStyle.addVariant {
    val backgroundColor = colorMode.toColorScheme.primary
    base {
        Modifier
            .backgroundColor(backgroundColor)
    }

    hover {
        Modifier.backgroundColor(backgroundColor.shifted(colorMode))
    }
}

val NormalButtonVariant = ButtonStyle.addVariant {
    val colorMode = colorMode.opposite

    base {
        Modifier
            .backgroundColor(colorMode.toColorScheme.background)
            .color(colorMode.toPalette().color)
    }
    hover {
        Modifier.backgroundColor(colorMode.toColorScheme.background.shifted(colorMode))
    }
}

val TextPrimaryButtonVariant = ButtonStyle.addVariant {
    base {
        Modifier
            .padding(leftRight = 8.px)
            .color(colorMode.toColorScheme.primary)
            .backgroundColor(BackgroundColor.Transparent)
    }
    hover {
        Modifier.backgroundColor(colorMode.toColorScheme.primary.shifted(colorMode.opposite, 0.5f))
    }
    active {
        Modifier.backgroundColor(colorMode.toColorScheme.primary.shifted(colorMode.opposite, 0.2f))
    }
}

/**
 * Create a [Button] which is styled with primary or secondary colors.
 *
 * @param primary If true, use styles that call this button out as one associated with a major action you want to draw
 *   attention to.
 * @param content If set, renders custom content on the button. If both this and [text] is specified, then this
 *   content will be rendered to the left of the text with a bit of margin. This is particularly useful for rendering
 *   logos.
 */
@Composable
fun ThemedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    shape: ButtonShape = ButtonShape.RECTANGLE,
    primary: Boolean = false,
    size: ButtonSize = ButtonSize.MD,
    content: @Composable () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
        size = size,
        modifier = modifier.then(getButtonModifier(shape)),
        variant = if (primary) PrimaryButtonVariant else NormalButtonVariant
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
            if (!text.isNullOrEmpty()) {
                SpanText(text)
            }
        }
    }
}