package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.ContactForm
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.navigation.Navigation
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px

@Composable
fun ContactSection() {
    val colorMode by ColorMode.currentState

    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .id(Navigation.Screen.Home.ContactSection.id)
            .scrollMargin(80.px)
            .backgroundColor(colorMode.toColorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(topBottom = 24.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(
            modifier = Modifier.fillMaxWidth(),
            section = Navigation.Screen.Home.AboutSection,
            alignment = Alignment.CenterHorizontally
        )

        Spacer(Modifier.height(24.px))

        ContactForm(breakpoint)
    }
}
