package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.Backdrop
import cbconnectit.portfolio.web.components.SocialBar
import cbconnectit.portfolio.web.data.models.domain.Link
import cbconnectit.portfolio.web.models.enums.Social
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Constants.SECTION_WIDTH
import cbconnectit.portfolio.web.utils.Res
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.DsBorderRadius
import com.materialdesignsystem.components.widgets.FilledButton
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.filter
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun MainSection(
    onLetsChatClicked: () -> Unit
) {
    val breakpoint = rememberBreakpoint()

    Box(
        modifier = Modifier
            .id(Navigation.Screen.Home.HomeSection.id)
            .scrollMargin(80.px)
            .fillMaxWidth()
            .maxWidth(SECTION_WIDTH.px),
        contentAlignment = Alignment.TopCenter
    ) {
        // SimpleGrid will automatically use a column (horizontal) for bigger devices, or a row (vertical) for smaller devices
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(
                if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent
            ),
            numColumns = numColumns(base = 1, md = 2)
        ) {
            MainText(onLetsChatClicked)
            MainImage()
        }
    }
}

@Composable
private fun MainText(
    onLetsChatClicked: () -> Unit
) {
    val breakpoint = rememberBreakpoint()

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialBar(
            links = Social.entries.map { Link(id = it.name, type = it.type, url = it.link, createdAt = "", updatedAt = "") },
            itemGap = 20.px
        )

        Spacer(Modifier.width(50.px))

        Column {
            if (breakpoint > Breakpoint.SM) {
                Spacer(Modifier.height(50.px))
            }

            P(
                attrs = Modifier
                    .margin(top = 0.px, bottom = 0.px)
                    .fontSize(if (breakpoint >= Breakpoint.LG) 36.px else 24.px)
                    .fontWeight(FontWeight.Normal)
                    .toAttrs()
            ) {
                Text(Res.String.IntroductionHello)
            }

            P(
                attrs = Modifier
                    .margin(top = 0.px, bottom = 0.px)
                    .fontSize(if (breakpoint >= Breakpoint.LG) 56.px else 36.px)
                    .color(ColorMode.current.toColorScheme.primary)
                    .fontWeight(FontWeight.Bolder)
                    .toAttrs()
            ) {
                Text(Res.String.IntroductionName)
            }

            P(
                attrs = Modifier
                    .margin(top = 0.px, bottom = 5.px)
                    .fontSize(22.px)
                    .fontWeight(FontWeight.Bold)
                    .toAttrs()
            ) {
                Text(Res.String.IntroductionFunction)
            }

            P(
                attrs = Modifier
                    .fillMaxWidth()
                    .margin(top = 0.px, bottom = 0.px)
                    .fontWeight(FontWeight.Normal)
                    .toAttrs()
            ) {
                Text(Res.String.IntroductionBody)
            }

            Spacer(Modifier.height(40.px))

            FilledButton(
                borderRadius = DsBorderRadius(5.px),
                onClick = onLetsChatClicked
            ) {
                Text(Res.String.LetsChat)
            }
        }
    }
}

@Composable
private fun MainImage() {
    val breakpoint = rememberBreakpoint()

    Box(
        modifier = Modifier.fillMaxSize(100.percent),
        contentAlignment = if (breakpoint < Breakpoint.MD) Alignment.BottomCenter else Alignment.BottomEnd,
    ) {
        val colorMode by ColorMode.currentState

        Backdrop(
            colorMode, modifier = Modifier
                .fillMaxWidth(90.percent)
                .fillMaxHeight(85.percent)
                .maxWidth(if (breakpoint < Breakpoint.MD) 345.px else 400.px)
        )

        Image(
            modifier = MainImageStyle.toModifier()
                .fillMaxWidth(90.percent)
                .borderRadius(8.px)
                .maxWidth(if (breakpoint < Breakpoint.MD) 345.px else 400.px),
            src = Res.Image.mainImage,
            alt = "Main Image"
        )
    }
}

@OptIn(ExperimentalComposeWebApi::class)
val MainImageStyle = CssStyle {
    base {
        Modifier
            .styleModifier {
                filter { grayscale(100.percent) }
            }
            .transition(Transition.of(property = "filter", duration = 200.ms))
    }

    hover {
        Modifier
            .styleModifier {
                filter { grayscale(0.percent) }
            }
    }
}