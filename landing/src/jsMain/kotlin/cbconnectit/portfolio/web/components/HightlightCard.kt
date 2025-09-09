package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun HighLightCard(
    colorMode: ColorMode,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    Backdrop(colorMode, modifier) {
        Column(
            modifier = Modifier
                .width(100.px)
                .height(100.px)
                .color(colorMode.toPalette().color),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()

            Spacer(Modifier.height(12.px))

            P(
                attrs = Modifier
                    .color(colorMode.toColorScheme.primary)
                    .margin(topBottom = 0.px)
                    .fontSize(16.px)
                    .toAttrs()
            ) {
                Text(title)
            }

            Spacer(Modifier.height(4.px))

            P(
                attrs = Modifier
                    .margin(topBottom = 0.px)
                    .fontSize(11.px)
                    .toAttrs()
            ) {
                Text(subtitle)
            }
        }
    }
}