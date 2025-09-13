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
import com.varabyte.kobweb.compose.css.functions.url
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.active
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun PortfolioSection(
    projects: List<Project>,
    selectedWork: Project?,
    onWorkSelected: (Project) -> Unit
) {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .id(Navigation.Screen.Home.PortfolioSection.id)
            .scrollMargin(80.px)
            .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(Constants.SECTION_WIDTH.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(
            Modifier.fillMaxWidth().margin(bottom = 25.px),
            Navigation.Screen.Home.PortfolioSection,
            showSeeAllButton = true,
            href = Navigation.Screen.Projects.route
        )

        Box(
            Modifier
                .borderRadius(20.px)
                .backgroundImage(url(selectedWork?.bannerImageUrl ?: Res.Image.portfolio1))
                .background {
                    size(BackgroundSize.Cover)
                    position(BackgroundPosition.of(CSSPosition(50.percent, 50.percent)))
                }
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .borderRadius(20.px)
                    .backgroundColor(ColorMode.current.toColorScheme.primary.toRgb().copy(alpha = 210))
            )

            Column(
                Modifier.fillMaxSize()
                    .padding(
                        leftRight = if (breakpoint >= Breakpoint.MD) 36.px else 16.px,
                        topBottom = if (breakpoint >= Breakpoint.MD) 36.px else 16.px
                    )
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .display(DisplayStyle.Flex)
                        .flexDirection(FlexDirection.Row)
                        .flexWrap(FlexWrap.Wrap)
                        .gap(12.px)
                        .margin(bottom = 8.px)
                ) {
                    projects.forEach { project ->
                        P(
                            ProjectNameStyle.toModifier()
                                .color(ColorMode.current.toColorScheme.onPrimary)
                                .thenIf(project.id == selectedWork?.id) {
                                    Modifier
                                        .backgroundColor(ColorMode.current.toColorScheme.surface)
                                        .color(ColorMode.current.toColorScheme.onSurface)
                                }
                                .onClick {
                                    onWorkSelected(project)
                                }
                                .padding(topBottom = 4.px, leftRight = 10.px)
                                .margin(topBottom = 0.px)
                                .fontSize(14.px)
                                .borderRadius(6.px)
                                .cursor(Cursor.Pointer)
                                .userSelect(UserSelect.None) // No selecting text within buttons
                                .maxLines(1)
                                .toAttrs()
                        ) { Text(project.title) }
                    }
                }

                Spacer(Modifier.height(if (breakpoint >= Breakpoint.MD) 36.px else 24.px))

                P(
                    Modifier
                        .fillMaxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 100.percent)
                        .margin(top = if (breakpoint > Breakpoint.MD) 100.px else 24.px, bottom = 0.px)
                        .fontSize(18.px)
                        .thenIf(breakpoint > Breakpoint.MD) { Modifier.fontSize(36.px) }
                        .thenIf(breakpoint == Breakpoint.MD) { Modifier.fontSize(24.px) }
                        .color(ColorMode.current.toColorScheme.onPrimary)
                        .toAttrs()
                ) {
                    Text(selectedWork?.shortDescription ?: "")
                }

                Spacer(Modifier.height(if (breakpoint >= Breakpoint.MD) 36.px else 24.px))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .margin(top = 8.px),
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
                            P(
                                Modifier
                                    .padding(topBottom = 4.px, leftRight = 6.px)
                                    .margin(topBottom = 0.px)
                                    .backgroundColor(ColorMode.current.toColorScheme.secondaryContainer)
                                    .color(ColorMode.current.toColorScheme.onSecondaryContainer)
                                    .fontSize(11.px)
                                    .borderRadius(6.px)
                                    .toAttrs()
                            ) { Text(it.name) }
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

val ProjectNameStyle = CssStyle {
    hover {
        Modifier.backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 50))
    }

    focus {
        Modifier.backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 50))
    }

    active {
        Modifier.backgroundColor(colorMode.toColorScheme.surface.toRgb().copy(alpha = 100))
    }
}