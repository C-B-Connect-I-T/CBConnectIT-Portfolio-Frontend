package cbconnectit.portfolio.web.pages

import androidx.compose.runtime.Composable
import com.materialdesignsystem.components.Spacer
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.sections.*
import cbconnectit.portfolio.web.utils.Res
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun HomePage() {
    val breakpoint = rememberBreakpoint()

    val spacerHeight = when {
        breakpoint > Breakpoint.MD -> 100.px
        breakpoint == Breakpoint.MD -> 80.px
        else -> 50.px
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PageLayout(
            title = Res.String.Home,
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(if (breakpoint < Breakpoint.MD) 56.px else 12.px))

                MainSection()

                Spacer(Modifier.height(spacerHeight))

                AboutSection()

                Spacer(Modifier.height(spacerHeight))

                ServiceSection()

                Spacer(Modifier.height(spacerHeight))

                PortfolioSection()

                Spacer(Modifier.height(spacerHeight))

                TestimonialSection()// TODO: Makes the page wider on a smaller screen size (my phone landscape)

                Spacer(Modifier.height(spacerHeight))

//                AchievementsContent()
//
//                Spacer(Modifier.height(spacerHeight))

                ExperienceSection()

                Spacer(Modifier.height(spacerHeight))

                ContactSection()

                Spacer(Modifier.height(52.px))
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
