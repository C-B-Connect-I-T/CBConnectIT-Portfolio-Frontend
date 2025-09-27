package cbconnectit.portfolio.web.pages.services

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.ServiceTypeCard
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.models.enums.TechnologyStacks
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.markdownParagraph
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.FilledButton
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
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.style.breakpoint.displayUntil
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Page("/services")
@Composable
fun ServicesPage() {
    val viewModel = remember { ServicesViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
//            when (it) { }
        }
    }

    ServicesPageContent(state, viewModel::sendIntent)
}

@Composable
fun ServicesPageContent(
    state: ServicesContract.State,
    sendIntent: (ServicesContract.Intent) -> Unit
) {
    PageLayout(
        modifier = Modifier.fillMaxSize(),
        title = Res.String.ServiceDocumentTitle,
        showMenuItems = false,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().margin(bottom = 68.px),
            verticalArrangement = Arrangement.spacedBy(68.px),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ServicesPageHeader(state.services)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(68.px)
            ) {
                state.services.forEachIndexed { index, service ->
                    val leftAligned = index % 2 == 0

                    ServicesPageList(service, leftAligned)
                }
            }

            ServicesPageTechnologyStacks()
        }
    }
}

@Composable
private fun ServicesPageHeader(services: List<Service>) {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Background.of(
                        image = BackgroundImage.of(url(Res.Image.servicesBanner)),
                        size = BackgroundSize.Cover,
                        position = BackgroundPosition.of(CSSPosition(50.percent, 50.percent))
                    ),
                    Background.of(
                        BackgroundImage.of(
                            linearGradient(ColorMode.current.toColorScheme.primary.toRgb().copy(alpha = 210), ColorMode.current.toColorScheme.primary.toRgb().copy(alpha = 210))
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .maxWidth(Constants.SECTION_WIDTH.px)
                    .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
                    .margin(topBottom = if (breakpoint >= Breakpoint.MD) 108.px else 56.px)
            ) {
                SpanText(
                    text = Res.String.MyServices,
                    modifier = Modifier
                        .color(ColorMode.current.toColorScheme.onPrimary)
                        .maxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 100.percent)
                        .fontSize(32.px)
                        .fontWeight(FontWeight.Bold)
                )

                Spacer(Modifier.height(12.px))

                P(
                    attrs = Modifier
                        .color(ColorMode.current.toColorScheme.onPrimary)
                        .maxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 100.percent)
                        .margin(topBottom = 0.px)
                        .fontSize(22.px)
                        .toAttrs {
                            markdownParagraph(Res.String.ServicesBannerDescription)
                        }
                )

                Column(
                    modifier = Modifier
                        .displayUntil(Breakpoint.LG)
                        .fillMaxWidth()
                        .margin(top = 68.px),
                    verticalArrangement = Arrangement.spacedBy(50.px)
                ) {
                    services.forEach { ServiceTypeCard(it) }
                }
            }
        }

        Row(
            modifier = Modifier
                .displayIfAtLeast(Breakpoint.LG)
                .margin(top = (-50).px)
                .fillMaxWidth(80.percent)
                .maxWidth(Constants.SECTION_WIDTH.px),
            horizontalArrangement = Arrangement.spacedBy(24.px)
        ) {
            services.forEach { ServiceTypeCard(it) }
        }
    }
}

@Composable
private fun ServicesPageList(service: Service, leftAligned: Boolean) {
    val breakpoint = rememberBreakpoint()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .thenIf(!leftAligned) {
                Modifier.backgroundColor(ColorMode.current.toColorScheme.secondaryContainer)
                    .color(ColorMode.current.toColorScheme.onSecondaryContainer)
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            Modifier
                .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 100.percent)
                .maxWidth(Constants.SECTION_WIDTH.px)
                .id(service.id)
                .scrollMargin(top = Constants.HEADER_HEIGHT.px)
                .display(DisplayStyle.Flex)
                .flexDirection(if (breakpoint > Breakpoint.MD) FlexDirection.Row else FlexDirection.Column)
                .alignItems(AlignItems.Center)
                .padding(topBottom = 50.px)
                .gap(if (breakpoint <= Breakpoint.MD) 40.px else 100.px)
        ) {
            Image(
                src = service.imageUrl,
                modifier = Modifier
                    .width(350.px)
                    .maxWidth(350.px)
                    .order(if (breakpoint > Breakpoint.MD && leftAligned) 0 else 1)
                    .thenIf(breakpoint <= Breakpoint.MD) {
                        Modifier.fillMaxWidth(90.percent)
                    }
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.px)
            ) {
                SpanText(
                    text = service.title,
                    modifier = Modifier
                        .fontSize(32.px)
                        .fontWeight(FontWeight.Bold)
                )

                P(
                    attrs = Modifier
                        .fontSize(22.px)
                        .toAttrs {
                            markdownParagraph(service.description)
                        }
                )

                A(
                    href = Navigation.Screen.Services.getService(service.id),
                    attrs = Modifier
                        .textDecorationLine(TextDecorationLine.None)
                        .toAttrs()
                ) {
                    FilledButton(onClick = {}) {
                        Text(Res.String.LearnMore)
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicesPageTechnologyStacks() {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 70.percent else 90.percent)
            .maxWidth(Constants.SECTION_WIDTH.px),
        verticalArrangement = Arrangement.spacedBy(40.px)
    ) {
        SpanText(
            text = Res.String.TechnologyStacks,
            modifier = Modifier
                .fillMaxWidth()
                .textAlign(TextAlign.Center)
                .fontWeight(FontWeight.Bold)
                .fontSize(if (breakpoint < Breakpoint.MD) 32.px else 45.px)
        )

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
