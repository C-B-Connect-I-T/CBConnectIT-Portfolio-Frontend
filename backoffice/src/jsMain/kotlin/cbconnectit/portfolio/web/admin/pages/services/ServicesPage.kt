package cbconnectit.portfolio.web.admin.pages.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.admin.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.components.table.DataTable
import cbconnectit.portfolio.web.components.table.TableColumnDef
import cbconnectit.portfolio.web.data.models.domain.ServiceAdmin
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

@Page("/admin/services")
@Composable
fun AdminServicesPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val viewModel = rememberViewModel { ServicesViewModel() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ServicesContract.Effect.NavigateToManage ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Services.Manage(effect.serviceId).route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ServicesContract.Intent.LoadServices)
    }

    AdminPageLayout(
        title = "Services",
        fabOnClick = {
            viewModel.sendIntent(ServicesContract.Intent.NavigateToManage())
        }
    ) {
        ServicesPageContent(state = state, sendIntent = viewModel::sendIntent)
    }
}

@Composable
private fun ServicesPageContent(
    state: ServicesContract.State,
    sendIntent: (ServicesContract.Intent) -> Unit
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
            text = "Services",
            color = ColorMode.current.toColorScheme.onBackground
        )

        DataTable(
            items = state.services,
            columns = servicesTableColumns,
            emptyMessage = when {
                state.isLoading -> "Loading services..."
                else -> "No services found yet."
            },
            onRowClick = { service ->
                sendIntent(ServicesContract.Intent.NavigateToManage(service.id))
            }
        )
    }
}

private val servicesTableColumns: List<TableColumnDef<ServiceAdmin>> = listOf(
    TableColumnDef(
        heading = "Title",
        comparator = compareBy { it.title.lowercase() }
    ) { service ->
        Text(service.title)
    },
    TableColumnDef(
        heading = "Parent Service",
        comparator = compareBy { it.parentService?.title?.lowercase().orEmpty() }
    ) { service ->
        Text(service.parentService?.title ?: "-")
    },
    TableColumnDef(
        heading = "Tag",
        comparator = compareBy { it.tag?.name?.lowercase().orEmpty() }
    ) { service ->
        Text(service.tag?.name ?: "-")
    },
    TableColumnDef(
        heading = "Updated At",
        comparator = compareBy { it.updatedAt }
    ) { service ->
        Text(service.updatedAt.substringBefore("T").ifBlank { service.updatedAt })
    }
)
