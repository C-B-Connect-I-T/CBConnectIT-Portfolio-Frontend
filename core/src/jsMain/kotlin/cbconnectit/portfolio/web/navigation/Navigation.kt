package cbconnectit.portfolio.web.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cbconnectit.portfolio.web.data.TokenManager
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.data.repos.AuthRepo
import cbconnectit.portfolio.web.utils.Identifiers
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.encodeReturnUrl
import cbconnectit.portfolio.web.utils.joinToStringIndexed
import com.materialkobweb.components.widgets.DsSpinner
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.UpdateHistoryMode
import com.varabyte.kobweb.navigation.open
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

sealed class Navigation(val route: String) {
    sealed class Screen(route: String) : Navigation(route) {
        data object Home : Screen("/") {
            val HomeSection = SectionItem("home", Res.String.Home, "", "#home")
            val AboutSection = SectionItem("about", Res.String.AboutMe, Res.String.AboutMeSubtitle, "#about")
            val ServiceSection = SectionItem("service", Res.String.Service, Res.String.ServiceSubtitle, "#service")
            val PortfolioSection = SectionItem("portfolio", Res.String.Portfolio, Res.String.PortfolioSubtitle, "#portfolio")
            val ExperienceSection = SectionItem("experience", Res.String.Experience, Res.String.ExperienceSubtitle, "#experience")
            val ContactSection = SectionItem("contact", Res.String.ContactMe, Res.String.ContactMeSubtitle, "#contact")
            val TestimonialSection = SectionItem("testimonial", Res.String.Testimonial, Res.String.TestimonialsSubtitle, "#testimonial")
            val AchievementsSection = SectionItem("achievements", Res.String.Achievements, Res.String.AchievementsSubtitle, "#achievements")
        }

        data object Services : Screen("/services") {
            fun getService(id: String) = "$route/service/?serviceId=$id"
        }

        data object Projects : Screen("/projects") {
            fun getByTagQuery(tags: List<Tag>) = "$route/?${tags.joinToStringIndexed("&") { index, tag -> "${Identifiers.PathParams.Tag}$index=${tag.id}" }}"
            fun getProject(id: String) = "$route/?projectId=$id"
        }

        sealed class Admin(route: String) : Screen("/admin$route") {
            data object Home : Admin("")

            sealed class Companies(route: String) : Admin("/companies$route") {
                data object Index : Companies("")
                data class Manage(val id: String? = null) : Companies("/manage" + if (id != null) "?id=$id" else "")
            }

            sealed class Tags(route: String) : Admin("/tags$route") {
                data object Index : Tags("")
                data class Manage(val id: String? = null) : Tags("/manage" + if (id != null) "?id=$id" else "")
            }

            sealed class JobPositions(route: String) : Admin("/job-positions$route") {
                data object Index : JobPositions("")
                data class Manage(val id: String? = null) : JobPositions("/manage" + if (id != null) "?id=$id" else "")
            }

            sealed class Experiences(route: String) : Admin("/experiences$route") {
                data object Index : Experiences("")
                data class Manage(val id: String? = null) : Experiences("/manage" + if (id != null) "?id=$id" else "")
            }

            data object Testimonials : Admin("/testimonials")
            data object Services : Admin("/services")
            data object Projects : Admin("/projects")
            data object Settings : Admin("/settings")
            data class Login(val returnTo: String? = null) :
                Admin("/login" + if (returnTo != null) "?returnTo=${encodeReturnUrl(returnTo)}" else "")
        }
    }

    sealed class External(route: String) : Navigation(route) {
        data object LinkedIn : Navigation("https://www.linkedin.com/in/christiano-bolla/")
        data object Github : Navigation("https://github.com/ShaHar91")
    }
}

data class SectionItem(val id: String, val title: String, val subtitle: String, val path: String)

private val logoutScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

fun logout(doNavigation: Boolean = true) = logoutScope.launch {
    // Navigate first so the authenticated guard is no longer active when auth state clears,
    // preventing it from racing to navigate to the login page.
    if (doNavigation) {
        window.open(Navigation.Screen.Home.route, OpenLinkStrategy.IN_PLACE)
    }

    // Call backend to clear HTTP-only cookies (auth state changes after navigation)
    AuthRepo.logout()
}

// Guard to protect routes that should only really be accessible to un-authenticated users
// e.g. a logged in user can't really benefit of the login and register pages, so they should be redirected to the home page if they try to access those routes while being logged in
@Composable
fun unauthenticatedGuard(content: @Composable () -> Unit) {
    val authStatus = TokenManager.authStatus.collectAsState()
    val isAuthenticated = authStatus.value?.authenticated

    when (isAuthenticated) {
        true -> {
            // If the user is already logged in, redirect to the home page
            val pageContext = rememberPageContext()
            LaunchedEffect(Unit) {
                pageContext.router.navigateTo(
                    pathQueryAndFragment = Navigation.Screen.Admin.Home.route,
                    updateHistoryMode = UpdateHistoryMode.REPLACE
                )
            }
        }

        null -> {
            // Still loading, show a spinner or something
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DsSpinner()
            }
        }

        else -> content()
    }
}

// Guard to protect routes that require authentication
// If user is not logged in, redirect to login page with returnTo parameter
@Composable
fun authenticatedGuard(content: @Composable () -> Unit) {
    val authStatus = TokenManager.authStatus.collectAsState()
    val isAuthenticated = authStatus.value?.authenticated

    val pageContext = rememberPageContext()

    when (isAuthenticated) {
        false -> {
            // User is not authenticated, redirect to login with return URL
            LaunchedEffect(Unit) {
                pageContext.router.navigateTo(
                    pathQueryAndFragment = Navigation.Screen.Admin.Login(
                        returnTo = pageContext.route.pathQueryAndFragment
                    ).route,
                    updateHistoryMode = UpdateHistoryMode.REPLACE
                )
            }
        }

        null -> {
            // Still loading, show a spinner
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DsSpinner()
            }
        }

        else -> content()
    }
}
