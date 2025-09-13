package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.backdropGradient
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.silk.components.icons.fa.FaAndroid
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.px

@Composable
fun Backdrop(
    colorMode: ColorMode,
    modifier: Modifier = Modifier,
    borderRadius: CSSSizeValue<CSSUnit.px> = 8.px,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val gradient = colorMode.backdropGradient

    Box(
        Modifier
            .id("parentBox")
            .backgroundImage(gradient)
            .borderRadius(borderRadius)
            .attrsModifier {
                style {
                    // TODO: replace this when this ticket is fixed "https://github.com/varabyte/kobweb/issues/247"
                    // "elevation Light 2 ----- Material 3
                    property("box-shadow", "rgba(0, 0, 0, 0.3) 0px 1px 2px 0px, rgba(0, 0, 0, 0.15) 0px 2px 6px 2px")
                }
            }
            .then(modifier),
        contentAlignment = Alignment.Center,
        content = content
    )
}