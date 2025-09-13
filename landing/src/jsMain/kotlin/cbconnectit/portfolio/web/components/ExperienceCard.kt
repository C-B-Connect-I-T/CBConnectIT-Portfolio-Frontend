package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.backdropGradient
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.extensions.techStackSvg
import cbconnectit.portfolio.web.primaryGradient
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.BoxShadow
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun ExperienceCard(
    breakpoint: Breakpoint,
    active: Boolean = false,
    experience: Experience,
) {
    val baseModifier = Modifier.fillMaxWidth().display(DisplayStyle.Grid)
    if (breakpoint > Breakpoint.MD) {
        Row(
            modifier = baseModifier
                .gridTemplateColumns {
                    size(20.percent)
                    size(10.percent)
                    size(70.percent)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExperienceDetails(breakpoint, experience)
            ExperienceNumber(active, experience)
            ExperienceDescription(breakpoint, active, experience.description)
        }
    } else {
        Row(
            modifier = baseModifier
                .gridTemplateColumns {
                    size(15.percent)
                    size(85.percent)
                },
        ) {
            ExperienceNumber(active, experience)

            Column(
                modifier = Modifier.margin(topBottom = 20.px),
                verticalArrangement = Arrangement.spacedBy(8.px)
            ) {
                ExperienceDetails(breakpoint, experience)
                ExperienceDescription(breakpoint, active, experience.description)
            }
        }
    }
}

@Composable
private fun ExperienceDescription(
    breakpoint: Breakpoint,
    active: Boolean,
    description: String
) {
    val colorMode by ColorMode.currentState

    val gradient = colorMode.backdropGradient
    val activeGradient = colorMode.primaryGradient

    // TODO: could be replaced with a DsCard in the future...
    //      but it requires some adjustments to the DsCard to be more flexible and have more styles
    Box(
        modifier = Modifier.fillMaxWidth()
            .thenIf(breakpoint > Breakpoint.MD) {
                Modifier.margin(topBottom = 20.px)
            }
            .padding(all = 14.px)
            .color(if (active) colorMode.toColorScheme.onPrimary else colorMode.toColorScheme.onSurface)
            .backgroundImage(if (active) activeGradient else gradient)
            .borderRadius(12.px)
            .boxShadow(
                listOf(
                    BoxShadow.of(0.px, 1.px, 2.px, 0.px, Colors.Black.copyf(alpha = 0.3f)),
                    BoxShadow.of(0.px, 2.px, 6.px, 2.px, Colors.Black.copyf(alpha = 0.15f))
                )
            )
    ) {
        P(
            attrs = Modifier
                .margin(topBottom = 0.px)
                .fontSize(16.px)
                .whiteSpace(WhiteSpace.PreLine)
                .fontWeight(FontWeight.Normal)
                .lineHeight(1.65)
                .toAttrs()
        ) {
            Text(description)
        }
    }
}

@Composable
private fun ExperienceDetails(
    breakpoint: Breakpoint,
    experience: Experience,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(40.px))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = if (breakpoint > Breakpoint.MD) Alignment.End else Alignment.Start
        ) {
            P(
                attrs = Modifier
                    .margin(topBottom = 0.px)
                    .fontSize(20.px)
                    .fontWeight(FontWeight.Bold)
                    .color(ColorMode.current.toColorScheme.primary)
                    .thenIf(breakpoint > Breakpoint.MD) {
                        Modifier.textAlign(TextAlign.End)
                    }
                    .toAttrs()
            ) {
                Text(experience.formattedJobPosition)
            }

            P(
                attrs = Modifier
                    .margin(topBottom = 0.px)
                    .fontSize(14.px)
                    .fontWeight(FontWeight.Normal)
                    .thenIf(breakpoint > Breakpoint.MD) {
                        Modifier.textAlign(TextAlign.End)
                    }
                    .toAttrs()
            ) {
                Text(experience.formattedDate)
            }

            P(
                attrs = Modifier
                    .margin(topBottom = 0.px)
                    .fontSize(14.px)
                    .fontWeight(FontWeight.Normal)
                    .thenIf(breakpoint > Breakpoint.MD) {
                        Modifier.textAlign(TextAlign.End)
                    }
                    .toAttrs()
            ) {
                Text(experience.company.name)
            }
        }
    }
}

@Composable
private fun ExperienceNumber(
    active: Boolean,
    experience: Experience
) {
    val colorMode by ColorMode.currentState

    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxHeight()
                .width(3.px)
                .backgroundColor(colorMode.toColorScheme.primary)
        )

        Box(
            modifier = Modifier
                .padding(leftRight = 4.px, topBottom = 10.px)
                .border(width = 3.px, style = LineStyle.Solid, colorMode.toColorScheme.primary)
                .backgroundColor(if (active) colorMode.toColorScheme.primary else colorMode.toColorScheme.background)
                .borderRadius(50.px),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.px)
            ) {
                experience.tags.forEach { tag ->
                    experience.techStackSvg(tag, if (active) colorMode.toColorScheme.onPrimary else colorMode.toColorScheme.primary)
                }
            }
        }
    }
}