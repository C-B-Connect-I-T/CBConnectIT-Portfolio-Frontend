package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.components.ExperienceCard
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Constants
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.scrollMargin
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun ExperienceSection(experiences: List<Experience>) {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .id(Navigation.Screen.Home.ExperienceSection.id)
            .scrollMargin(Constants.HEADER_HEIGHT.px)
            .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(Constants.SECTION_WIDTH.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.px)
    ) {
        SectionTitle(
            modifier = Modifier.fillMaxWidth(),
            section = Navigation.Screen.Home.ExperienceSection,
            alignment = Alignment.CenterHorizontally,
            href = null
        )

        Column {
            experiences.forEachIndexed { index, experience ->
                ExperienceCard(index == 0, experience)
            }
        }
    }
}
