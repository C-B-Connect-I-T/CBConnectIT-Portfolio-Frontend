package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.svg.chevronRightSvg
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.maxLines
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.DsBorderRadius
import com.materialdesignsystem.components.widgets.TextButton
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.css.functions.url
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text

@Composable
fun PortfolioSection(
    projects: List<Project>,
    selectedWork: Project?,
    onWorkSelected: (Project) -> Unit
) {
    val overlayColor = ColorMode.current.toColorScheme.primary.toRgb().copy(alpha = 210)
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .id(Navigation.Screen.Home.PortfolioSection.id)
            .scrollMargin(Constants.HEADER_HEIGHT.px)
            .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(Constants.SECTION_WIDTH.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(
            modifier = Modifier.fillMaxWidth().margin(bottom = 25.px),
            section = Navigation.Screen.Home.PortfolioSection,
            showSeeAllButton = true,
            href = Navigation.Screen.Projects.route
        )

        Box(
            Modifier
                .fillMaxWidth()
                .borderRadius(20.px)
                .background(
                    Background.of(
                        image = BackgroundImage.of(url(selectedWork?.bannerImageUrl ?: Res.Image.portfolio1)),
                        position = BackgroundPosition.of(CSSPosition(50.percent, 50.percent)),
                        size = BackgroundSize.Cover
                    ),
                    Background.of(BackgroundImage.of(linearGradient(overlayColor, overlayColor)))
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (breakpoint >= Breakpoint.MD) 36.px else 16.px),
                verticalArrangement = Arrangement.spacedBy(if (breakpoint >= Breakpoint.MD) 36.px else 24.px)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .display(DisplayStyle.Flex)
                        .flexDirection(FlexDirection.Row)
                        .flexWrap(FlexWrap.Wrap)
                        .gap(12.px)
                ) {
                    projects.forEach { project ->
                        SpanText(
                            text = project.title,
                            modifier = ProjectNameStyle.toModifier()
                                .classNames(if (project.id == selectedWork?.id) "active" else "inactive")
                                .onClick { onWorkSelected(project) }
                        )
                    }
                }

                SpanText(
                    text = selectedWork?.shortDescription ?: "",
                    modifier = ProjectDescriptionStyle.toModifier()
                        .margin(topBottom = 8.px)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(75.percent)
                            .display(DisplayStyle.Flex)
                            .flexDirection(FlexDirection.Row)
                            .flexWrap(FlexWrap.Wrap)
                            .gap(12.px)
                            .padding(right = if (breakpoint > Breakpoint.MD) 100.px else 25.px)
                    ) {
                        selectedWork?.tags?.forEach {
                            SpanText(
                                text = it.name,
                                modifier = Modifier
                                    .padding(topBottom = 4.px, leftRight = 6.px)
                                    .backgroundColor(ColorMode.current.toColorScheme.secondaryContainer)
                                    .color(ColorMode.current.toColorScheme.onSecondaryContainer)
                                    .fontSize(11.px)
                                    .borderRadius(6.px)
                            )
                        }
                    }

                    TextButton(
                        modifier = Modifier
                            .setVariable(ButtonVars.Color, ColorMode.current.toColorScheme.inverseOnSurface)
                            .border(1.px, LineStyle.Solid, ColorMode.current.toColorScheme.onPrimary)
                            .visibility(Visibility.Hidden),
                        borderRadius = DsBorderRadius(6.px),
                        size = ButtonSize.SM,
                        onClick = {
                            //TODO: add navigation!
                            window.alert("Navigate to Project details")
                        }
                    ) {
                        Text(Res.String.ReadMore)

                        Spacer(Modifier.width(8.px))

                        chevronRightSvg(Modifier.minSize(18.px).maxSize(18.px).size(18.px).margin(top = 2.px))
                    }
                }
            }
        }
    }
}

val ProjectDescriptionStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth(100.percent)
            .margin(top = 24.px, bottom = 0.px)
            .fontSize(18.px)
            .color(colorMode.toColorScheme.onPrimary)
    }

    Breakpoint.MD {
        Modifier
            .fillMaxWidth(65.percent)
            .margin(top = 100.px, bottom = 0.px)
            .fontSize(24.px)
    }

    Breakpoint.LG {
        Modifier.fontSize(36.px)
    }
}

val ProjectNameStyle = CssStyle {
    base {
        Modifier
            .color(colorMode.toColorScheme.onPrimary)
            .padding(topBottom = 4.px, leftRight = 10.px)
            .margin(topBottom = 0.px)
            .fontSize(14.px)
            .borderRadius(6.px)
            .cursor(Cursor.Pointer)
            .userSelect(UserSelect.None) // No selecting text within buttons
            .maxLines(1)
    }

    cssRule(".active") {
        Modifier
            .backgroundColor(colorMode.toColorScheme.surface)
            .color(colorMode.toColorScheme.onSurface)
    }

    hover {
        Modifier
            .color(colorMode.toColorScheme.onPrimary)
            .backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 50))
    }

    focus {
        Modifier
            .color(colorMode.toColorScheme.onPrimary)
            .backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 50))
    }
}
