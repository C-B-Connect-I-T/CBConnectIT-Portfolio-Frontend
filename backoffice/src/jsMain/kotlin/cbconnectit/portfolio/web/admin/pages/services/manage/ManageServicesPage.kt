package cbconnectit.portfolio.web.admin.pages.services.manage

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
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import cbconnectit.portfolio.web.styles.OnWarningColor
import cbconnectit.portfolio.web.styles.WarningColor
import cbconnectit.portfolio.web.utils.rememberViewModel
import cbconnectit.portfolio.web.utils.withAlpha
import com.materialkobweb.components.Spacer
import com.materialkobweb.components.widgets.DsBorderRadius
import com.materialkobweb.components.widgets.DsEditableArea
import com.materialkobweb.components.widgets.DsEditableField
import com.materialkobweb.components.widgets.DsSelect
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
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
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
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

private const val NO_PARENT_OPTION = "No parent service"
private const val NO_TAG_OPTION = "No tag"

@Page("/admin/services/manage")
@Composable
fun AdminManageServicesPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val serviceId = ctx.route.params["id"]
    val viewModel = rememberViewModel(cached = false) { ManageServicesViewModel(serviceId = serviceId) }
    val state by viewModel.state.collectAsState()
    val isEdit = serviceId != null

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ManageServicesContract.Effect.NavigateBackToServices ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Services.Index.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ManageServicesContract.Intent.LoadInitialData)
    }

    AdminPageLayout(
        title = if (isEdit) "Edit service" else "Create service"
    ) {
        ManageServicesPageContent(
            state = state,
            isEdit = isEdit,
            sendIntent = viewModel::sendIntent
        )
    }
}

