package cbconnectit.portfolio.web.sections

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.ExperienceCard
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.components.Spacer
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.data.repos.ExperienceRepo
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Config
import cbconnectit.portfolio.web.utils.Constants
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun ExperienceSection() {
    Box(
        modifier = Modifier
            .id(Navigation.Screen.Home.ExperienceSection.id)
            .scrollMargin(80.px)
            .fillMaxWidth()
            .maxWidth(Constants.SECTION_WIDTH.px),
        contentAlignment = Alignment.Center
    ) {
        ExperienceContent()
    }
}

@Composable
fun ExperienceContent() {
    val breakpoint = rememberBreakpoint()
    var experiences by remember { mutableStateOf(emptyList<Experience>()) }

    LaunchedEffect(Unit) {
        experiences = ExperienceRepo.getExperiences(Config.baseUrl)
    }

    Column(
        modifier = Modifier.fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(
            modifier = Modifier
                .fillMaxWidth(),
            section = Navigation.Screen.Home.ExperienceSection,
            alignment = Alignment.CenterHorizontally,
            href = null // TODO: add navigation
//            showSeeAllButton = true
        )

        Spacer(Modifier.height(25.px))

        Column {
            experiences.forEachIndexed { index, experience ->
                ExperienceCard(breakpoint, index == 0, experience)
            }
        }
    }
}