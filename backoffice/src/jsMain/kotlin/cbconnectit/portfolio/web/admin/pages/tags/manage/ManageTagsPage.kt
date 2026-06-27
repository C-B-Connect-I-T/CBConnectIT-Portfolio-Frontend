package cbconnectit.portfolio.web.admin.pages.tags.manage

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

// TODO: when trying to delete a tag that is linked to projects, let the backend return that it is not possible (with a number associated of the linked projects)
//  We could foresee a very destructive button in the dialog to force remove the tag and remove it from all linked projects by force...

//  Why this is usually best:                                                                                                                                                                                                                                                                                                                                                         ┃
//                                                                                                                                                                                                                                                                                                                                                                                    ┃
//   1. Prevents accidental data integrity loss.                                                                                                                                                                                                                                                                                                                                      ┃
//   2. Forces explicit user intent (detach/reassign first).                                                                                                                                                                                                                                                                                                                          ┃
//   3. Keeps history and reporting stable.                                                                                                                                                                                                                                                                                                                                           ┃
//                                                                                                                                                                                                                                                                                                                                                                                    ┃
//  Common policy:                                                                                                                                                                                                                                                                                                                                                                    ┃
//                                                                                                                                                                                                                                                                                                                                                                                    ┃
//   1. Backend enforces it (source of truth): reject DELETE /tags/{id} if linked projects exist.                                                                                                                                                                                                                                                                                     ┃
//   2. Frontend surfaces it nicely: disable or warn in dialog (“Tag is used by X projects; remove links first”).                                                                                                                                                                                                                                                                     ┃
//   3. Optional advanced flow later: “Delete and remove from all projects” as a separate destructive action.                                                                                                                                                                                                                                                                         ┃
//                                                                                                                                                                                                                                                                                                                                                                                    ┃
//  So for your case, I’d implement restrict delete + reference count in the dialog as the safest baseline.

@Page("/admin/tags/manage")
@Composable
fun AdminManageTagsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val tagId = ctx.route.params["id"]
    val viewModel = rememberViewModel(cached = false) { ManageTagsViewModel(tagId = tagId) }
    val state by viewModel.state.collectAsState()
    val isEdit = tagId != null

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ManageTagsContract.Effect.NavigateBackToTags ->
                    ctx.router.navigateTo(Navigation.Screen.Admin.Tags.Index.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ManageTagsContract.Intent.LoadInitialData)
    }

    AdminPageLayout(
        title = if (isEdit) "Edit tag" else "Create tag"
    ) {
        ManageTagsPageContent(
            state = state,
            isEdit = isEdit,
            sendIntent = viewModel::sendIntent
        )
    }
}

@Composable
private fun ManageTagsPageContent(
    state: ManageTagsContract.State,
    isEdit: Boolean,
    sendIntent: (ManageTagsContract.Intent) -> Unit
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
            text = if (isEdit) "Edit tag" else "Create tag",
            color = colorScheme.onBackground
        )

        DsEditableField(
            modifier = Modifier.fillMaxWidth(),
            id = "tag-name-input",
            label = "Name",
            placeholder = "Enter name of the tag",
            value = state.name,
            valid = !state.hasAttemptedSave || state.isNameValid,
            onValueChange = { sendIntent(ManageTagsContract.Intent.UpdateName(it)) },
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
                    onClick = { sendIntent(ManageTagsContract.Intent.ShowDeleteDialog) }
                ) {
                    Text("Delete")
                }
            } else {
                Spacer(Modifier)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.px), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageTagsContract.Intent.Cancel) }
                ) {
                    Text("Cancel")
                }

                FilledButton(
                    borderRadius = DsBorderRadius(8.px),
                    onClick = { sendIntent(ManageTagsContract.Intent.SaveTag) }
                ) {
                    if (state.isSaving) DsSpinner(size = SpinnerSize.Small) else Text("Save")
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        Dialog(onHideDialog = { sendIntent(ManageTagsContract.Intent.HideDeleteDialog) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.px)
            ) {
                TitleLarge(
                    text = "Delete tag?",
                    color = colorScheme.onSurface
                )
                SpanText(
                    text = "Are you sure you want to delete this tag? This action cannot be undone.",
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
                        onClick = { sendIntent(ManageTagsContract.Intent.HideDeleteDialog) }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(12.px))
                    DestructiveFilledButton(
                        borderRadius = DsBorderRadius(8.px),
                        onClick = { sendIntent(ManageTagsContract.Intent.ConfirmDelete) }
                    ) {
                        if (state.isDeleting) DsSpinner(size = SpinnerSize.Small) else Text("Delete")
                    }
                }
            }
        }
    }
}
