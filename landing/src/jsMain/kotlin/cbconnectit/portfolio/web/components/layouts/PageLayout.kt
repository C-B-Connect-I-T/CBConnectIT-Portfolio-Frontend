package cbconnectit.portfolio.web.components.layouts

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.BackToTopButton
import cbconnectit.portfolio.web.components.Header
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.pages.sections.FooterSection
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.format
import cbconnectit.portfolio.web.utils.logoImage
import com.materialdesignsystem.components.DsPageLayout
import com.materialdesignsystem.components.sections.NavigationItem
import com.materialdesignsystem.components.sections.OverflowMenu
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px

@Composable
fun PageLayout(
    modifier: Modifier = Modifier,
    title: String,
    showMenuItems: Boolean = true,
    content: @Composable () -> Unit
) {
    var overflowMenuOpened by remember { mutableStateOf(false) }

    DsPageLayout(
        modifier = modifier,
        title = Res.String.DocumentTitle.format(title),
        header = {
            Header(showMenuItems) {
                overflowMenuOpened = true
            }
        },
        overflowMenu = {
            OverflowMenu(
                logoImage = logoImage(ColorMode.current),
                onMenuClosed = { overflowMenuOpened = false }
            ) { closeMenu ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.px)
                ) {
                    val list = listOf(
                        Navigation.Screen.Home.HomeSection,
                        Navigation.Screen.Home.AboutSection,
                        Navigation.Screen.Home.ServiceSection,
                        Navigation.Screen.Home.PortfolioSection,
                        Navigation.Screen.Home.ExperienceSection,
                        Navigation.Screen.Home.ContactSection,
                    )

                    list.forEach { item ->
                        NavigationItem(
                            href = item.path,
                            title = item.title
                        ) { closeMenu() }
                    }
                }
            }
        },
        overflowMenuOpened = overflowMenuOpened,
        footer = {
            FooterSection(showMenuItems)
        },
        content = {
            content()

            BackToTopButton()
        }
    )
}