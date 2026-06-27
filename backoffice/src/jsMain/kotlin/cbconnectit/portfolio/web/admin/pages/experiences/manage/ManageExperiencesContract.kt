package cbconnectit.portfolio.web.admin.pages.experiences.manage

import cbconnectit.portfolio.web.data.models.domain.Company
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.data.models.domain.JobPosition
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.utils.MVI

interface ManageExperiencesContract :
    MVI<ManageExperiencesContract.State, ManageExperiencesContract.Intent, ManageExperiencesContract.Effect> {

    data class State(
        val experience: Experience? = null,
        val shortDescription: String = "",
        val description: String = "",
        val from: String = "",
        val to: String = "",
        val asFreelance: Boolean = false,
        val companyId: String = "",
        val jobPositionId: String = "",
        val selectedTagIds: List<String> = emptyList(),
        val companies: List<Company> = emptyList(),
        val jobPositions: List<JobPosition> = emptyList(),
        val tags: List<Tag> = emptyList(),
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val isDeleting: Boolean = false,
        val showDeleteDialog: Boolean = false,
        val hasAttemptedSave: Boolean = false
    ) {
        val areRequiredFieldsValid: Boolean
            get() = shortDescription.isNotBlank() &&
                description.isNotBlank() &&
                from.isNotBlank() &&
                to.isNotBlank() &&
                companyId.isNotBlank() &&
                jobPositionId.isNotBlank()
    }

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class UpdateShortDescription(val shortDescription: String) : Intent()
        data class UpdateDescription(val description: String) : Intent()
        data class UpdateFrom(val from: String) : Intent()
        data class UpdateTo(val to: String) : Intent()
        data object ToggleFreelance : Intent()
        data class UpdateCompanyId(val companyId: String) : Intent()
        data class UpdateJobPositionId(val jobPositionId: String) : Intent()
        data class ToggleTagId(val tagId: String) : Intent()
        data object SaveExperience : Intent()
        data object ShowDeleteDialog : Intent()
        data object HideDeleteDialog : Intent()
        data object ConfirmDelete : Intent()
        data object Cancel : Intent()
    }

    sealed class Effect {
        data object NavigateBackToExperiences : Effect()
    }
}
