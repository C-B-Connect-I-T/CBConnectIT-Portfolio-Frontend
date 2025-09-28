package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.utils.Identifiers.TestimonialSectionClasses.content
import cbconnectit.portfolio.web.utils.Identifiers.TestimonialSectionClasses.item
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun TestimonialCard(
    testimonial: Testimonial
) {
    val colorMode by ColorMode.currentState

    Box(
        Modifier
            .classNames(item)
            .fillMaxWidth()
            .borderRadius(12.px)
            .border(2.px, LineStyle.Solid, colorMode.toColorScheme.outlineVariant)
    ) {
        Column(
            Modifier
                .classNames(content)
                .padding(20.px)
        ) {
            Row {
                Image(
                    testimonial.imageUrl,
                    modifier = Modifier
                        .weight(1)
                        .size(56.px)
                        .borderRadius(50.percent),
                    alt = "Avatar of ${testimonial.fullName}"
                )

                Spacer(Modifier.width(12.px))

                Column {
                    SpanText(
                        text = testimonial.fullName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .color(colorMode.toColorScheme.primary)
                            .fontSize(22.px)
                            .fontWeight(FontWeight.Bold)
                    )

                    Spacer(Modifier.height(4.px))

                    SpanText(
                        text = testimonial.jobPosition.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fontSize(14.px)
                            .fontWeight(FontWeight.Medium)
                    )
                }
            }

            Spacer(Modifier.height(16.px))

            SpanText(
                text = testimonial.review,
                modifier = Modifier
                    .fillMaxWidth()
                    .fontSize(14.px)
            )
        }
    }
}