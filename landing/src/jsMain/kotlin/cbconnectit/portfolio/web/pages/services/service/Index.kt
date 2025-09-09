package cbconnectit.portfolio.web.pages.services.service

import androidx.compose.runtime.*
import com.materialdesignsystem.components.Spacer
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.data.repos.ServiceRepo
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.styles.MainButtonStyle
import cbconnectit.portfolio.web.utils.*
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.*
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
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
fun ServicePage() {
    val breakpoint = rememberBreakpoint()
    val ctx = rememberPageContext()
    val hasServiceIdParam = remember(ctx.route) {
        ctx.route.params.containsKey(Identifiers.PathParams.ServiceId)
    }

    var service by remember { mutableStateOf<Service?>(null) }

    LaunchedEffect(Unit) {
        if (hasServiceIdParam) {
            val serviceId = ctx.route.params.getValue(Identifiers.PathParams.ServiceId)
            service = ServiceRepo.getServiceById(Config.baseUrl, serviceId)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PageLayout(
            title = Res.String.SubServicesDocumentTitle.format(service?.title),
            showMenuItems = false,
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ServiceBanner(service, breakpoint)

                val subServices = service?.subServices
                if (subServices.isNullOrEmpty().not()) {
                    Spacer(Modifier.height(68.px))

                    SubServices(subServices!!, breakpoint)
                }

                val extraInfo = service?.extraInfo
                if (extraInfo.isNullOrEmpty().not()) {
                    Spacer(Modifier.height(68.px))

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .maxWidth(Constants.SECTION_WIDTH.px),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        P(
                            attrs = Modifier
                                .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
                                .fontSize(22.px)
                                .fontFamily(Constants.FONT_FAMILY)
                                .toAttrs {
                                    markdownParagraph(extraInfo!!)
                                }

                        )
                    }
                }
            }
        }

//        * var showModal by remember { mutableStateOf(true) }
//        * if (showModal) {
//        *   Overlay(Modifier.onClick { showModal = false }) {
//            *     Dialog {
//            *        // ... your modal content here ...
//            *     }
//            *   }
//        * }
    }
}

@Composable
fun ServiceBanner(service: Service?, breakpoint: Breakpoint) {
    Box(
        Modifier
            .backgroundImage(url(service?.bannerImageUrl ?: ""))
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
            Modifier
                .fillMaxWidth()
                .maxWidth(Constants.SECTION_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                Modifier
                    .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
                    .margin(topBottom = if (breakpoint >= Breakpoint.MD) 108.px else 56.px),
            ) {
                P(
                    Modifier
                        .color(ColorMode.current.toColorScheme.onPrimary)
                        .maxWidth(if (breakpoint > Breakpoint.MD) 65.percent else 85.percent)
                        .margin(topBottom = 0.px)
                        .fontSize(32.px)
                        .fontWeight(FontWeight.Bold)
                        .toAttrs()
                ) {
                    Text(
                        service?.title ?: ""
                    )
                }

                Spacer(Modifier.height(12.px))

                service?.bannerDescription?.let {
                    P(
                        Modifier
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
}

@Composable
fun SubServices(subServices: List<Service>, breakpoint: Breakpoint) {

    subServices.forEachIndexed { index, subService ->
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
                        .scrollMargin(top = 60.px)
                        .display(DisplayStyle.Flex)
                        .flexDirection(if (breakpoint > Breakpoint.MD) FlexDirection.Row else FlexDirection.Column)
                        .alignItems(AlignItems.Center)
                        .padding(topBottom = 50.px, leftRight = 10.percent)
                ) {
                    if (leftAligned && breakpoint > Breakpoint.MD) {
                        Image(subService.imageUrl, Modifier.width(275.px))

                        Spacer(Modifier.width(100.px))
                    }

                    Column(Modifier.fillMaxWidth()) {
                        P(
                            attrs = Modifier
                                .fontSize(32.px)
                                .fontWeight(FontWeight.Bold)
                                .fontFamily(Constants.FONT_FAMILY)
                                .toAttrs()
                        ) {
                            Text(subService.title)
                        }

                        Spacer(Modifier.height(24.px))

                        P(
                            attrs = Modifier
                                .fontSize(18.px)
                                .fontFamily(Constants.FONT_FAMILY)
                                .toAttrs {
                                    markdownParagraph(subService.description)
                                }
                        )

                        Spacer(Modifier.height(24.px))

                        A(
                            // Keeping it like this for the possible support of multiple tags per sub-service
                            href = Navigation.Screen.Projects.getByTagQuery(listOf(subService.tag).joinToStringIndexed("&") { index, tag -> "${Identifiers.PathParams.Tag}$index=${tag?.id}" }),
                            attrs = Modifier
                                .fillMaxSize()
                                .textDecorationLine(TextDecorationLine.None)
                                .toAttrs()
                        ) {
                            Button(
                                modifier = MainButtonStyle.toModifier(),
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
                            subService.imageUrl,
                            Modifier.maxWidth(250.px)
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