package cbconnectit.portfolio.web.sections

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.components.Spacer
import cbconnectit.portfolio.web.components.TextPrimaryButtonVariant
import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.repos.ProjectRepo
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.styles.ProjectNameStyle
import cbconnectit.portfolio.web.svg.chevronRightSvg
import cbconnectit.portfolio.web.utils.Config
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.maxLines
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
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun PortfolioSection() {
    Box(
        modifier = Modifier
            .id(Navigation.Screen.Home.PortfolioSection.id)
            .scrollMargin(80.px)
            .fillMaxWidth()
            .maxWidth(Constants.SECTION_WIDTH.px),
        contentAlignment = Alignment.Center
    ) {
        PortfolioContent()
    }
}

@Composable
fun PortfolioContent() {

    var works by remember { mutableStateOf(emptyList<Project>()) }
    var selectedWork by remember { mutableStateOf<Project?>(null) }
    val pageContext = rememberPageContext()

    LaunchedEffect(Unit) {
        works = ProjectRepo.getProjects(Config.baseUrl)
        selectedWork = works.first()
    }

    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent),
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
                .backgroundSize(BackgroundSize.Cover)
                .backgroundPosition(BackgroundPosition.of(CSSPosition(50.percent, 50.percent)))
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
                    works.forEach { project ->
                        P(
                            ProjectNameStyle.toModifier()
                                .color(ColorMode.current.toColorScheme.onPrimary)
                                .thenIf(project.id == selectedWork?.id) {
                                    Modifier
                                        .backgroundColor(ColorMode.current.toColorScheme.surface)
                                        .color(ColorMode.current.toColorScheme.onSurface)
                                }
                                .onClick {
                                    selectedWork = project
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

                    Button(
                        modifier = Modifier
                            .border(1.px, LineStyle.Solid, ColorMode.current.toColorScheme.onPrimary)
                            .borderRadius(6.px)
                            .visibility(Visibility.Hidden)
                            .color(ColorMode.current.toColorScheme.onPrimary),
                        variant = TextPrimaryButtonVariant,
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