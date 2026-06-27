package cbconnectit.portfolio.web.admin.pages.projects.manage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.admin.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.components.DestructiveFilledButton
import cbconnectit.portfolio.web.components.DestructiveOutlinedButton
import cbconnectit.portfolio.web.components.Dialog
import cbconnectit.portfolio.web.components.ImageChooser
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import cbconnectit.portfolio.web.styles.OnWarningColor
import cbconnectit.portfolio.web.styles.WarningColor
import cbconnectit.portfolio.web.utils.rememberViewModel
import com.materialkobweb.components.Spacer
import com.materialkobweb.components.widgets.DsBorderRadius
import com.materialkobweb.components.widgets.DsEditableArea
import com.materialkobweb.components.widgets.DsEditableField
import com.materialkobweb.components.widgets.DsMultiSelect
import com.materialkobweb.components.widgets.DsSpinner
import com.materialkobweb.components.widgets.FilledButton
import com.materialkobweb.components.widgets.OutlinedButton
import com.materialkobweb.components.widgets.SpinnerSize
import com.materialkobweb.styles.MaterialColorVars
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
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
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Page("/admin/projects/manage")
@Composable
fun AdminManageProjectsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val projectId = ctx.route.params["id"]
    val viewModel = rememberViewModel(cached = false) { ManageProjectsViewModel(projectId = projectId) }
    val state by viewModel.state.collectAsState()
    val isEdit = projectId != null

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ManageProjectsContract.Effect.NavigateBackToProjects ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Projects.Index.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ManageProjectsContract.Intent.LoadInitialData)
    }

    AdminPageLayout(
        title = if (isEdit) "Edit project" else "Create project"
    ) {
        ManageProjectsPageContent(
            state = state,
            isEdit = isEdit,
            sendIntent = viewModel::sendIntent
        )
    }
}

