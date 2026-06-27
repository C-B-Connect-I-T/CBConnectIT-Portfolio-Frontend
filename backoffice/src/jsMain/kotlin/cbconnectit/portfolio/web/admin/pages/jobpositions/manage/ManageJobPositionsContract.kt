package cbconnectit.portfolio.web.admin.pages.jobpositions.manage

import cbconnectit.portfolio.web.data.models.domain.JobPosition
import cbconnectit.portfolio.web.utils.MVI

interface ManageJobPositionsContract :
    MVI<ManageJobPositionsContract.State, ManageJobPositionsContract.Intent, ManageJobPositionsContract.Effect> {

    data class State(
        val jobPosition: JobPosition? = null,
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
        data object SaveJobPosition : Intent()
        data object ShowDeleteDialog : Intent()
        data object HideDeleteDialog : Intent()
        data object ConfirmDelete : Intent()
        data object Cancel : Intent()
    }

    sealed class Effect {
        data object NavigateBackToJobPositions : Effect()
    }
}
