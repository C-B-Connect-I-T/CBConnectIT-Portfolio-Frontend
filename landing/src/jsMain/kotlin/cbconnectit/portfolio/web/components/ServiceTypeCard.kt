package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.extensions.getServiceTypeIcon
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun ServiceTypeCard(
    service: Service
) {
    Link(
        "#${service.id}",
        ServicesTypeCardStyle.toModifier()
            .fillMaxWidth()
            .textDecorationLine(TextDecorationLine.None)
    ) {
        Column(
            Modifier
                .borderRadius(12.px)
                .padding(topBottom = 24.px, leftRight = 16.px)
                .backgroundColor(ColorMode.current.toColorScheme.surfaceVariant)
                .color(ColorMode.current.toColorScheme.onSurfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            service.getServiceTypeIcon(ColorMode.current.toColorScheme.primary, Modifier.size(56.px))

            Spacer(Modifier.height(10.px))

            P(
                attrs = Modifier
                    .fillMaxWidth()
                    .textAlign(TextAlign.Center)
                    .margin(topBottom = 0.px)
                    .fontSize(22.px)
                    .toAttrs()
            ) {
                Text(service.title)
            }
        }
    }
}

val ServicesTypeCardStyle = CssStyle {
    base {
        Modifier.translateY(0.px)
            .transition(Transition.of(property = "translate", duration = 200.ms))
    }

    hover {
        Modifier.translateY((-10).px)
    }
}
