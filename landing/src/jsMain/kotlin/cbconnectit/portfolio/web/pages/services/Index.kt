package cbconnectit.portfolio.web.pages.services

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.ServiceTypeCard
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.data.repos.ServiceRepo
import cbconnectit.portfolio.web.models.enums.TechnologyStacks
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.markdownParagraph
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.FilledButton
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
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
fun ServicesPage() {
    val breakpoint = rememberBreakpoint()
    var services by remember { mutableStateOf<List<Service>>(emptyList()) }

    LaunchedEffect(Unit) {
        services = ServiceRepo.getServices()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        PageLayout(
            title = Res.String.ServiceDocumentTitle,
            showMenuItems = false,
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ServicesPageHeader(breakpoint, services)

                Spacer(Modifier.height(68.px))

                ServicesPageList(breakpoint, services)

                Spacer(Modifier.height(68.px))

                ServicesPageTechnologyStacks(breakpoint)

                Spacer(Modifier.height(68.px))
            }
        }
    }
}

@Composable
private fun ServicesPageHeader(breakpoint: Breakpoint, services: List<Service>) {
    Box(
        Modifier
            .backgroundImage(url(Res.Image.servicesBanner))
            .backgroundSize(BackgroundSize.Cover)
            .backgroundPosition(BackgroundPosition.of(CSSPosition(50.percent, 50.percent)))
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .backgroundColor(ColorMode.current.toColorScheme.primary.toRgb().copy(alpha = 210))
        )

        Box(
            Modifier.fillMaxWidth().maxWidth(Constants.SECTION_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                Modifier
                    .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
                    .margin(topBottom = if (breakpoint >= Breakpoint.MD) 108.px else 56.px)
            ) {
                P(
                    Modifier
                        .color(ColorMode.current.toColorScheme.onPrimary)
                        .maxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 100.percent)
                        .margin(topBottom = 0.px)
                        .fontSize(32.px)
                        .fontWeight(FontWeight.Bold)
                        .toAttrs()
                ) {
                    Text(
                        Res.String.MyServices
                    )
                }

                Spacer(Modifier.height(12.px))

                P(
                    Modifier
                        .color(ColorMode.current.toColorScheme.onPrimary)
                        .maxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 100.percent)
                        .margin(topBottom = 0.px)
                        .fontSize(22.px)
                        .toAttrs {
                            markdownParagraph(Res.String.ServicesBannerDescription)
                        }
                )

                if (breakpoint <= Breakpoint.MD) {
                    Spacer(Modifier.height(68.px))

                    services.forEachIndexed { index, service ->
                        if (index != 0) {
                            Spacer(Modifier.height(50.px))
                        }

                        ServiceTypeCard(service)
                    }
                }
            }
        }
    }

    if (breakpoint > Breakpoint.MD) {
        Box(
            Modifier.fillMaxWidth().maxWidth(Constants.SECTION_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                Modifier
                    .margin(leftRight = 144.px)
                    .margin(top = (-50).px)
                    .fillMaxWidth(80.percent)
                    .maxWidth(Constants.SECTION_WIDTH.px)
            ) {
                services.forEachIndexed { index, service ->
                    if (index != 0) {
                        Spacer(Modifier.width(100.px))
                    }

                    ServiceTypeCard(service)
                }
            }
        }
    }
}

@Composable
private fun ServicesPageList(breakpoint: Breakpoint, services: List<Service>) {
    val pageContext = rememberPageContext()

    services.forEachIndexed { index, service ->
        val leftAligned = index % 2 == 0

        if (index != 0) {
            Spacer(Modifier.height(68.px))
        }

        Box(
            Modifier
                .fillMaxWidth()
                .thenIf(leftAligned.not()) {
                    Modifier.backgroundColor(ColorMode.current.toColorScheme.secondaryContainer)
                        .color(ColorMode.current.toColorScheme.onSecondaryContainer)
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .maxWidth(Constants.SECTION_WIDTH.px),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .id(service.id)
                        .scrollMargin(top = 60.px)
                        .display(DisplayStyle.Flex)
                        .flexDirection(if (breakpoint > Breakpoint.MD) FlexDirection.Row else FlexDirection.Column)
                        .alignItems(AlignItems.Center)
                        .padding(topBottom = 50.px, leftRight = 10.percent)
                ) {
                    if (leftAligned && breakpoint > Breakpoint.MD) {
                        Image(service.imageUrl, Modifier.width(350.px))

                        Spacer(Modifier.width(100.px))
                    }

                    Column(Modifier.fillMaxWidth()) {
                        P(
                            attrs = Modifier
                                .fontSize(32.px)
                                .fontWeight(FontWeight.Bold)
                                .toAttrs()
                        ) {
                            Text(service.title)
                        }

                        Spacer(Modifier.height(24.px))

                        P(
                            attrs = Modifier
                                .fontSize(22.px)
                                .toAttrs {
                                    markdownParagraph(service.description ?: "")
                                }
                        )

                        Spacer(Modifier.height(24.px))

                        A(
                            href = Navigation.Screen.Services.getService(service.id),
                            attrs = Modifier
                                .textDecorationLine(TextDecorationLine.None)
                                .toAttrs()
                        ) {
                            FilledButton(
                                onClick = {}
                            ) {
                                Text(Res.String.LearnMore)
                            }
                        }
                    }


                    if (leftAligned.not() || breakpoint <= Breakpoint.MD) {
                        Spacer(
                            Modifier
                                .width(100.px)
                                .thenIf(breakpoint <= Breakpoint.MD) {
                                    Modifier.height(40.px)
                                })

                        Image(
                            service.imageUrl,
                            Modifier.maxWidth(350.px)
                                .thenIf(breakpoint <= Breakpoint.MD) {
                                    Modifier.fillMaxWidth(90.percent)
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicesPageTechnologyStacks(breakpoint: Breakpoint) {
    Box(
        Modifier
            .fillMaxWidth()
            .maxWidth(Constants.SECTION_WIDTH.px),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(
                if (breakpoint >= Breakpoint.MD) 70.percent else 90.percent
            ),
        ) {
            P(
                attrs =
                    Modifier
                        .fillMaxWidth()
                        .textAlign(TextAlign.Center)
                        .fontWeight(FontWeight.Bold)
                        .fontSize(if (breakpoint < Breakpoint.MD) 32.px else 45.px)
                        .toAttrs()
            ) {
                Text(Res.String.TechnologyStacks)
            }

            Spacer(Modifier.height(40.px))

            Box(
                Modifier
                    .display(DisplayStyle.Flex)
                    .flexDirection(FlexDirection.Row)
                    .flexWrap(FlexWrap.Wrap)
                    .justifyContent(JustifyContent.Center)
                    .gap(60.px)
            ) {
                TechnologyStacks.entries.forEach {
                    Image(it.icon, modifier = Modifier.size(if (breakpoint < Breakpoint.MD) 58.px else 114.px))
                }
            }
        }
    }
}