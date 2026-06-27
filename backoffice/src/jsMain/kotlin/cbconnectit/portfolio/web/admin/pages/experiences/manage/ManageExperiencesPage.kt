package cbconnectit.portfolio.web.admin.pages.experiences.manage

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
import com.materialkobweb.components.widgets.DsEditableArea
import com.materialkobweb.components.widgets.DsEditableField
import com.materialkobweb.components.widgets.DsMultiSelect
import com.materialkobweb.components.widgets.DsSelect
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
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Page("/admin/experiences/manage")
@Composable
fun AdminManageExperiencesPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val experienceId = ctx.route.params["id"]
    val viewModel = rememberViewModel(cached = false) { ManageExperiencesViewModel(experienceId = experienceId) }
    val state by viewModel.state.collectAsState()
    val isEdit = experienceId != null

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ManageExperiencesContract.Effect.NavigateBackToExperiences ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Experiences.Index.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ManageExperiencesContract.Intent.LoadInitialData)
    }

    AdminPageLayout(
        title = if (isEdit) "Edit experience" else "Create experience"
    ) {
        ManageExperiencesPageContent(
            state = state,
            isEdit = isEdit,
            sendIntent = viewModel::sendIntent
        )
    }
}

@Composable
private fun ManageExperiencesPageContent(
    state: ManageExperiencesContract.State,
    isEdit: Boolean,
    sendIntent: (ManageExperiencesContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    val colorScheme = ColorMode.current.toColorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(1000.px)
            .padding(leftRight = 24.px, topBottom = 58.px),
        verticalArrangement = Arrangement.spacedBy(20.px)
    ) {
        TitleLarge(
            text = if (isEdit) "Edit experience" else "Create experience",
            color = colorScheme.onBackground
        )

        FilledButton(
            borderRadius = DsBorderRadius(8.px),
            onClick = { sendIntent(ManageExperiencesContract.Intent.ToggleFreelance) }
        ) {
            Text(if (state.asFreelance) "Freelance: Yes" else "Freelance: No")
        }

        SimpleGrid(
            modifier = Modifier.fillMaxWidth().gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            DsSelect(
                modifier = Modifier.fillMaxWidth(),
                id = "experience-company-select",
                label = "Company",
                placeholder = "Select company",
                items = state.companies.map { it.name },
                preselectedItem = state.companies.firstOrNull { it.id == state.companyId }?.name
            ) { _, selectedCompanyName ->
                val selectedCompany = state.companies.firstOrNull { it.name == selectedCompanyName } ?: return@DsSelect
                sendIntent(ManageExperiencesContract.Intent.UpdateCompanyId(selectedCompany.id))
            }

            DsSelect(
                modifier = Modifier.fillMaxWidth(),
                id = "experience-job-position-select",
                label = "Job Position",
                placeholder = "Select job position",
                items = state.jobPositions.map { it.name },
                preselectedItem = state.jobPositions.firstOrNull { it.id == state.jobPositionId }?.name
            ) { _, selectedJobPositionName ->
                val selectedJobPosition = state.jobPositions.firstOrNull { it.name == selectedJobPositionName } ?: return@DsSelect
                sendIntent(ManageExperiencesContract.Intent.UpdateJobPositionId(selectedJobPosition.id))
            }
        }

        DsMultiSelect(
            modifier = Modifier.fillMaxWidth(),
            id = "experience-tags-select",
            label = "Tags",
            placeholder = "Select tags",
            items = state.tags.map { it.name },
            selectedItems = state.tags.filter { state.selectedTagIds.contains(it.id) }.map { it.name }
        ) { selectedTagName ->
            val selectedTag = state.tags.firstOrNull { it.name == selectedTagName } ?: return@DsMultiSelect
            sendIntent(ManageExperiencesContract.Intent.ToggleTagId(selectedTag.id))
        }

        SimpleGrid(
            modifier = Modifier.fillMaxWidth().gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "experience-from-input",
                label = "From",
                placeholder = "Start date",
                value = state.from,
                valid = !state.hasAttemptedSave || state.from.isNotBlank(),
                onValueChange = { sendIntent(ManageExperiencesContract.Intent.UpdateFrom(it)) },
                type = InputType.Date,
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )

            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "experience-to-input",
                label = "To",
                placeholder = "End date",
                value = state.to,
                valid = !state.hasAttemptedSave || state.to.isNotBlank(),
                onValueChange = { sendIntent(ManageExperiencesContract.Intent.UpdateTo(it)) },
                type = InputType.Date,
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )
        }

        DsEditableField(
            modifier = Modifier.fillMaxWidth(),
            id = "experience-short-description-input",
            label = "Short description",
            placeholder = "Short summary",
            value = state.shortDescription,
            valid = !state.hasAttemptedSave || state.shortDescription.isNotBlank(),
            onValueChange = { sendIntent(ManageExperiencesContract.Intent.UpdateShortDescription(it)) },
            required = true,
            backgroundColor = colorScheme.surfaceContainer,
            focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
        )

        DsEditableArea(
            modifier = Modifier.fillMaxWidth(),
            id = "experience-description-area",
            label = "Description",
            placeholder = "Detailed experience description",
            value = state.description,
            valid = !state.hasAttemptedSave || state.description.isNotBlank(),
            onValueChange = { sendIntent(ManageExperiencesContract.Intent.UpdateDescription(it)) },
            required = true,
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
                    onClick = { sendIntent(ManageExperiencesContract.Intent.ShowDeleteDialog) }
                ) {
                    Text("Delete")
                }
            } else {
                Spacer(Modifier)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.px), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageExperiencesContract.Intent.Cancel) }
                ) {
                    Text("Cancel")
                }

                FilledButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageExperiencesContract.Intent.SaveExperience) }
                ) {
                    if (state.isSaving) DsSpinner(size = SpinnerSize.Small) else Text("Save")
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        Dialog(onHideDialog = { sendIntent(ManageExperiencesContract.Intent.HideDeleteDialog) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                TitleLarge(
                    text = "Delete experience?",
                    color = colorScheme.onSurface
                )
                SpanText(
                    text = "Are you sure you want to delete this experience? This action cannot be undone.",
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
                        onClick = { sendIntent(ManageExperiencesContract.Intent.HideDeleteDialog) }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.px))
                    DestructiveFilledButton(
                        borderRadius = DsBorderRadius(8.px),
                        onClick = { sendIntent(ManageExperiencesContract.Intent.ConfirmDelete) }
                    ) {
                        if (state.isDeleting) DsSpinner(size = SpinnerSize.Small) else Text("Delete")
                    }
                }
            }
        }
    }
}
