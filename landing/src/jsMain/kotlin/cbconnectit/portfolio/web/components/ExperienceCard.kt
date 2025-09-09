package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.extensions.techStackSvg
import cbconnectit.portfolio.web.utils.Constants
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
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
//    @Composable // TODO: check what this is about?
    if (breakpoint > Breakpoint.MD) {
        Row(
            Modifier
                .fillMaxWidth()
                .display(DisplayStyle.Grid)
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
            Modifier.fillMaxWidth()
                .display(DisplayStyle.Grid)
                .gridTemplateColumns {
                    size(15.percent)
                    size(85.percent)
                },
        ) {
            ExperienceNumber(active, experience)

            Column {
                Spacer(Modifier.height(20.px))
                ExperienceDetails(breakpoint, experience)
                Spacer(Modifier.height(8.px))
                ExperienceDescription(breakpoint, active, experience.description)
                Spacer(Modifier.height(20.px))
            }
        }
    }
}

@Composable
fun ExperienceDescription(
    breakpoint: Breakpoint,
    active: Boolean,
    description: String
) {
    val colorMode by ColorMode.currentState

    val gradient = if (colorMode.isLight) {
        linearGradient(LinearGradient.Direction.ToBottomRight) {
            add(Color.rgb(237, 240, 242))
            add(Colors.White)
        }
    } else {
        linearGradient(LinearGradient.Direction.ToBottomRight) {
            add(Color.rgb(20, 22, 28))
            add(Color.rgb(76, 79, 82))
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .thenIf(breakpoint > Breakpoint.MD) {
                Modifier.margin(topBottom = 20.px)
            }
            .padding(all = 14.px)
            .color(if (active) colorMode.toColorScheme.onPrimary else colorMode.toColorScheme.onSurface)
            .backgroundImage(
                if (active) {
                    linearGradient(LinearGradient.Direction.ToBottomRight) {
                        add(colorMode.toColorScheme.primary)
                        add(colorMode.toColorScheme.primary)
                    }
                } else {
                    gradient
                }
            )
            .borderRadius(12.px)
            .attrsModifier {
                style {
                    // TODO: replace this when this ticket is fixed "https://github.com/varabyte/kobweb/issues/247"
                    // "elevation Light 2 ----- Material 3
                    property("box-shadow", "rgba(0, 0, 0, 0.3) 0px 1px 2px 0px, rgba(0, 0, 0, 0.15) 0px 2px 6px 2px")
                }
            }
    ) {
        P(
            attrs = Modifier
                .margin(topBottom = 0.px)
                .fontFamily(Constants.FONT_FAMILY)
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
fun ExperienceDetails(
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
                    .fontFamily(Constants.FONT_FAMILY)
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
                    .fontFamily(Constants.FONT_FAMILY)
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
                    .fontFamily(Constants.FONT_FAMILY)
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
fun ExperienceNumber(
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
            Column {
                experience.tags.forEachIndexed { index, tag ->
                    if (index != 0) {
                        Spacer(Modifier.height(10.px))
                    }
                    experience.techStackSvg(tag, if (active) colorMode.toColorScheme.onPrimary else colorMode.toColorScheme.primary)
                }
            }
        }
    }
}