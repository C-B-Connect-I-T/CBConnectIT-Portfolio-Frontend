package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.ContactForm
import cbconnectit.portfolio.web.components.SectionTitle
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Constants
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px

@Composable
fun ContactSection() {
    val colorMode by ColorMode.currentState

    Column(
        modifier = Modifier
            .id(Navigation.Screen.Home.ContactSection.id)
            .scrollMargin(Constants.HEADER_HEIGHT.px)
            .backgroundColor(colorMode.toColorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(topBottom = 24.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        SectionTitle(
            modifier = Modifier.fillMaxWidth(),
            section = Navigation.Screen.Home.AboutSection,
            alignment = Alignment.CenterHorizontally
        )

        ContactForm()
    }
}
