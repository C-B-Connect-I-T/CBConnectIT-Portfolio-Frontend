package cbconnectit.portfolio.web.admin.pages.projects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.admin.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.components.table.DataTable
import cbconnectit.portfolio.web.components.table.TableColumnDef
import cbconnectit.portfolio.web.data.models.domain.Project
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

@Page("/admin/projects")
@Composable
fun AdminProjectsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val viewModel = rememberViewModel { ProjectsViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProjectsContract.Effect.NavigateToManage ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Projects.Manage(effect.projectId).route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ProjectsContract.Intent.LoadProjects)
    }

    AdminPageLayout(
        title = "Projects",
        fabOnClick = {
            viewModel.sendIntent(ProjectsContract.Intent.NavigateToManage())
        }
    ) {
        ProjectsPageContent(state = state, sendIntent = viewModel::sendIntent)
    }
}

@Composable
private fun ProjectsPageContent(
    state: ProjectsContract.State,
    sendIntent: (ProjectsContract.Intent) -> Unit
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
            text = "Projects",
            color = ColorMode.current.toColorScheme.onBackground
        )

        DataTable(
            items = state.projects,
            columns = projectsTableColumns,
            emptyMessage = when {
                state.isLoading -> "Loading projects..."
                else -> "No projects found yet."
            },
            onRowClick = { project ->
                sendIntent(ProjectsContract.Intent.NavigateToManage(project.id))
            }
        )
    }
}

private val projectsTableColumns: List<TableColumnDef<Project>> = listOf(
    TableColumnDef(
        heading = "Title",
        comparator = compareBy { it.title.lowercase() }
    ) { project ->
        Text(project.title)
    },
    TableColumnDef(
        heading = "Tags",
        comparator = compareBy { it.tags.size }
    ) { project ->
        val count = project.tags.size
        Text("$count ${if (count == 1) "tag" else "tags"}")
    },
    TableColumnDef(
        heading = "Links",
        comparator = compareBy { it.links.size }
    ) { project ->
        val count = project.links.size
        Text("$count ${if (count == 1) "link" else "links"}")
    },
    TableColumnDef(
        heading = "Updated At",
        comparator = compareByDescending { it.updatedAt }
    ) { project ->
        Text(project.updatedAt.substringBefore("T").ifBlank { project.updatedAt })
    }
)
