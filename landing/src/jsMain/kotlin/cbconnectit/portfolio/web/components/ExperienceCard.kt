package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.backdropGradient
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.extensions.techStackSvg
import cbconnectit.portfolio.web.primaryGradient
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.BoxShadow
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.style.breakpoint.displayUntil
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.*

fun Modifier.gridTemplate(vararg sizes: CSSLengthOrPercentageNumericValue) = gridTemplateColumns {
    sizes.forEach { size(it) }
}

@Composable
fun ExperienceCard(
    active: Boolean = false,
    experience: Experience,
) {
    val baseModifier = Modifier.fillMaxWidth().display(DisplayStyle.Grid)

    Row(
        modifier = baseModifier
            .displayIfAtLeast(Breakpoint.LG)
            .gridTemplate(20.percent, 10.percent, 70.percent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExperienceDetails(experience)
        TimelineMarker(active, experience)
        ExperienceDescription(active, experience.description)
    }

    Row(
        modifier = baseModifier
            .displayUntil(Breakpoint.LG)
            .gridTemplate(15.percent, 85.percent)
    ) {
        TimelineMarker(active, experience)

        Column(
            modifier = Modifier.margin(topBottom = 20.px),
            verticalArrangement = Arrangement.spacedBy(8.px)
        ) {
            ExperienceDetails(experience)
            ExperienceDescription(active, experience.description)
        }
    }
}

private val CardShadow = listOf(
    BoxShadow.of(0.px, 1.px, 2.px, 0.px, Colors.Black.copyf(alpha = 0.3f)),
    BoxShadow.of(0.px, 2.px, 6.px, 2.px, Colors.Black.copyf(alpha = 0.15f))
)

@Composable
private fun ExperienceDescription(
    active: Boolean,
    description: String
) {
    val breakpoint = rememberBreakpoint()
    val colorMode by ColorMode.currentState

    val textColor = if (active) colorMode.toColorScheme.onPrimary else colorMode.toColorScheme.onSurface
    val backgroundGradient = if (active) colorMode.primaryGradient else colorMode.backdropGradient

    // TODO: could be replaced with a DsCard in the future...
    //      but it requires some adjustments to the DsCard to be more flexible and have more styles
    Box(
        modifier = Modifier.fillMaxWidth()
            .margin(topBottom = if (breakpoint > Breakpoint.MD) 20.px else 0.px)
            .padding(all = 14.px)
            .color(textColor)
            .backgroundImage(backgroundGradient)
            .borderRadius(12.px)
            .boxShadow(CardShadow)
    ) {
        SpanText(
            text = description,
            modifier = Modifier.lineHeight(1.65)
        )
    }
}

@Composable
private fun ExperienceDetails(
    experience: Experience
) {
    val breakpoint = rememberBreakpoint()
    val textAlign = if (breakpoint > Breakpoint.MD) TextAlign.End else TextAlign.Start

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(left = 32.px),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = if (breakpoint > Breakpoint.MD) Alignment.End else Alignment.Start
    ) {
        SpanText(
            text = experience.formattedJobPosition,
            modifier = Modifier
                .fontSize(20.px)
                .fontWeight(FontWeight.Bold)
                .color(ColorMode.current.toColorScheme.primary)
                .textAlign(textAlign)
        )

        SpanText(
            text = experience.formattedDate,
            modifier = Modifier
                .fontSize(14.px)
                .textAlign(textAlign)
        )

        SpanText(
            text = experience.company.name,
            modifier = Modifier
                .fontSize(14.px)
                .textAlign(textAlign)
        )
    }
}

@Composable
private fun TimelineMarker(
    active: Boolean,
    experience: Experience
) {
    val colorMode by ColorMode.currentState

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Vertical upper line
        Box(
            modifier = Modifier
                .weight(1)
                .width(3.px)
                .backgroundColor(colorMode.toColorScheme.primary)
        )

        Column(
            modifier = Modifier
                .padding(leftRight = 4.px, topBottom = 10.px)
                .border(width = 3.px, style = LineStyle.Solid, colorMode.toColorScheme.primary)
                .backgroundColor(if (active) colorMode.toColorScheme.primary else Color.transparent)
                .borderRadius(50.px),
            verticalArrangement = Arrangement.spacedBy(10.px)
        ) {
            experience.tags.forEach { tag ->
                experience.techStackSvg(tag, if (active) colorMode.toColorScheme.onPrimary else colorMode.toColorScheme.primary)
            }
        }

        // Vertical bottom line
        Box(
            modifier = Modifier
                .weight(1)
                .width(3.px)
                .backgroundColor(colorMode.toColorScheme.primary)
        )
    }
}
