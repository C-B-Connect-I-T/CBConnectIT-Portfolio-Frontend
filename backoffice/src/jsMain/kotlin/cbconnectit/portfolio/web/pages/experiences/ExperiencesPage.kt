package cbconnectit.portfolio.web.pages.experiences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.components.table.DataTable
import cbconnectit.portfolio.web.components.table.TableColumnDef
import cbconnectit.portfolio.web.data.models.domain.Experience
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

@Page("/admin/experiences")
@Composable
fun AdminExperiencesPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val viewModel = rememberViewModel { ExperiencesViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ExperiencesContract.Effect.NavigateToManage ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Experiences.Manage(effect.experienceId).route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ExperiencesContract.Intent.LoadExperiences)
    }

    AdminPageLayout(
        title = "Experiences",
        fabOnClick = {
            viewModel.sendIntent(ExperiencesContract.Intent.NavigateToManage())
        }
    ) {
        ExperiencesPageContent(state = state, sendIntent = viewModel::sendIntent)
    }
}

@Composable
private fun ExperiencesPageContent(
    state: ExperiencesContract.State,
    sendIntent: (ExperiencesContract.Intent) -> Unit
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
            text = "Experiences",
            color = ColorMode.current.toColorScheme.onBackground
        )

        DataTable(
            items = state.experiences,
            columns = experiencesTableColumns,
            emptyMessage = when {
                state.isLoading -> "Loading experiences..."
                else -> "No experiences found yet."
            },
            onRowClick = { experience ->
                sendIntent(ExperiencesContract.Intent.NavigateToManage(experience.id))
            }
        )
    }
}

private val experiencesTableColumns = listOf<TableColumnDef<Experience>>(
    TableColumnDef(
        heading = "Short Description",
        comparator = compareBy { it.shortDescription.lowercase() }
    ) { experience ->
        Text(experience.shortDescription)
    },
    TableColumnDef(
        heading = "Company",
        comparator = compareBy { it.company.name.lowercase() }
    ) { experience ->
        Text(experience.company.name)
    },
    TableColumnDef(
        heading = "Job Position",
        comparator = compareBy { it.formattedJobPosition.lowercase() }
    ) { experience ->
        Text(experience.formattedJobPosition)
    },
    TableColumnDef(
        heading = "Period",
        comparator = compareBy { it.from }
    ) { experience ->
        Text(experience.formattedDate)
    }
)
