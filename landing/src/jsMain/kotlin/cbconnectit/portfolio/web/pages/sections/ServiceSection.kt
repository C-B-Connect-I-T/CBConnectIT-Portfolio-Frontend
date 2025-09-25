package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.components.MainServiceCard
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Res
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.VerticalAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.cssRule
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val serviceSectionStyle = CssStyle {
    base {
        // column
        Modifier
            .display(DisplayStyle.Flex)
            .fillMaxWidth(80.percent)
            .verticalAlign(VerticalAlign.Middle)
            .flexDirection(FlexDirection.Column)
    }

    Breakpoint.MD {
        // row
        Modifier
            .fillMaxWidth(90.percent)
            .flexDirection(FlexDirection.Row)
    }

    cssRule("#service-image") {
        Modifier
            .order(1)
            .margin(top = 50.px, right = 0.px)
            .width(60.percent)
    }

    cssRule(Breakpoint.MD, "#service-image") {
        Modifier
            .order(-1)
            .margin(top = 0.px, right = 100.px)
            .width(30.percent)
    }
}

@Composable
fun ServiceSection(
    services: List<Service>
) {
    Box(
        modifier = serviceSectionStyle.toModifier()
            .id(Navigation.Screen.Home.ServiceSection.id)
            .scrollMargin(Constants.HEADER_HEIGHT.px)
            .maxWidth(Constants.SECTION_WIDTH.px),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .id("service-image")
                .borderRadius(40.px)
                .aspectRatio(18, 27)
                .objectFit(ObjectFit.Cover),
            src = Res.Image.services,
            alt = "Services Image"
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(36.px, Alignment.CenterVertically),
        ) {
            SectionTitle(
                modifier = Modifier.fillMaxWidth(),
                section = Navigation.Screen.Home.ServiceSection,
                alignment = Alignment.Start,
                href = Navigation.Screen.Services.route,
                showSeeAllButton = true
            )

            SimpleGrid(
                modifier = Modifier.gap(24.px),
                numColumns = numColumns(base = 1, md = 2)
            ) {
                services.forEach {
                    MainServiceCard(service = it)
                }
            }
        }
    }
}
