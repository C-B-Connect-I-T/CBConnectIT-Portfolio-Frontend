package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px

@Composable
fun HighLightCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val colorMode by ColorMode.currentState

    Backdrop(modifier) {
        Column(
            modifier = Modifier
                .size(100.px)
                .color(colorMode.toColorScheme.onBackground),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()

            Spacer(Modifier.height(12.px))

            SpanText(
                text = title,
                modifier = Modifier.color(colorMode.toColorScheme.primary)
            )

            Spacer(Modifier.height(4.px))

            SpanText(
                text = subtitle,
                modifier = Modifier.fontSize(11.px)
            )
        }
    }
}