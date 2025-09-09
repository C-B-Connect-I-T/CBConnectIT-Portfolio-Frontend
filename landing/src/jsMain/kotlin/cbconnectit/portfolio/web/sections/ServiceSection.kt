package cbconnectit.portfolio.web.sections

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.MainServiceCard
import cbconnectit.portfolio.web.components.SectionTitle
import com.materialdesignsystem.components.Spacer
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.data.repos.ServiceRepo
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Config
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Res
import com.varabyte.kobweb.compose.css.JustifyContent
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.*

@Composable
fun ServiceSection() {
    Box(
        modifier = Modifier
            .id(Navigation.Screen.Home.ServiceSection.id)
            .scrollMargin(80.px)
            .fillMaxWidth()
            .maxWidth(Constants.SECTION_WIDTH.px),
        contentAlignment = Alignment.Center
    ) {
        ServiceContent()
    }
}

@Composable
fun ServiceContent() {
    val breakpoint = rememberBreakpoint()
    val pageContext = rememberPageContext()
    var services by remember { mutableStateOf<List<Service>>(emptyList()) }

    LaunchedEffect(Unit) {
        services = ServiceRepo.getServices(Config.baseUrl)
    }

    @Composable
    fun ContentWrapper(content: @Composable () -> Unit) {
        if (breakpoint >= Breakpoint.MD) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(90.percent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }

    ContentWrapper {
        if (breakpoint >= Breakpoint.MD) {
            ServiceImage(breakpoint)

            Spacer(
                Modifier.width(
                    if (breakpoint > Breakpoint.MD) 100.px else 50.px
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            SectionTitle(
                modifier = Modifier.fillMaxWidth(),
                section = Navigation.Screen.Home.ServiceSection,
                alignment = Alignment.Start,
                href = Navigation.Screen.Services.route,
                showSeeAllButton = true
            )

            Spacer(Modifier.height(36.px))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .display(DisplayStyle.Flex)
                    .flexDirection(FlexDirection.Row)
                    .flexWrap(FlexWrap.Wrap)
                    .justifyContent(JustifyContent.SpaceAround)
            ) {
                services.forEach {
                    MainServiceCard(
                        modifier = Modifier.fillMaxWidth(if (breakpoint >= Breakpoint.MD) 50.percent else 100.percent),
                        service = it
                    )
                }
            }
        }

        if (breakpoint < Breakpoint.MD) {
            Spacer(Modifier.height(50.px))

            ServiceImage(breakpoint)
        }
    }
}

@Composable
fun ServiceImage(breakpoint: Breakpoint) {
    Image(
        modifier = Modifier
            .borderRadius(40.px)
            .width(if (breakpoint >= Breakpoint.MD) 30.percent else 60.percent)
            .aspectRatio(18, 27)
            .objectFit(ObjectFit.Cover),
        src = Res.Image.services,
        alt = "Services Image"
    )
}