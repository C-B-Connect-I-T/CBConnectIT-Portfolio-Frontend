package cbconnectit.portfolio.web.admin.pages.testimonials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.admin.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.components.table.DataTable
import cbconnectit.portfolio.web.components.table.TableColumnDef
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import cbconnectit.portfolio.web.utils.rememberViewModel
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Page("/admin/testimonials")
@Composable
fun AdminTestimonialsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val viewModel = rememberViewModel { TestimonialsViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is TestimonialsContract.Effect.NavigateToManage ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Testimonials.Manage(effect.testimonialId).route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(TestimonialsContract.Intent.LoadTestimonials)
    }

    AdminPageLayout(
        title = "Testimonials",
        fabOnClick = {
            viewModel.sendIntent(TestimonialsContract.Intent.NavigateToManage())
        }
    ) {
        TestimonialsPageContent(state = state, sendIntent = viewModel::sendIntent)
    }
}

@Composable
private fun TestimonialsPageContent(
    state: TestimonialsContract.State,
    sendIntent: (TestimonialsContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(1000.px)
            .padding(leftRight = 24.px, topBottom = 58.px)
    ) {
        TitleLarge(
            modifier = Modifier.padding(bottom = 48.px),
            text = "Testimonials",
            color = ColorMode.current.toColorScheme.onBackground
        )

        DataTable(
            items = state.testimonials,
            columns = testimonialsTableColumns,
            emptyMessage = when {
                state.isLoading -> "Loading testimonials..."
                else -> "No testimonials found yet."
            },
            onRowClick = { testimonial ->
                sendIntent(TestimonialsContract.Intent.NavigateToManage(testimonial.id))
            }
        )
    }
}

private val testimonialsTableColumns = listOf<TableColumnDef<Testimonial>>(
    TableColumnDef(
        heading = "Full Name",
        comparator = compareBy { it.fullName.lowercase() }
    ) { testimonial ->
        Text(testimonial.fullName)
    },
    TableColumnDef(
        heading = "Company",
        comparator = compareBy { it.company?.name?.lowercase().orEmpty() }
    ) { testimonial ->
        Text(testimonial.company?.name ?: "-")
    },
    TableColumnDef(
        heading = "Job Position",
        comparator = compareBy { it.jobPosition.name.lowercase() }
    ) { testimonial ->
        Text(testimonial.jobPosition.name)
    },
    TableColumnDef(
        heading = "Review",
        comparator = compareBy { it.review.lowercase() }
    ) { testimonial ->
        Text(reviewPreview(testimonial.review))
    }
)

// Remove white spaces by using 'trim', the regex removes any white spaces throughout the text itself by replacing them ("\t" or "\n") by a regular space.
private fun reviewPreview(review: String, maxLength: Int = 90): String {
    val normalized = review.trim().replace(Regex("\\s+"), " ")
    if (normalized.length <= maxLength) return normalized
    return normalized.take(maxLength - 3).trimEnd() + "..."
}
