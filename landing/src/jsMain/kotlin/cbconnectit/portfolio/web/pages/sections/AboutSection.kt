package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.HighLightCard
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.svg.completedProjectsSvg
import cbconnectit.portfolio.web.svg.experienceSvg
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.format
import com.materialdesignsystem.components.widgets.DsMaterialSymbols
import com.materialdesignsystem.components.widgets.MaterialSymbolType
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun AboutSection(yearsExperience: Int) {
    val colorMode by ColorMode.currentState
    val colorScheme = colorMode.toColorScheme

    Column(
        modifier = Modifier
            .id(Navigation.Screen.Home.AboutSection.id)
            .scrollMargin(Constants.HEADER_HEIGHT.px)
            .fillMaxWidth()
            .color(colorScheme.onSecondaryContainer)
            .backgroundColor(colorScheme.secondaryContainer)
            .padding(topBottom = 32.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(36.px)
    ) {
        SectionTitle(
            modifier = Modifier.fillMaxWidth(),
            section = Navigation.Screen.Home.AboutSection,
            alignment = Alignment.CenterHorizontally
        )

        SpanText(
            text = Res.String.AboutContent,
            modifier = Modifier
                .fillMaxWidth(80.percent)
                .maxWidth(780.px)
                .textAlign(TextAlign.Center)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.px)
        ) {
            HighLightCard(Res.String.Experience, Res.String.ExperienceInYears.format(yearsExperience)) {
                DsMaterialSymbols(
                    color = colorScheme.onSurface,
                    fill = true,
                    icon = "work_history"
                )
            }

            HighLightCard(Res.String.Completed, Res.String.CompletedProjects) {
                DsMaterialSymbols(
                    color = colorScheme.onSurface,
                    fill = true,
                    icon = "fact_check"
                )
            }
        }
    }
}
