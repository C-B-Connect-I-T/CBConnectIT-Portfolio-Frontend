package cbconnectit.portfolio.web.admin.pages.tags.manage

import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.utils.MVI

interface ManageTagsContract : MVI<ManageTagsContract.State, ManageTagsContract.Intent, ManageTagsContract.Effect> {

    data class State(
        val tag: Tag? = null,
        val name: String = "",
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val isDeleting: Boolean = false,
        val showDeleteDialog: Boolean = false,
        val hasAttemptedSave: Boolean = false
    ) {
        val isNameValid: Boolean get() = name.isNotBlank()
    }

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class UpdateName(val name: String) : Intent()
        data object SaveTag : Intent()
        data object ShowDeleteDialog : Intent()
        data object HideDeleteDialog : Intent()
        data object ConfirmDelete : Intent()
        data object Cancel : Intent()
    }

    sealed class Effect {
        data object NavigateBackToTags : Effect()
    }
}