@Composable
private fun ManageServicesPageContent(
    state: ManageServicesContract.State,
    isEdit: Boolean,
    sendIntent: (ManageServicesContract.Intent) -> Unit
) {
    val breakpoint = rememberBreakpoint()
    val colorScheme = ColorMode.current.toColorScheme

    val topLevelServices = state.allServices.topLevelServices()
        .filter { it.id != state.service?.id }
        .sortedBy { it.title.lowercase() }

    val parentServiceOptions = buildList {
        add(NO_PARENT_OPTION)
        addAll(topLevelServices.map { it.title })
    }
    val selectedParentServiceTitle = if (state.parentServiceId == null) {
        NO_PARENT_OPTION
    } else {
        topLevelServices.firstOrNull { it.id == state.parentServiceId }?.title ?: NO_PARENT_OPTION
    }

    val tagOptions = buildList {
        add(NO_TAG_OPTION)
        addAll(state.tags.map { it.name }.sortedBy { it.lowercase() })
    }
    val selectedTagTitle = if (state.tagId == null) {
        NO_TAG_OPTION
    } else {
        state.tags.firstOrNull { it.id == state.tagId }?.name ?: NO_TAG_OPTION
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .maxWidth(1000.px)
            .padding(leftRight = 24.px, topBottom = 58.px),
        verticalArrangement = Arrangement.spacedBy(28.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleLarge(
            text = if (isEdit) "Edit service" else "Create service",
            color = colorScheme.onBackground
        )

        SimpleGrid(
            modifier = Modifier.fillMaxWidth().gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            ImageChooser(
                modifier = Modifier.size(220.px),
                id = "service-image",
                label = "Image",
                imageUrl = state.imagePreviewUrl,
                centerText = "Click to upload service image",
                isLoading = state.isLoading,
                onFileSelected = { file ->
                    sendIntent(ManageServicesContract.Intent.UpdateImageFile(file))
                }
            )

            ImageChooser(
                modifier = Modifier.fillMaxWidth().height(220.px),
                id = "service-banner-image",
                label = "Banner Image",
                imageUrl = state.bannerImagePreviewUrl,
                centerText = "Click to upload banner image",
                isLoading = state.isLoading,
                onFileSelected = { file ->
                    sendIntent(ManageServicesContract.Intent.UpdateBannerImageFile(file))
                },
                onDeleteClicked = if (state.hasBannerImageInFinalState) {
                    { sendIntent(ManageServicesContract.Intent.RemoveBannerImage) }
                } else null
            )
        }

        if (state.parentServiceId == null && !state.hasBannerImageInFinalState) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(98.percent)
                    .padding(12.px)
                    .backgroundColor(MaterialColorVars.SecondaryContainer.value())
                    .borderRadius(8.px)
                    .border(1.px, LineStyle.Solid, MaterialColorVars.Secondary.value())
            ) {
                SpanText(
                    text = "Tip: Top-level services work better with a banner image, but it is optional.",
                    modifier = Modifier
                        .padding(top = 4.px)
                        .fontSize(14.px)
                        .color(MaterialColorVars.OnSecondaryContainer.withAlpha(0.8f))
                )
            }
        }

        H2(
            attrs = Modifier
                .fillMaxWidth()
                .borderBottom(2.px, LineStyle.Solid, Color.gray)
                .padding(bottom = 4.px)
                .toAttrs()
        ) {
            Text("Content")
        }

        Column(
            modifier = Modifier.fillMaxWidth(98.percent),
            verticalArrangement = Arrangement.spacedBy(28.px)
        ) {
            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "service-title-input",
                label = "Title",
                placeholder = "Enter service title",
                value = state.title,
                valid = !state.hasAttemptedSave || state.title.isNotBlank(),
                onValueChange = { sendIntent(ManageServicesContract.Intent.UpdateTitle(it)) },
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )

            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "service-short-description-input",
                label = "Short Description",
                placeholder = "Enter short description (optional)",
                value = state.shortDescription,
                onValueChange = { sendIntent(ManageServicesContract.Intent.UpdateShortDescription(it)) },
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )

            DsEditableArea(
                modifier = Modifier.fillMaxWidth(),
                id = "service-description-area",
                label = "Description",
                placeholder = "Write the full service description",
                value = state.description,
                valid = !state.hasAttemptedSave || state.description.isNotBlank(),
                onValueChange = { sendIntent(ManageServicesContract.Intent.UpdateDescription(it)) },
                required = true
            )

            DsEditableArea(
                modifier = Modifier.fillMaxWidth(),
                id = "service-banner-description-input",
                label = "Banner Description",
                placeholder = "Enter banner description (optional)",
                value = state.bannerDescription,
                onValueChange = { sendIntent(ManageServicesContract.Intent.UpdateBannerDescription(it)) }
            )

            DsEditableArea(
                modifier = Modifier.fillMaxWidth(),
                id = "service-extra-info-input",
                label = "Extra Info",
                placeholder = "Enter extra information (optional)",
                value = state.extraInfo,
                onValueChange = { sendIntent(ManageServicesContract.Intent.UpdateExtraInfo(it)) }
            )
        }

        H2(
            attrs = Modifier
                .fillMaxWidth()
                .borderBottom(2.px, LineStyle.Solid, Color.gray)
                .padding(bottom = 4.px)
                .toAttrs()
        ) {
            Text("Accessibility")
        }

        SimpleGrid(
            modifier = Modifier.fillMaxWidth(98.percent).gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "service-image-alt-text-input",
                label = "Image Alt Text",
                placeholder = "Describe the service image",
                value = state.imageAltText,
                valid = !state.hasAttemptedSave || state.imageAltText.isNotBlank(),
                onValueChange = { sendIntent(ManageServicesContract.Intent.UpdateImageAltText(it)) },
                required = true,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )

            DsEditableField(
                modifier = Modifier.fillMaxWidth(),
                id = "service-banner-image-alt-text-input",
                label = "Banner Image Alt Text",
                placeholder = "Describe the banner image",
                value = state.bannerImageAltText,
                readOnly = !state.hasBannerImageInFinalState,
                valid = !state.hasAttemptedSave || !state.hasBannerImageInFinalState || state.bannerImageAltText.isNotBlank(),
                onValueChange = {
                    if (state.hasBannerImageInFinalState) {
                        sendIntent(ManageServicesContract.Intent.UpdateBannerImageAltText(it))
                    }
                },
                required = state.hasBannerImageInFinalState,
                backgroundColor = colorScheme.surfaceContainer,
                focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
            )
        }

        if (!state.hasBannerImageInFinalState) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(98.percent)
                    .padding(12.px)
                    .backgroundColor(ColorMode.current.WarningColor)
                    .borderRadius(8.px)
                    .border(1.px, LineStyle.Solid, MaterialColorVars.OutlineVariant.value())
            ) {
                SpanText(
                    text = "Banner image alt text is available once a banner image is selected.",
                    modifier = Modifier
                        .padding(top = 4.px)
                        .fontSize(14.px)
                        .color(ColorMode.current.OnWarningColor)
                )
            }
        }

        H2(
            attrs = Modifier
                .fillMaxWidth()
                .borderBottom(2.px, LineStyle.Solid, Color.gray)
                .padding(bottom = 4.px)
                .toAttrs()
        ) {
            Text("Configuration")
        }

        SimpleGrid(
            modifier = Modifier.fillMaxWidth(98.percent).gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            DsSelect(
                modifier = Modifier.fillMaxWidth(),
                id = "service-parent-select",
                label = "Parent Service",
                placeholder = "Select parent service",
                enabled = !state.hasChildren,
                items = parentServiceOptions,
                preselectedItem = selectedParentServiceTitle
            ) { _, selectedParentTitle ->
                val parentServiceId = if (selectedParentTitle == NO_PARENT_OPTION) {
                    null
                } else {
                    topLevelServices.firstOrNull { it.title == selectedParentTitle }?.id
                }

                sendIntent(ManageServicesContract.Intent.UpdateParentServiceId(parentServiceId))
            }

            DsSelect(
                modifier = Modifier.fillMaxWidth(),
                id = "service-tag-select",
                label = "Tag",
                placeholder = "Select tag",
                items = tagOptions,
                preselectedItem = selectedTagTitle
            ) { _, selectedTagTitle ->
                val tagId = if (selectedTagTitle == NO_TAG_OPTION) {
                    null
                } else {
                    state.tags.firstOrNull { it.name == selectedTagTitle }?.id
                }
                sendIntent(ManageServicesContract.Intent.UpdateTagId(tagId))
            }
        }

        if (state.hasChildren) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(98.percent)
                    .padding(12.px)
                    .backgroundColor(ColorMode.current.WarningColor)
                    .borderRadius(8.px)
                    .border(1.px, LineStyle.Solid, MaterialColorVars.OutlineVariant.value())
            ) {
                SpanText(
                    text = "This service has sub-services. Parent service assignment is disabled to keep hierarchy depth at one level.",
                    modifier = Modifier
                        .padding(top = 4.px)
                        .fontSize(14.px)
                        .color(ColorMode.current.OnWarningColor)
                )
            }
        }

        Spacer(Modifier.height(16.px))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEdit) {
                DestructiveOutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageServicesContract.Intent.ShowDeleteDialog) }
                ) {
                    Text("Delete")
                }
            } else {
                Spacer(Modifier)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.px), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageServicesContract.Intent.Cancel) }
                ) {
                    Text("Cancel")
                }

                FilledButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageServicesContract.Intent.SaveService) }
                ) {
                    if (state.isSaving) DsSpinner(size = SpinnerSize.Small) else Text("Save")
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        Dialog(onHideDialog = { sendIntent(ManageServicesContract.Intent.HideDeleteDialog) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                TitleLarge(
                    text = "Delete service?",
                    color = colorScheme.onSurface
                )
                SpanText(
                    text = "Are you sure you want to delete this service? This action cannot be undone.",
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
                        onClick = { sendIntent(ManageServicesContract.Intent.HideDeleteDialog) }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.px))
                    DestructiveFilledButton(
                        borderRadius = DsBorderRadius(8.px),
                        onClick = { sendIntent(ManageServicesContract.Intent.ConfirmDelete) }
                    ) {
                        if (state.isDeleting) DsSpinner(size = SpinnerSize.Small) else Text("Delete")
                    }
                }
            }
        }
    }
}

private fun List<Service>.topLevelServices(): List<Service> {
    val subServiceIds = mutableSetOf<String>()
    forEach { service ->
        service.subServices.orEmpty().forEach { subService ->
            subServiceIds.add(subService.id)
        }
    }

    return filter { it.id !in subServiceIds }
}
