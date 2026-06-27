package cbconnectit.portfolio.web.admin.pages.jobpositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.admin.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.components.table.DataTable
import cbconnectit.portfolio.web.components.table.TableColumnDef
import cbconnectit.portfolio.web.data.models.domain.JobPosition
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

@Page("/admin/job-positions")
@Composable
fun AdminJobPositionsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val viewModel = rememberViewModel { JobPositionsViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is JobPositionsContract.Effect.NavigateToManage ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.JobPositions.Manage(effect.jobPositionId).route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(JobPositionsContract.Intent.LoadJobPositions)
    }

    AdminPageLayout(
        title = "Job Positions",
        fabOnClick = {
            viewModel.sendIntent(JobPositionsContract.Intent.NavigateToManage())
        }
    ) {
        JobPositionsPageContent(state = state, sendIntent = viewModel::sendIntent)
    }
}

@Composable
private fun JobPositionsPageContent(
    state: JobPositionsContract.State,
    sendIntent: (JobPositionsContract.Intent) -> Unit
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
            text = "Job Positions",
            color = ColorMode.current.toColorScheme.onBackground
        )

        DataTable(
            items = state.jobPositions,
            columns = jobPositionsTableColumns,
            emptyMessage = when {
                state.isLoading -> "Loading job positions..."
                else -> "No job positions found yet."
            },
            onRowClick = { jobPosition ->
                sendIntent(JobPositionsContract.Intent.NavigateToManage(jobPosition.id))
            }
        )
    }
}

private val jobPositionsTableColumns = listOf<TableColumnDef<JobPosition>>(
    TableColumnDef(
        heading = "Name",
        comparator = compareBy { it.name.lowercase() }
    ) { jobPosition ->
        Text(jobPosition.name)
    }
)
