package cbconnectit.portfolio.web.pages.sections

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.components.SocialBar
import com.materialdesignsystem.components.Spacer
import cbconnectit.portfolio.web.data.models.domain.Link
import cbconnectit.portfolio.web.models.enums.Social
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.logoImage
import com.materialdesignsystem.components.sections.NavigationItem
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px

@Composable
fun FooterSection(showMenu: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(topBottom = 32.px)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        FooterContent(showMenu)
    }
}

@Composable
fun FooterContent(showMenu: Boolean) {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier.fillMaxWidth(/*if (breakpoint >= Breakpoint.MD) 100.percent else 90.percent*/),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.width(110.px),
            src = logoImage(ColorMode.current),
            alt = "Logo Image"
        )

        Spacer(Modifier.height(25.px))

        if (showMenu) {
            // simple grid can't be used since it only allows up to 5 items
            if (breakpoint > Breakpoint.SM) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.px, Alignment.CenterHorizontally),
                ) {
                    FooterMenu()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.px)
                ) {
                    FooterMenu()
                }
            }
        }

        Spacer(Modifier.height(25.px))

        SocialBar(
            row = true,
            links = Social.entries.map { Link(id = it.name, type = it.type, url = it.link, createdAt = "", updatedAt = "") },
            itemGap = 20.px
        )
    }
}

@Composable
fun FooterMenu() {
    val linkStyle = Modifier.fontSize(12.px)

    NavigationItem(
        modifier = linkStyle,
        href = Navigation.Screen.Home.HomeSection.path,
        title = Navigation.Screen.Home.HomeSection.title
    )

    NavigationItem(
        modifier = linkStyle,
        href = Navigation.Screen.Home.AboutSection.path,
        title = Navigation.Screen.Home.AboutSection.title
    )

    NavigationItem(
        modifier = linkStyle,
        href = Navigation.Screen.Home.ServiceSection.path,
        title = Navigation.Screen.Home.ServiceSection.title
    )

    NavigationItem(
        modifier = linkStyle,
        href = Navigation.Screen.Home.PortfolioSection.path,
        title = Navigation.Screen.Home.PortfolioSection.title
    )

    NavigationItem(
        modifier = linkStyle,
        href = Navigation.Screen.Home.ExperienceSection.path,
        title = Navigation.Screen.Home.ExperienceSection.title
    )

    NavigationItem(
        modifier = linkStyle,
        href = Navigation.Screen.Home.ContactSection.path,
        title = Navigation.Screen.Home.ContactSection.title
    )
}