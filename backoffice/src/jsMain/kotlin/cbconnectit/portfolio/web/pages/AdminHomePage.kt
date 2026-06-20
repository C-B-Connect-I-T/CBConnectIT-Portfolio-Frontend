package cbconnectit.portfolio.web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cbconnectit.portfolio.web.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Page("/admin")
@Composable
fun AdminPage() = authenticatedGuard {
    val viewModel = remember { AdminHomeViewModel() }
    val state by viewModel.state.collectAsState()

    AdminPageLayout(title = "Home") {
        AdminPageContent(state = state)
    }
}

@Composable
fun AdminPageContent(
    state: AdminHomeContract.State
) {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .padding(top = if (breakpoint >= Breakpoint.MD) 24.px else 12.px),
        verticalArrangement = Arrangement.spacedBy(30.px)
    ) {
        val greeting = if (state.isLoading) {
            "Welkom terug"
        } else {
            "Welkom terug, ${state.currentUser?.fullName ?: "Admin"}"
        }

        H1 { Text("Dashboard") }
        P { Text(greeting) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.px)
        ) {
            DashboardCard(
                modifier = Modifier.fillMaxWidth(50.percent),
                title = "Testimonials",
                subtitle = "Beheer testimonials en publicatie status.",
                path = Navigation.Screen.Admin.Testimonials.route
            )
            DashboardCard(
                modifier = Modifier.fillMaxWidth(50.percent),
                title = "Services",
                subtitle = "Voeg services toe, werk bij of verwijder ze.",
                path = Navigation.Screen.Admin.Services.route
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.px)
        ) {
            DashboardCard(
                modifier = Modifier.fillMaxWidth(50.percent),
                title = "Projects",
                subtitle = "Onderhoud portfolio projecten en metadata.",
                path = Navigation.Screen.Admin.Projects.route
            )
            DashboardCard(
                modifier = Modifier.fillMaxWidth(50.percent),
                title = "Experiences",
                subtitle = "Beheer ervaring-items in de tijdslijn.",
                path = Navigation.Screen.Admin.Experiences.Index.route
            )
        }
    }
}

@Composable
private fun DashboardCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    path: String
) {
    val colorScheme = ColorMode.current.toColorScheme

    Link(
        modifier = modifier,
        variant = UndecoratedLinkVariant,
        path = path,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .borderRadius(14.px)
                .backgroundColor(colorScheme.surfaceContainer)
                .padding(18.px)
                .cursor(Cursor.Pointer)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.px)) {
                H3 {
                    Text(title)
                }
                SpanText(
                    text = subtitle,
                    modifier = Modifier.color(colorScheme.onSurface)
                )
            }
        }
    }
}
