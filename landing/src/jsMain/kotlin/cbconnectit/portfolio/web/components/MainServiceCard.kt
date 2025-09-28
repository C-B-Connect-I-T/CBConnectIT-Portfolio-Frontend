package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.extensions.getServiceTypeIcon
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.svg.chevronRightSvg
import cbconnectit.portfolio.web.utils.Res
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.DsBorderRadius
import com.materialdesignsystem.components.widgets.TextButton
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

@Composable
fun MainServiceCard(service: Service) {
    val colorMode by ColorMode.currentState

    Column(
        verticalArrangement = Arrangement.spacedBy(10.px)
    ) {
        Column(
            modifier = Modifier.margin(left = 8.px),
            verticalArrangement = Arrangement.spacedBy(10.px)
        ) {
            service.getServiceTypeIcon(colorMode.toColorScheme.primary, Modifier.size(34.px))

            SpanText(
                text = service.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 0.px, bottom = 0.px)
                    .fontSize(22.px)
                    .fontWeight(FontWeight.Bold)
            )

            SpanText(
                text = service.shortDescription ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 0.px, bottom = 0.px)
                    .fontSize(14.px)
                    .fontWeight(FontWeight.Normal)
            )
        }

        A(
            href = Navigation.Screen.Services.getService(service.id),
            attrs = Modifier
                .textDecorationLine(TextDecorationLine.None)
                .toAttrs()
        ) {
            TextButton(
                modifier = Modifier
                    .setVariable(ButtonVars.Color, colorMode.toColorScheme.primary),
                borderRadius = DsBorderRadius(6.px),
                size = ButtonSize.SM,
                onClick = { }
            ) {
                Text(Res.String.ReadMore)

                Spacer(Modifier.width(8.px))

                chevronRightSvg(Modifier.size(18.px).margin(top = 2.px))
            }
        }
    }
}