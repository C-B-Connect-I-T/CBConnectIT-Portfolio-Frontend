package cbconnectit.portfolio.web.admin.pages.services.manage

import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.utils.MVI
import org.w3c.files.File

interface ManageServicesContract :
    MVI<ManageServicesContract.State, ManageServicesContract.Intent, ManageServicesContract.Effect> {

    data class State(
        val service: Service? = null,
        val allServices: List<Service> = emptyList(),
        val tags: List<Tag> = emptyList(),
        val title: String = "",
        val shortDescription: String = "",
        val description: String = "",
        val imageAltText: String = "",
        val bannerImageAltText: String = "",
        val bannerDescription: String = "",
        val extraInfo: String = "",
        val parentServiceId: String? = null,
        val initialParentServiceId: String? = null,
        val tagId: String? = null,
        val imageFile: File? = null,
        val bannerImageFile: File? = null,
        val shouldRemoveBannerImage: Boolean = false,
        val imagePreviewUrl: String? = null,
        val bannerImagePreviewUrl: String? = null,
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val isDeleting: Boolean = false,
        val hasAttemptedSave: Boolean = false,
        val showDeleteDialog: Boolean = false
    ) {
        val hasChildren: Boolean
            get() = !service?.subServices.isNullOrEmpty()

        val hasImageInFinalState: Boolean
            get() = imageFile != null || service?.image != null

        val hasExistingBannerImageInFinalState: Boolean
            get() = !shouldRemoveBannerImage && service?.bannerImage != null

        val hasBannerImageInFinalState: Boolean
            get() = bannerImageFile != null || hasExistingBannerImageInFinalState

        val areRequiredFieldsValid: Boolean
            get() = title.isNotBlank() &&
                    description.isNotBlank() &&
                    imageAltText.isNotBlank() &&
                    hasImageInFinalState &&
                    (!hasBannerImageInFinalState || bannerImageAltText.isNotBlank())
    }

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class UpdateTitle(val title: String) : Intent()
        data class UpdateShortDescription(val shortDescription: String) : Intent()
        data class UpdateDescription(val description: String) : Intent()
        data class UpdateImageAltText(val imageAltText: String) : Intent()
        data class UpdateBannerImageAltText(val bannerImageAltText: String) : Intent()
        data class UpdateBannerDescription(val bannerDescription: String) : Intent()
        data class UpdateExtraInfo(val extraInfo: String) : Intent()
        data class UpdateParentServiceId(val parentServiceId: String?) : Intent()
        data class UpdateTagId(val tagId: String?) : Intent()
        data class UpdateImageFile(val file: File) : Intent()
        data class UpdateBannerImageFile(val file: File) : Intent()
        data object RemoveBannerImage : Intent()
        data object SaveService : Intent()
        data object ShowDeleteDialog : Intent()
        data object HideDeleteDialog : Intent()
        data object ConfirmDelete : Intent()
        data object Cancel : Intent()
    }

    sealed class Effect {
        data object NavigateBackToServices : Effect()
    }
}
