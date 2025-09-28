package cbconnectit.portfolio.web.pages.services.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.*
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.FilledButton
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.css.functions.url
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Page("/services/service")
@Composable
fun ServicePage() {
    val ctx = rememberPageContext()

    val serviceId = remember(ctx.route) {
        ctx.route.params["serviceId"]
    }

    val viewModel = remember(serviceId) { ServiceViewModel(serviceId) }
    val state by viewModel.state.collectAsState()

    ServicePageContent(state = state, sendIntent = viewModel::sendIntent)
}

@Composable
fun ServicePageContent(
    state: ServiceContract.State,
    sendIntent: (ServiceContract.Intent) -> Unit
) {
    val service = state.service
    val breakpoint = rememberBreakpoint()

    PageLayout(
        modifier = Modifier.fillMaxSize(),
        title = Res.String.SubServicesDocumentTitle.format(service?.title),
        showMenuItems = false,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ServiceBanner(service)

            val subServices = service?.subServices
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(68.px)
            ) {
                subServices?.forEachIndexed { index, service ->
                    val leftAligned = index % 2 == 0

                    SubServices(service, leftAligned)
                }
            }

            val extraInfo = service?.extraInfo
            if (!extraInfo.isNullOrEmpty()) {
                P(
                    attrs = Modifier
                        .margin(top = 68.px)
                        .maxWidth(Constants.SECTION_WIDTH.px)
                        .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
                        .fontSize(22.px)
                        .toAttrs {
                            markdownParagraph(extraInfo)
                        }
                )
            }
        }
    }
}

@Composable
fun ServiceBanner(service: Service?) {
    val breakpoint = rememberBreakpoint()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Background.of(
                    image = BackgroundImage.of(url(service?.bannerImageUrl ?: "")),
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
            Modifier
                .maxWidth(Constants.SECTION_WIDTH.px)
                .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
                .margin(topBottom = if (breakpoint >= Breakpoint.MD) 108.px else 56.px),
        ) {
            SpanText(
                text = service?.title ?: "",
                modifier = Modifier
                    .color(ColorMode.current.toColorScheme.onPrimary)
                    .maxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 85.percent)
                    .fontSize(32.px)
                    .fontWeight(FontWeight.Bold)
            )

            Spacer(Modifier.height(12.px))

            service?.bannerDescription?.let {
                P(
                    attrs = Modifier
                        .color(ColorMode.current.toColorScheme.onPrimary)
                        .maxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 85.percent)
                        .margin(topBottom = 0.px)
                        .fontSize(22.px)
                        .toAttrs {
                            markdownParagraph(it)
                        }
                )
            }
        }
    }
}

@Composable
fun SubServices(subService: Service, leftAligned: Boolean) {
    val breakpoint = rememberBreakpoint()

    Box(
        Modifier
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
                .scrollMargin(top = Constants.HEADER_HEIGHT.px)
                .display(DisplayStyle.Flex)
                .flexDirection(if (breakpoint > Breakpoint.MD) FlexDirection.Row else FlexDirection.Column)
                .alignItems(AlignItems.Center)
                .padding(topBottom = 50.px)
                .gap(if (breakpoint <= Breakpoint.MD) 40.px else 100.px)
        ) {
            Image(
                src = subService.imageUrl,
                modifier = Modifier
                    .width(275.px)
                    .maxWidth(250.px)
                    .order(if (breakpoint > Breakpoint.MD && leftAligned) 0 else 1)
                    .thenIf(breakpoint <= Breakpoint.MD) {
                        Modifier.fillMaxWidth(90.percent)
                    }
            )

            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.px)
            ) {
                SpanText(
                    text = subService.title,
                    modifier = Modifier
                        .fontSize(32.px)
                        .fontWeight(FontWeight.Bold)
                )

                P(
                    attrs = Modifier
                        .fontSize(18.px)
                        .toAttrs {
                            markdownParagraph(subService.description)
                        }
                )

                A(
                    // Keeping it like this for the possible support of multiple tags per sub-service
                    href = Navigation.Screen.Projects.getByTagQuery(listOfNotNull(subService.tag)),
                    attrs = Modifier
                        .fillMaxSize()
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
