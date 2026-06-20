package cbconnectit.portfolio.web.pages

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import com.materialkobweb.components.widgets.FilledButton
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text

@Page("/admin/testimonials")
@Composable
fun AdminTestimonialsPage() = authenticatedGuard {
    AdminCrudSkeletonPage(
        title = "Testimonials",
        createActionLabel = "Nieuw testimonial",
        emptyStateText = "Nog geen testimonials toegevoegd."
    )
}

@Page("/admin/services")
@Composable
fun AdminServicesPage() = authenticatedGuard {
    AdminCrudSkeletonPage(
        title = "Services",
        createActionLabel = "Nieuwe service",
        emptyStateText = "Nog geen services toegevoegd."
    )
}

@Page("/admin/projects")
@Composable
fun AdminProjectsPage() = authenticatedGuard {
    AdminCrudSkeletonPage(
        title = "Projects",
        createActionLabel = "Nieuw project",
        emptyStateText = "Nog geen projecten toegevoegd."
    )
}

@Page("/admin/settings")
@Composable
fun AdminSettingsPage() = authenticatedGuard {
    AdminCrudSkeletonPage(
        title = "Settings",
        createActionLabel = "Settings aanpassen?",
        emptyStateText = "Geen settings voor het moment."
    )
}

@Composable
@Deprecated("This is a placeholder page for CRUD operations. Replace with actual implementation.")
fun AdminCrudSkeletonPage(
    title: String,
    createActionLabel: String,
    emptyStateText: String
) {
    val colorScheme = ColorMode.current.toColorScheme

    AdminPageLayout(title = title) {
        Column(
            modifier = Modifier.fillMaxWidth(90.percent),
            verticalArrangement = Arrangement.spacedBy(24.px)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                H1 { Text("$title beheren") }
                FilledButton(onClick = { }) {
                    Text(createActionLabel)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .borderRadius(14.px)
                    .backgroundColor(colorScheme.surface)
                    .padding(20.px),
                verticalArrangement = Arrangement.spacedBy(10.px)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SpanText(text = "Naam", modifier = Modifier.fontWeight(FontWeight.SemiBold))
                    SpanText(text = "Status", modifier = Modifier.fontWeight(FontWeight.SemiBold))
                    SpanText(text = "Acties", modifier = Modifier.fontWeight(FontWeight.SemiBold))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .borderRadius(10.px)
                        .backgroundColor(colorScheme.background)
                        .padding(18.px),
                    contentAlignment = Alignment.Center
                ) {
                    SpanText(text = emptyStateText, modifier = Modifier.color(colorScheme.onSurfaceVariant))
                }
            }
        }
    }
}