@Composable
private fun ManageProjectsPageContent(
    state: ManageProjectsContract.State,
    isEdit: Boolean,
    sendIntent: (ManageProjectsContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    val colorScheme = ColorMode.current.toColorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(1000.px)
            .padding(leftRight = 24.px, topBottom = 58.px),
        verticalArrangement = Arrangement.spacedBy(28.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleLarge(
            text = if (isEdit) "Edit project" else "Create project",
            color = colorScheme.onBackground
        )

        SimpleGrid(
            modifier = Modifier.fillMaxWidth().gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            ImageChooser(
                modifier = Modifier.size(220.px),
                id = "project-image",
                label = "Image",
                imageUrl = state.imagePreviewUrl,
                centerText = "Click to upload project image",
                isLoading = state.isLoading,
                onFileSelected = { file ->
                    sendIntent(ManageProjectsContract.Intent.UpdateImageFile(file))
                }
            )

            ImageChooser(
                modifier = Modifier.fillMaxWidth().height(220.px),
                id = "project-banner-image",
                label = "Banner Image",
                imageUrl = state.bannerImagePreviewUrl,
                centerText = "Click to upload project banner image",
                isLoading = state.isLoading,
                onFileSelected = { file ->
                    sendIntent(ManageProjectsContract.Intent.UpdateBannerImageFile(file))
                }
            )
        }

        if (!state.hasImageInFinalState || !state.hasBannerImageInFinalState) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(98.percent)
                    .padding(12.px)
                    .backgroundColor(ColorMode.current.WarningColor)
                    .borderRadius(8.px)
                    .border(1.px, LineStyle.Solid, MaterialColorVars.OutlineVariant.value())
            ) {
                SpanText(
                    text = "Projects must always have both an image and a banner image.",
                    modifier = Modifier
                        .padding(top = 4.px)
                        .fontSize(14.px)
                        .color(ColorMode.current.OnWarningColor)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(98.percent),
            verticalArrangement = Arrangement.spacedBy(20.px)
        ) {
            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "project-title-input",
                label = "Title",
                placeholder = "Enter project title",
                value = state.title,
                valid = !state.hasAttemptedSave || state.title.isNotBlank(),
                onValueChange = { sendIntent(ManageProjectsContract.Intent.UpdateTitle(it)) },
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )

            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "project-short-description-input",
                label = "Short Description",
                placeholder = "Enter short description",
                value = state.shortDescription,
                valid = !state.hasAttemptedSave || state.shortDescription.isNotBlank(),
                onValueChange = { sendIntent(ManageProjectsContract.Intent.UpdateShortDescription(it)) },
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )

            DsEditableArea(
                modifier = Modifier.fillMaxWidth(),
                id = "project-description-area",
                label = "Description",
                placeholder = "Write the full project description",
                value = state.description,
                valid = !state.hasAttemptedSave || state.description.isNotBlank(),
                onValueChange = { sendIntent(ManageProjectsContract.Intent.UpdateDescription(it)) },
                required = true
            )
        }

        SimpleGrid(
            modifier = Modifier.fillMaxWidth(98.percent).gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "project-image-alt-text-input",
                label = "Image Alt Text",
                placeholder = "Describe the project image",
                value = state.imageAltText,
                valid = !state.hasAttemptedSave || state.imageAltText.isNotBlank(),
                onValueChange = { sendIntent(ManageProjectsContract.Intent.UpdateImageAltText(it)) },
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )

            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "project-banner-image-alt-text-input",
                label = "Banner Image Alt Text",
                placeholder = "Describe the project banner image",
                value = state.bannerImageAltText,
                valid = !state.hasAttemptedSave || state.bannerImageAltText.isNotBlank(),
                onValueChange = { sendIntent(ManageProjectsContract.Intent.UpdateBannerImageAltText(it)) },
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )
        }

        DsMultiSelect(
            modifier = Modifier.fillMaxWidth(98.percent),
            id = "project-tags-select",
            label = "Tags",
            placeholder = "Select tags",
            items = state.tags.map { it.name },
            selectedItems = state.tags.filter { state.selectedTagIds.contains(it.id) }.map { it.name }
        ) { selectedTagName ->
            val selectedTag = state.tags.firstOrNull { it.name == selectedTagName } ?: return@DsMultiSelect
            sendIntent(ManageProjectsContract.Intent.ToggleTagId(selectedTag.id))
        }

        if (state.hasAttemptedSave && state.selectedTagIds.isEmpty()) {
            SpanText(
                text = "At least one tag is required.",
                modifier = Modifier
                    .fillMaxWidth(98.percent)
                    .fontSize(14.px)
                    .color(MaterialColorVars.Error.value())
            )
        }

        DsEditableArea(
            modifier = Modifier.fillMaxWidth(98.percent),
            id = "project-links-input",
            label = "Links",
            placeholder = "https://github.com/org/repo, https://example.com/demo",
            value = state.linksInput,
            onValueChange = { sendIntent(ManageProjectsContract.Intent.UpdateLinksInput(it)) }
        )

        SpanText(
            text = "Use comma or newline separated URLs. Links must start with http:// or https://",
            modifier = Modifier
                .fillMaxWidth(98.percent)
                .fontSize(14.px)
                .color(MaterialColorVars.OnSurfaceVariant.value())
        )

        Spacer(Modifier.height(16.px))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEdit) {
                DestructiveOutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageProjectsContract.Intent.ShowDeleteDialog) }
                ) {
                    Text("Delete")
                }
            } else {
                Spacer(Modifier)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.px), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageProjectsContract.Intent.Cancel) }
                ) {
                    Text("Cancel")
                }

                FilledButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageProjectsContract.Intent.SaveProject) }
                ) {
                    if (state.isSaving) DsSpinner(size = SpinnerSize.Small) else Text("Save")
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        Dialog(onHideDialog = { sendIntent(ManageProjectsContract.Intent.HideDeleteDialog) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                TitleLarge(
                    text = "Delete project?",
                    color = colorScheme.onSurface
                )
                SpanText(
                    text = "Are you sure you want to delete this project? This action cannot be undone.",
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
                        onClick = { sendIntent(ManageProjectsContract.Intent.HideDeleteDialog) }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.px))
                    DestructiveFilledButton(
                        borderRadius = DsBorderRadius(8.px),
                        onClick = { sendIntent(ManageProjectsContract.Intent.ConfirmDelete) }
                    ) {
                        if (state.isDeleting) DsSpinner(size = SpinnerSize.Small) else Text("Delete")
                    }
                }
            }
        }
    }
}
