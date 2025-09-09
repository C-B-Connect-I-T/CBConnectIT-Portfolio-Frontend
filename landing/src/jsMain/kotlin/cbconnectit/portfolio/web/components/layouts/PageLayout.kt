package cbconnectit.portfolio.web.components.layouts

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.BackToTopButton
import cbconnectit.portfolio.web.components.Header
import cbconnectit.portfolio.web.components.OverlowMenu
import cbconnectit.portfolio.web.sections.FooterSection
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.format
import com.materialdesignsystem.components.DsPageLayout
import com.varabyte.kobweb.compose.ui.Modifier

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
            OverlowMenu { overflowMenuOpened = false }
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