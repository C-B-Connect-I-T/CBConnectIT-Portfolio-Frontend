package cbconnectit.portfolio.web.admin.pages.projects.manage

import cbconnectit.portfolio.web.data.models.dto.requests.project.InsertProject
import cbconnectit.portfolio.web.data.models.dto.requests.project.UpdateProject
import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.ProjectRepo
import cbconnectit.portfolio.web.data.repos.TagRepo
import cbconnectit.portfolio.web.utils.ViewModel
import com.materialkobweb.components.toast.ToastManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.w3c.dom.url.URL

class ManageProjectsViewModel(
    private val projectId: String?,
    private val projectRepo: ProjectRepo = ProjectRepo,
    private val tagRepo: TagRepo = TagRepo
) : ViewModel(), ManageProjectsContract {
    private val _state = MutableStateFlow(ManageProjectsContract.State())
    override val state: StateFlow<ManageProjectsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ManageProjectsContract.Effect>()
    override val effect: SharedFlow<ManageProjectsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ManageProjectsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ManageProjectsContract.Intent.LoadInitialData -> loadInitialData()
            is ManageProjectsContract.Intent.UpdateTitle -> updateState { it.copy(title = intent.title) }
            is ManageProjectsContract.Intent.UpdateShortDescription -> updateState { it.copy(shortDescription = intent.shortDescription) }
            is ManageProjectsContract.Intent.UpdateDescription -> updateState { it.copy(description = intent.description) }
            is ManageProjectsContract.Intent.UpdateImageAltText -> updateState { it.copy(imageAltText = intent.imageAltText) }
            is ManageProjectsContract.Intent.UpdateBannerImageAltText -> updateState { it.copy(bannerImageAltText = intent.bannerImageAltText) }
            is ManageProjectsContract.Intent.ToggleTagId -> toggleTag(intent.tagId)
            is ManageProjectsContract.Intent.UpdateLinksInput -> updateState { it.copy(linksInput = intent.linksInput) }
            is ManageProjectsContract.Intent.UpdateImageFile -> updateImageFile(intent.file)
            is ManageProjectsContract.Intent.UpdateBannerImageFile -> updateBannerImageFile(intent.file)
            is ManageProjectsContract.Intent.SaveProject -> saveProject()
            is ManageProjectsContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageProjectsContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageProjectsContract.Intent.ConfirmDelete -> deleteProject()
            is ManageProjectsContract.Intent.Cancel -> emitEffect(ManageProjectsContract.Effect.NavigateBackToProjects)
        }
    }

    private suspend fun loadInitialData() {
        updateState { it.copy(isLoading = true) }

        loadTags()
        loadProjectIfNeeded()

        updateState { it.copy(isLoading = false) }
    }

    private suspend fun loadTags() {
        tagRepo.getTags().fold(
            onSuccess = { tags ->
                updateState { it.copy(tags = tags.sortedBy { tag -> tag.name.lowercase() }) }
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun loadProjectIfNeeded() {
        val editId = projectId ?: return

        projectRepo.getProjectById(editId).fold(
            onSuccess = { loadedProject ->
                updateState {
                    it.copy(
                        project = loadedProject,
                        title = loadedProject.title,
                        shortDescription = loadedProject.shortDescription,
                        description = loadedProject.description,
                        imageAltText = loadedProject.image?.altText.orEmpty(),
                        bannerImageAltText = loadedProject.bannerImage?.altText.orEmpty(),
                        selectedTagIds = loadedProject.tags.map { tag -> tag.id },
                        linksInput = loadedProject.links.joinToString(", ") { link -> link.url },
                        imagePreviewUrl = loadedProject.image?.url,
                        bannerImagePreviewUrl = loadedProject.bannerImage?.url
                    )
                }
            },
            onError = { error ->
                ToastManager.error(error.message)
                emitEffect(ManageProjectsContract.Effect.NavigateBackToProjects)
            }
        )
    }

    private fun toggleTag(tagId: String) {
        updateState { currentState ->
            val updatedTags = if (currentState.selectedTagIds.contains(tagId)) {
                currentState.selectedTagIds - tagId
            } else {
                currentState.selectedTagIds + tagId
            }

            currentState.copy(selectedTagIds = updatedTags)
        }
    }

    private fun updateImageFile(file: org.w3c.files.File) {
        state.value.imagePreviewUrl?.let(URL::revokeObjectURL)
        updateState {
            it.copy(
                imageFile = file,
                imagePreviewUrl = URL.createObjectURL(file)
            )
        }
    }

    private fun updateBannerImageFile(file: org.w3c.files.File) {
        state.value.bannerImagePreviewUrl?.let(URL::revokeObjectURL)
        updateState {
            it.copy(
                bannerImageFile = file,
                bannerImagePreviewUrl = URL.createObjectURL(file)
            )
        }
    }

    private suspend fun saveProject() {
        val currentState = state.value
        updateState { it.copy(hasAttemptedSave = true) }

        if (!currentState.areRequiredFieldsValid) {
            ToastManager.warning("Please complete all required fields before saving.")
            return
        }

        val links = parseLinks(currentState.linksInput)
        val invalidLinks = links.filterNot(::isValidUrl)
        if (invalidLinks.isNotEmpty()) {
            ToastManager.warning("Invalid links: ${invalidLinks.joinToString(", ")}")
            return
        }

        updateState { it.copy(isSaving = true) }

        val normalizedTagIds = currentState.selectedTagIds.distinct()
        val normalizedLinks = links
        val updatePayload = UpdateProject(
            title = currentState.title.trim(),
            shortDescription = currentState.shortDescription.trim(),
            description = currentState.description.trim(),
            imageAltText = currentState.imageAltText.trim(),
            bannerImageAltText = currentState.bannerImageAltText.trim(),
            tags = normalizedTagIds,
            links = normalizedLinks
        )

        val saveResult = if (projectId != null) {
            if (isProjectUnchanged(currentState, normalizedTagIds, normalizedLinks)) {
                updateState { it.copy(isSaving = false) }
                emitEffect(ManageProjectsContract.Effect.NavigateBackToProjects)
                return
            }

            projectRepo.updateProject(
                id = projectId,
                update = updatePayload,
                image = currentState.imageFile,
                bannerImage = currentState.bannerImageFile
            )
        } else {
            val image = currentState.imageFile ?: run {
                updateState { it.copy(isSaving = false) }
                ToastManager.warning("Please upload an image.")
                return
            }
            val bannerImage = currentState.bannerImageFile ?: run {
                updateState { it.copy(isSaving = false) }
                ToastManager.warning("Please upload a banner image.")
                return
            }

            projectRepo.insertProject(
                project = InsertProject(
                    title = currentState.title.trim(),
                    shortDescription = currentState.shortDescription.trim(),
                    description = currentState.description.trim(),
                    imageAltText = currentState.imageAltText.trim(),
                    bannerImageAltText = currentState.bannerImageAltText.trim(),
                    tags = normalizedTagIds,
                    links = normalizedLinks
                ),
                image = image,
                bannerImage = bannerImage
            )
        }

        saveResult.fold(
            onSuccess = {
                val successMessage = if (projectId != null) {
                    "Project updated successfully."
                } else {
                    "Project created successfully."
                }
                ToastManager.success(successMessage)
                emitEffect(ManageProjectsContract.Effect.NavigateBackToProjects)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )

        updateState { it.copy(isSaving = false) }
    }

    private fun isProjectUnchanged(
        currentState: ManageProjectsContract.State,
        normalizedTagIds: List<String>,
        normalizedLinks: List<String>
    ): Boolean {
        val existingProject = currentState.project ?: return false

        return existingProject.title == currentState.title.trim() &&
                existingProject.shortDescription == currentState.shortDescription.trim() &&
                existingProject.description == currentState.description.trim() &&
                existingProject.image?.altText.orEmpty() == currentState.imageAltText.trim() &&
                existingProject.bannerImage?.altText.orEmpty() == currentState.bannerImageAltText.trim() &&
                existingProject.tags.map { it.id }.sorted() == normalizedTagIds.sorted() &&
                existingProject.links.map { it.url.trim() } == normalizedLinks &&
                currentState.imageFile == null &&
                currentState.bannerImageFile == null
    }

    private suspend fun deleteProject() {
        val id = projectId ?: return
        updateState { it.copy(isDeleting = true) }

        projectRepo.deleteProject(id).fold(
            onSuccess = {
                ToastManager.success("Project deleted successfully.")
                updateState { it.copy(showDeleteDialog = false) }
                emitEffect(ManageProjectsContract.Effect.NavigateBackToProjects)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )

        updateState { it.copy(isDeleting = false) }
    }

    private fun parseLinks(linksInput: String): List<String> =
        linksInput
            .split(",", "\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()

    private fun isValidUrl(url: String): Boolean {
        val lowered = url.lowercase()
        return lowered.startsWith("http://") || lowered.startsWith("https://")
    }

    override fun emitEffect(effect: ManageProjectsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageProjectsContract.State) -> ManageProjectsContract.State) {
        _state.update(block)
    }
}
