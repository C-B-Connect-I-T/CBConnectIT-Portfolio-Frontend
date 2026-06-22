package cbconnectit.portfolio.web.pages.testimonials.manage

import cbconnectit.portfolio.web.data.models.domain.Company
import cbconnectit.portfolio.web.data.models.domain.JobPosition
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.utils.MVI
import org.w3c.files.File

interface ManageTestimonialsContract :
    MVI<ManageTestimonialsContract.State, ManageTestimonialsContract.Intent, ManageTestimonialsContract.Effect> {

    data class State(
        val testimonial: Testimonial? = null,
        val fullName: String = "",
        val review: String = "",
        val companyId: String = "",
        val jobPositionId: String = "",
        val companies: List<Company> = emptyList(),
        val jobPositions: List<JobPosition> = emptyList(),
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val isDeleting: Boolean = false,
        val showDeleteDialog: Boolean = false,
        val hasAttemptedSave: Boolean = false,
        val isImageLoading: Boolean = false,
        val avatarAltText: String = "",
        val avatarImageFile: File? = null,
        val avatarImageUrl: String? = null
    ) {
        val areRequiredFieldsValid: Boolean
            get() = fullName.isNotBlank() &&
                    review.isNotBlank() &&
                    companyId.isNotBlank() &&
                    jobPositionId.isNotBlank() &&
                    avatarAltText.isNotBlank()
    }

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class UpdateFullName(val fullName: String) : Intent()
        data class UpdateReview(val review: String) : Intent()
        data class UpdateCompanyId(val companyId: String) : Intent()
        data class UpdateJobPositionId(val jobPositionId: String) : Intent()
        data class UpdateAvatarAltText(val avatarAltText: String) : Intent()

        // Create mode: stage a local file to be sent with SaveTestimonial
        data class UpdateAvatarFile(val file: File) : Intent()

        // Edit mode: upload immediately via dedicated endpoint
        data class UploadAvatar(val file: File) : Intent()
        data object RemoveAvatar : Intent()
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
