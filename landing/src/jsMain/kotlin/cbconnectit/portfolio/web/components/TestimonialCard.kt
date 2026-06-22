package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.utils.Identifiers.TestimonialSectionClasses.content
import cbconnectit.portfolio.web.utils.Identifiers.TestimonialSectionClasses.item
import com.materialkobweb.components.Spacer
import com.materialkobweb.components.widgets.DsMaterialSymbols
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.width
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
                val avatarModifier = Modifier
                    .weight(1)
                    .size(56.px)
                    .borderRadius(50.percent)

                val avatarUrl = testimonial.avatarImage?.url
                if (avatarUrl.isNullOrBlank()) {
                    Box(
                        modifier = avatarModifier,
                        contentAlignment = Alignment.Center
                    ) {
                        DsMaterialSymbols(
                            modifier = Modifier.fontSize(56.px),
                            icon = "account_circle",
                            color = colorMode.toColorScheme.primary
                        )
                    }
                } else {
                    Image(
                        avatarUrl,
                        modifier = avatarModifier,
                        alt = testimonial.avatarImage?.altText.orEmpty().ifBlank { "Avatar of ${testimonial.fullName}" }
                    )
                }

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
