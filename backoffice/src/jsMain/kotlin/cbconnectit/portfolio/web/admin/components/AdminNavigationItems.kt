package cbconnectit.portfolio.web.admin.components

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.logout
import com.materialkobweb.components.sections.NavigationItem
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.css.px

@Composable
fun AdminNavigationItems(
    onItemClick: (() -> Unit)? = null
) {
    val context = rememberPageContext()
    val currentPath = context.route.path

    NavigationItem(
        modifier = Modifier.margin(bottom = 75.px),
        selected = currentPath == Navigation.Screen.Admin.Home.route,
        title = "Home",
        icon = "home",
        href = Navigation.Screen.Admin.Home.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 16.px),
        selected = currentPath.contains(Navigation.Screen.Admin.Companies.Index.route),
        title = "Companies",
        icon = "domain",
        href = Navigation.Screen.Admin.Companies.Index.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 16.px),
        selected = currentPath.contains(Navigation.Screen.Admin.JobPositions.Index.route),
        title = "Job Positions",
        icon = "badge",
        href = Navigation.Screen.Admin.JobPositions.Index.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 16.px),
        selected = currentPath.contains(Navigation.Screen.Admin.Tags.Index.route),
        title = "Tags",
        icon = "tag",
        href = Navigation.Screen.Admin.Tags.Index.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 16.px),
        selected = currentPath.contains(Navigation.Screen.Admin.Testimonials.Index.route),
        title = "Testimonials",
        icon = "rate_review",
        href = Navigation.Screen.Admin.Testimonials.Index.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 16.px),
        selected = currentPath.contains(Navigation.Screen.Admin.Services.Index.route),
        title = "Services",
        icon = "work",
        href = Navigation.Screen.Admin.Services.Index.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 16.px),
        selected = currentPath.contains(Navigation.Screen.Admin.Projects.route),
        title = "Projects",
        icon = "folder_open",
        href = Navigation.Screen.Admin.Projects.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 16.px),
        selected = currentPath.contains(Navigation.Screen.Admin.Experiences.Index.route),
        title = "Experiences",
        icon = "work_history",
        href = Navigation.Screen.Admin.Experiences.Index.route
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 75.px),
        title = "Settings",
        selected = currentPath.contains(Navigation.Screen.Admin.Settings.route),
        icon = "settings",
        href = Navigation.Screen.Admin.Settings.route
    )

    NavigationItem(
        title = "Logout",
        icon = "logout",
        href = Navigation.Screen.Home.route,
        onClick = {
            logout()
            onItemClick?.invoke()
        }
    )
}
