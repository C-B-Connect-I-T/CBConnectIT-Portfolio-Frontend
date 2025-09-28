package cbconnectit.portfolio.web.pages

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.pages.sections.*
import cbconnectit.portfolio.web.utils.Res
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.css.px

@Page("/index")
@Composable
fun HomePage() {
    val ctx = rememberPageContext()
    val viewModel = remember { HomeViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                HomeContract.Effect.NavigateToContactSection -> ctx.router.navigateTo(Navigation.Screen.Home.ContactSection.path)
            }
        }
    }
    HomePageContent(state, viewModel::sendIntent)
}

@Composable
private fun HomePageContent(
    state: HomeContract.State,
    sendIntent: (HomeContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()

    val spacerHeight = when {
        breakpoint > Breakpoint.MD -> 100.px
        breakpoint == Breakpoint.MD -> 80.px
        else -> 50.px
    }

    PageLayout(
        modifier = Modifier.fillMaxSize(),
        title = Res.String.Home,
    ) {
        Column(
            Modifier.fillMaxSize()
                .padding(
                    top = if (breakpoint < Breakpoint.MD) 56.px else 12.px,
                    bottom = 52.px
                ),
            verticalArrangement = Arrangement.spacedBy(spacerHeight, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainSection {
                sendIntent(HomeContract.Intent.LetsChatClicked)
            }
            AboutSection(state.yearsOfExperience)
            ServiceSection(state.services)
            PortfolioSection(state.projects, state.selectedWork) {
                sendIntent(HomeContract.Intent.UpdateSelectedWork(it))
            }
            TestimonialSection(state.testimonials)// TODO: Makes the page wider on a smaller screen size (my phone landscape)
            ExperienceSection(state.experiences)
            ContactSection()
        }
    }
}

