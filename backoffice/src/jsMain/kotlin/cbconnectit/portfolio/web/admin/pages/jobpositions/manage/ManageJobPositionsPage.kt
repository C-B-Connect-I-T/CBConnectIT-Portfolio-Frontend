package cbconnectit.portfolio.web.admin.pages.jobpositions.manage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.DestructiveFilledButton
import cbconnectit.portfolio.web.components.DestructiveOutlinedButton
import cbconnectit.portfolio.web.components.Dialog
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.admin.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import cbconnectit.portfolio.web.utils.rememberViewModel
import com.materialkobweb.components.Spacer
import com.materialkobweb.components.widgets.DsBorderRadius
import com.materialkobweb.components.widgets.DsEditableField
import com.materialkobweb.components.widgets.DsSpinner
import com.materialkobweb.components.widgets.FilledButton
import com.materialkobweb.components.widgets.OutlinedButton
import com.materialkobweb.components.widgets.SpinnerSize
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Page("/admin/job-positions/manage")
@Composable
fun AdminManageJobPositionsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val jobPositionId = ctx.route.params["id"]
    val viewModel = rememberViewModel(cached = false) { ManageJobPositionsViewModel(jobPositionId = jobPositionId) }
    val state by viewModel.state.collectAsState()
    val isEdit = jobPositionId != null

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ManageJobPositionsContract.Effect.NavigateBackToJobPositions ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.JobPositions.Index.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ManageJobPositionsContract.Intent.LoadInitialData)
    }

    AdminPageLayout(
        title = if (isEdit) "Edit job position" else "Create job position"
    ) {
        ManageJobPositionsPageContent(
            state = state,
            isEdit = isEdit,
            sendIntent = viewModel::sendIntent
        )
    }
}

@Composable
private fun ManageJobPositionsPageContent(
    state: ManageJobPositionsContract.State,
    isEdit: Boolean,
    sendIntent: (ManageJobPositionsContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    val colorScheme = ColorMode.current.toColorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(1000.px)
            .padding(leftRight = 24.px, topBottom = 58.px),
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        TitleLarge(
            text = if (isEdit) "Edit job position" else "Create job position",
            color = colorScheme.onBackground
        )

        DsEditableField(
            modifier = Modifier.fillMaxWidth(),
            id = "job-position-name-input",
            label = "Name",
            placeholder = "Enter name of the job position",
            value = state.name,
            valid = !state.hasAttemptedSave || state.isNameValid,
            onValueChange = { sendIntent(ManageJobPositionsContract.Intent.UpdateName(it)) },
            required = true,
            backgroundColor = colorScheme.surfaceContainer,
            focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
        )

        Spacer(Modifier.height(8.px))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEdit) {
                DestructiveOutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageJobPositionsContract.Intent.ShowDeleteDialog) }
                ) {
                    Text("Delete")
                }
            } else {
                Spacer(Modifier)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.px), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageJobPositionsContract.Intent.Cancel) }
                ) {
                    Text("Cancel")
                }

                FilledButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageJobPositionsContract.Intent.SaveJobPosition) }
                ) {
                    if (state.isSaving) DsSpinner(size = SpinnerSize.Small) else Text("Save")
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        Dialog(onHideDialog = { sendIntent(ManageJobPositionsContract.Intent.HideDeleteDialog) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                TitleLarge(
                    text = "Delete job position?",
                    color = colorScheme.onSurface
                )
                SpanText(
                    text = "Are you sure you want to delete this job position? This action cannot be undone.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .textAlign(com.varabyte.kobweb.compose.css.TextAlign.Left)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        borderRadius = DsBorderRadius(8.px),
                        onClick = { sendIntent(ManageJobPositionsContract.Intent.HideDeleteDialog) }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.px))
                    DestructiveFilledButton(
                        borderRadius = DsBorderRadius(8.px),
                        onClick = { sendIntent(ManageJobPositionsContract.Intent.ConfirmDelete) }
                    ) {
                        if (state.isDeleting) DsSpinner(size = SpinnerSize.Small) else Text("Delete")
                    }
                }
            }
        }
    }
}
