package cbconnectit.portfolio.web.pages.testimonials.manage

import cbconnectit.portfolio.web.data.models.domain.Company
import cbconnectit.portfolio.web.data.models.domain.JobPosition
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.utils.MVI

interface ManageTestimonialsContract :
    MVI<ManageTestimonialsContract.State, ManageTestimonialsContract.Intent, ManageTestimonialsContract.Effect> {

    data class State(
        val testimonial: Testimonial? = null,
        val fullName: String = "",
        val imageUrl: String = "",
        val review: String = "",
        val companyId: String = "",
        val jobPositionId: String = "",
        val companies: List<Company> = emptyList(),
        val jobPositions: List<JobPosition> = emptyList(),
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val isDeleting: Boolean = false,
        val showDeleteDialog: Boolean = false,
        val hasAttemptedSave: Boolean = false
    ) {
        val isImageUrlValid: Boolean
            get() = imageUrl.trim().startsWith("http://", ignoreCase = true) ||
                imageUrl.trim().startsWith("https://", ignoreCase = true)

        val areRequiredFieldsValid: Boolean
            get() = fullName.isNotBlank() &&
                review.isNotBlank() &&
                companyId.isNotBlank() &&
                jobPositionId.isNotBlank() &&
                imageUrl.isNotBlank()
    }

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class UpdateFullName(val fullName: String) : Intent()
        data class UpdateImageUrl(val imageUrl: String) : Intent()
        data class UpdateReview(val review: String) : Intent()
        data class UpdateCompanyId(val companyId: String) : Intent()
        data class UpdateJobPositionId(val jobPositionId: String) : Intent()
        data object SaveTestimonial : Intent()
        data object ShowDeleteDialog : Intent()
        data object HideDeleteDialog : Intent()
        data object ConfirmDelete : Intent()
        data object Cancel : Intent()
    }

    sealed class Effect {
        data object NavigateBackToTestimonials : Effect()
    }
}
