package cbconnectit.portfolio.web.pages.testimonials.manage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.DestructiveFilledButton
import cbconnectit.portfolio.web.components.DestructiveOutlinedButton
import cbconnectit.portfolio.web.components.Dialog
import cbconnectit.portfolio.web.components.ImageChooser
import cbconnectit.portfolio.web.components.TitleLarge
import cbconnectit.portfolio.web.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import cbconnectit.portfolio.web.utils.rememberViewModel
import com.materialkobweb.components.Spacer
import com.materialkobweb.components.widgets.DsBorderRadius
import com.materialkobweb.components.widgets.DsEditableArea
import com.materialkobweb.components.widgets.DsEditableField
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
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Page("/admin/testimonials/manage")
@Composable
fun AdminManageTestimonialsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val testimonialId = ctx.route.params["id"]
    val viewModel = rememberViewModel(cached = false) { ManageTestimonialsViewModel(testimonialId = testimonialId) }
    val state by viewModel.state.collectAsState()
    val isEdit = testimonialId != null

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ManageTestimonialsContract.Effect.NavigateBackToTestimonials ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Testimonials.Index.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ManageTestimonialsContract.Intent.LoadInitialData)
    }

    AdminPageLayout(
        title = if (isEdit) "Edit testimonial" else "Create testimonial"
    ) {
        ManageTestimonialsPageContent(
            state = state,
            isEdit = isEdit,
            sendIntent = viewModel::sendIntent
        )
    }
}

@Composable
private fun ManageTestimonialsPageContent(
    state: ManageTestimonialsContract.State,
    isEdit: Boolean,
    sendIntent: (ManageTestimonialsContract.Intent) -> Unit
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
            text = if (isEdit) "Edit testimonial" else "Create testimonial",
            color = colorScheme.onBackground
        )

        // Logo upload
        ImageChooser(
            modifier = Modifier.size(200.px),
            id = "avatar-image",
            label = "Avatar Image",
            imageUrl = state.avatarImageUrl ?: state.testimonial?.avatarImage?.url,
            centerText = "Klik om een afbeelding te uploaden",
            isLoading = state.isImageLoading,
            onFileSelected = { file ->
                if (isEdit) {
                    sendIntent(ManageTestimonialsContract.Intent.UploadAvatar(file))
                } else {
                    sendIntent(ManageTestimonialsContract.Intent.UpdateAvatarFile(file))
                }
            },
            onDeleteClicked = if (isEdit && state.testimonial?.avatarImage != null) {
                { sendIntent(ManageTestimonialsContract.Intent.RemoveAvatar) }
            } else null
        )

        DsEditableField(
            modifier = Modifier.fillMaxWidth(),
            id = "testimonial-full-name-input",
            label = "Full name",
            placeholder = "Enter full name",
            value = state.fullName,
            valid = !state.hasAttemptedSave || state.fullName.isNotBlank(),
            onValueChange = { sendIntent(ManageTestimonialsContract.Intent.UpdateFullName(it)) },
            required = true,
            backgroundColor = colorScheme.surfaceContainer,
            focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
        )

        DsEditableField(
            modifier = Modifier.fillMaxWidth(),
            id = "testimonial-avatar-alt-text-input",
            label = "Avatar alt text",
            placeholder = "Enter avatar's alt text",
            value = state.avatarAltText,
            valid = !state.hasAttemptedSave || state.avatarAltText.isNotBlank(),
            onValueChange = { sendIntent(ManageTestimonialsContract.Intent.UpdateAvatarAltText(it)) },
            required = true,
            backgroundColor = colorScheme.surfaceContainer,
            focusBorderColor = colorScheme.primary.toRgb().copyf(alpha = 0.6f)
        )

        SimpleGrid(
            modifier = Modifier.fillMaxWidth().gap(20.px, 32.px),
            numColumns = numColumns(1, 2)
        ) {
            DsSelect(
                modifier = Modifier.fillMaxWidth(),
                id = "testimonial-company-select",
                label = "Company",
                placeholder = "Select company",
                items = state.companies.map { it.name },
                preselectedItem = state.companies.firstOrNull { it.id == state.companyId }?.name
            ) { _, selectedCompanyName ->
                val selectedCompany = state.companies.firstOrNull { it.name == selectedCompanyName } ?: return@DsSelect
                sendIntent(ManageTestimonialsContract.Intent.UpdateCompanyId(selectedCompany.id))
            }

            DsSelect(
                modifier = Modifier.fillMaxWidth(),
                id = "testimonial-job-position-select",
                label = "Job Position",
                placeholder = "Select job position",
                items = state.jobPositions.map { it.name },
                preselectedItem = state.jobPositions.firstOrNull { it.id == state.jobPositionId }?.name
            ) { _, selectedJobPositionName ->
                val selectedJobPosition = state.jobPositions.firstOrNull { it.name == selectedJobPositionName } ?: return@DsSelect
                sendIntent(ManageTestimonialsContract.Intent.UpdateJobPositionId(selectedJobPosition.id))
            }
        }

        DsEditableArea(
            modifier = Modifier.fillMaxWidth(),
            id = "testimonial-review-area",
            label = "Review",
            placeholder = "Write the testimonial review",
            value = state.review,
            valid = !state.hasAttemptedSave || state.review.isNotBlank(),
            onValueChange = { sendIntent(ManageTestimonialsContract.Intent.UpdateReview(it)) },
            required = true
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
                    onClick = { sendIntent(ManageTestimonialsContract.Intent.ShowDeleteDialog) }
                ) {
                    Text("Delete")
                }
            } else {
                Spacer(Modifier)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.px), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageTestimonialsContract.Intent.Cancel) }
                ) {
                    Text("Cancel")
                }

                FilledButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageTestimonialsContract.Intent.SaveTestimonial) }
                ) {
                    if (state.isSaving) DsSpinner(size = SpinnerSize.Small) else Text("Save")
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        Dialog(onHideDialog = { sendIntent(ManageTestimonialsContract.Intent.HideDeleteDialog) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                TitleLarge(
                    text = "Delete testimonial?",
                    color = colorScheme.onSurface
                )
                SpanText(
                    text = "Are you sure you want to delete this testimonial? This action cannot be undone.",
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
                        onClick = { sendIntent(ManageTestimonialsContract.Intent.HideDeleteDialog) }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.px))
                    DestructiveFilledButton(
                        borderRadius = DsBorderRadius(8.px),
                        onClick = { sendIntent(ManageTestimonialsContract.Intent.ConfirmDelete) }
                    ) {
                        if (state.isDeleting) DsSpinner(size = SpinnerSize.Small) else Text("Delete")
                    }
                }
            }
        }
    }
}
