package cbconnectit.portfolio.web.admin.pages.companies.manage

import cbconnectit.portfolio.web.data.models.domain.Company
import cbconnectit.portfolio.web.utils.MVI

interface ManageCompaniesContract :
    MVI<ManageCompaniesContract.State, ManageCompaniesContract.Intent, ManageCompaniesContract.Effect> {

    data class State(
        val company: Company? = null,
        val name: String = "",
        val linksInput: String = "",
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
        data class UpdateLinksInput(val linksInput: String) : Intent()
        data object SaveCompany : Intent()
        data object ShowDeleteDialog : Intent()
        data object HideDeleteDialog : Intent()
        data object ConfirmDelete : Intent()
        data object Cancel : Intent()
    }

    sealed class Effect {
        data object NavigateBackToCompanies : Effect()
    }
}
