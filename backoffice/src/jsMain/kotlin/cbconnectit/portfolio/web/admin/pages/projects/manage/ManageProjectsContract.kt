package cbconnectit.portfolio.web.admin.pages.projects.manage

import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.utils.MVI
import org.w3c.files.File

interface ManageProjectsContract :
    MVI<ManageProjectsContract.State, ManageProjectsContract.Intent, ManageProjectsContract.Effect> {

    data class State(
        val project: Project? = null,
        val tags: List<Tag> = emptyList(),
        val title: String = "",
        val shortDescription: String = "",
        val description: String = "",
        val imageAltText: String = "",
        val bannerImageAltText: String = "",
        val selectedTagIds: List<String> = emptyList(),
        val linksInput: String = "",
        val imageFile: File? = null,
        val bannerImageFile: File? = null,
        val imagePreviewUrl: String? = null,
        val bannerImagePreviewUrl: String? = null,
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val isDeleting: Boolean = false,
        val hasAttemptedSave: Boolean = false,
        val showDeleteDialog: Boolean = false
    ) {
        val hasImageInFinalState: Boolean
            get() = imageFile != null || project?.image != null

        val hasBannerImageInFinalState: Boolean
            get() = bannerImageFile != null || project?.bannerImage != null

        val areRequiredFieldsValid: Boolean
            get() = title.isNotBlank() &&
                    shortDescription.isNotBlank() &&
                    description.isNotBlank() &&
                    imageAltText.isNotBlank() &&
                    bannerImageAltText.isNotBlank() &&
                    selectedTagIds.isNotEmpty() &&
                    hasImageInFinalState &&
                    hasBannerImageInFinalState
    }

    sealed class Intent {
        data object LoadInitialData : Intent()
        data class UpdateTitle(val title: String) : Intent()
        data class UpdateShortDescription(val shortDescription: String) : Intent()
        data class UpdateDescription(val description: String) : Intent()
        data class UpdateImageAltText(val imageAltText: String) : Intent()
        data class UpdateBannerImageAltText(val bannerImageAltText: String) : Intent()
        data class ToggleTagId(val tagId: String) : Intent()
        data class UpdateLinksInput(val linksInput: String) : Intent()
        data class UpdateImageFile(val file: File) : Intent()
        data class UpdateBannerImageFile(val file: File) : Intent()
        data object SaveProject : Intent()
        data object ShowDeleteDialog : Intent()
        data object HideDeleteDialog : Intent()
        data object ConfirmDelete : Intent()
        data object Cancel : Intent()
    }

    sealed class Effect {
        data object NavigateBackToProjects : Effect()
    }
}
