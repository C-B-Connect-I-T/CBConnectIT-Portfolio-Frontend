package cbconnectit.portfolio.web.admin.pages.services.manage

import cbconnectit.portfolio.web.data.models.dto.requests.service.InsertService
import cbconnectit.portfolio.web.data.models.dto.requests.service.UpdateService
import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.ServiceRepo
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

class ManageServicesViewModel(
    private val serviceId: String?,
    private val serviceRepo: ServiceRepo = ServiceRepo,
    private val tagRepo: TagRepo = TagRepo
) : ViewModel(), ManageServicesContract {
    private val _state = MutableStateFlow(ManageServicesContract.State())
    override val state: StateFlow<ManageServicesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ManageServicesContract.Effect>()
    override val effect: SharedFlow<ManageServicesContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ManageServicesContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ManageServicesContract.Intent.LoadInitialData -> loadInitialData()
            is ManageServicesContract.Intent.UpdateTitle -> updateState { it.copy(title = intent.title) }
            is ManageServicesContract.Intent.UpdateShortDescription -> updateState { it.copy(shortDescription = intent.shortDescription) }
            is ManageServicesContract.Intent.UpdateDescription -> updateState { it.copy(description = intent.description) }
            is ManageServicesContract.Intent.UpdateImageAltText -> updateState { it.copy(imageAltText = intent.imageAltText) }
            is ManageServicesContract.Intent.UpdateBannerImageAltText -> updateState { it.copy(bannerImageAltText = intent.bannerImageAltText) }
            is ManageServicesContract.Intent.UpdateBannerDescription -> updateState { it.copy(bannerDescription = intent.bannerDescription) }
            is ManageServicesContract.Intent.UpdateExtraInfo -> updateState { it.copy(extraInfo = intent.extraInfo) }
            is ManageServicesContract.Intent.UpdateParentServiceId -> updateParentServiceId(intent.parentServiceId)
            is ManageServicesContract.Intent.UpdateTagId -> updateState { it.copy(tagId = intent.tagId) }
            is ManageServicesContract.Intent.UpdateImageFile -> updateImageFile(intent.file)
            is ManageServicesContract.Intent.UpdateBannerImageFile -> updateBannerImageFile(intent.file)
            is ManageServicesContract.Intent.RemoveBannerImage -> removeBannerImage()
            is ManageServicesContract.Intent.SaveService -> saveService()
            is ManageServicesContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageServicesContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageServicesContract.Intent.ConfirmDelete -> deleteService()
            is ManageServicesContract.Intent.Cancel -> emitEffect(ManageServicesContract.Effect.NavigateBackToServices)
        }
    }

    private suspend fun loadInitialData() {
        updateState { it.copy(isLoading = true) }

        loadServices()
        loadTags()
        loadServiceIfNeeded()

        updateState { it.copy(isLoading = false) }
    }

    private suspend fun loadServices() {
        serviceRepo.getServices().fold(
            onSuccess = { services ->
                updateState { it.copy(allServices = services) }
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun loadTags() {
        tagRepo.getTags().fold(
            onSuccess = { tags ->
                updateState { it.copy(tags = tags) }
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun loadServiceIfNeeded() {
        val editId = serviceId ?: return

        serviceRepo.getServiceById(editId).fold(
            onSuccess = { loadedService ->
                if (loadedService == null) {
                    ToastManager.error("Service not found.")
                    emitEffect(ManageServicesContract.Effect.NavigateBackToServices)
                    return@fold
                }

                val inferredParentServiceId = inferParentServiceId(
                    serviceId = loadedService.id,
                    allServices = state.value.allServices
                )

                updateState {
                    it.copy(
                        service = loadedService,
                        title = loadedService.title,
                        shortDescription = loadedService.shortDescription.orEmpty(),
                        description = loadedService.description,
                        imagePreviewUrl = loadedService.image?.url,
                        imageAltText = loadedService.image?.altText.orEmpty(),
                        bannerImagePreviewUrl = loadedService.bannerImage?.url,
                        bannerImageAltText = loadedService.bannerImage?.altText.orEmpty(),
                        bannerDescription = loadedService.bannerDescription.orEmpty(),
                        extraInfo = loadedService.extraInfo.orEmpty(),
                        parentServiceId = inferredParentServiceId,
                        initialParentServiceId = inferredParentServiceId,
                        tagId = loadedService.tag?.id
                    )
                }
            },
            onError = { error ->
                ToastManager.error(error.message)
                emitEffect(ManageServicesContract.Effect.NavigateBackToServices)
            }
        )
    }

    private fun updateParentServiceId(parentServiceId: String?) {
        val currentState = state.value
        val normalizedParentServiceId = normalizeNullable(parentServiceId)

        if (currentState.hasChildren && normalizedParentServiceId != null) {
            ToastManager.warning("Services with sub-services cannot be assigned a parent service.")
            return
        }

        updateState { it.copy(parentServiceId = normalizedParentServiceId) }
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
                bannerImagePreviewUrl = URL.createObjectURL(file),
                shouldRemoveBannerImage = false
            )
        }
    }

    private fun removeBannerImage() {
        state.value.bannerImagePreviewUrl?.let(URL::revokeObjectURL)
        updateState {
            it.copy(
                bannerImageFile = null,
                bannerImagePreviewUrl = null,
                bannerImageAltText = "",
                shouldRemoveBannerImage = true
            )
        }
    }

    private suspend fun saveService() {
        val currentState = state.value
        updateState { it.copy(hasAttemptedSave = true) }

        if (!currentState.areRequiredFieldsValid) {
            ToastManager.warning("Please complete all required fields before saving.")
            return
        }

        val normalizedParentServiceId = normalizeNullable(currentState.parentServiceId)
        if (currentState.hasChildren && normalizedParentServiceId != null) {
            ToastManager.warning("Services with sub-services cannot be assigned a parent service.")
            return
        }

        updateState { it.copy(isSaving = true) }

        val updatePayload = UpdateService(
            title = currentState.title.trim(),
            shortDescription = currentState.shortDescription.trim(),
            description = currentState.description.trim(),
            imageAltText = currentState.imageAltText.trim(),
            bannerImageAltText = if (currentState.hasBannerImageInFinalState) {
                normalizeNullable(currentState.bannerImageAltText)
            } else {
                null
            },
            removeBannerImage = currentState.shouldRemoveBannerImage,
            bannerDescription = normalizeNullable(currentState.bannerDescription),
            extraInfo = normalizeNullable(currentState.extraInfo),
            parentServiceId = normalizedParentServiceId,
            tagId = normalizeNullable(currentState.tagId)
        )

        val saveResult = if (serviceId != null) {
            if (isServiceUnchanged(currentState)) {
                updateState { it.copy(isSaving = false) }
                emitEffect(ManageServicesContract.Effect.NavigateBackToServices)
                return
            }

            serviceRepo.updateService(
                id = serviceId,
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

            serviceRepo.insertService(
                service = InsertService(
                    title = currentState.title.trim(),
                    shortDescription = currentState.shortDescription.trim(),
                    description = currentState.description.trim(),
                    imageAltText = currentState.imageAltText.trim(),
                    bannerImageAltText = if (currentState.hasBannerImageInFinalState) {
                        normalizeNullable(currentState.bannerImageAltText)
                    } else {
                        null
                    },
                    bannerDescription = normalizeNullable(currentState.bannerDescription),
                    extraInfo = normalizeNullable(currentState.extraInfo),
                    parentServiceId = normalizedParentServiceId,
                    tagId = normalizeNullable(currentState.tagId)
                ),
                image = image,
                bannerImage = currentState.bannerImageFile
            )
        }

        saveResult.fold(
            onSuccess = {
                val successMessage = if (serviceId != null) {
                    "Service updated successfully."
                } else {
                    "Service created successfully."
                }
                ToastManager.success(successMessage)
                emitEffect(ManageServicesContract.Effect.NavigateBackToServices)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )

        updateState { it.copy(isSaving = false) }
    }

    private fun isServiceUnchanged(currentState: ManageServicesContract.State): Boolean {
        val existingService = currentState.service ?: return false

        return existingService.title == currentState.title.trim() &&
                existingService.shortDescription.orEmpty() == currentState.shortDescription.trim() &&
                existingService.description == currentState.description.trim() &&
                existingService.image?.altText.orEmpty() == currentState.imageAltText.trim() &&
                normalizeNullable(existingService.bannerImage?.altText) ==
                (if (currentState.hasBannerImageInFinalState) normalizeNullable(currentState.bannerImageAltText) else null) &&
                normalizeNullable(existingService.bannerDescription) == normalizeNullable(currentState.bannerDescription) &&
                normalizeNullable(existingService.extraInfo) == normalizeNullable(currentState.extraInfo) &&
                normalizeNullable(currentState.initialParentServiceId) == normalizeNullable(currentState.parentServiceId) &&
                normalizeNullable(existingService.tag?.id) == normalizeNullable(currentState.tagId) &&
                currentState.imageFile == null &&
                currentState.bannerImageFile == null &&
                !currentState.shouldRemoveBannerImage
    }

    private suspend fun deleteService() {
        val id = serviceId ?: return
        updateState { it.copy(isDeleting = true) }

        serviceRepo.deleteService(id).fold(
            onSuccess = {
                ToastManager.success("Service deleted successfully.")
                updateState { it.copy(showDeleteDialog = false) }
                emitEffect(ManageServicesContract.Effect.NavigateBackToServices)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )

        updateState { it.copy(isDeleting = false) }
    }

    private fun inferParentServiceId(
        serviceId: String,
        allServices: List<cbconnectit.portfolio.web.data.models.domain.Service>
    ): String? {
        return allServices.firstOrNull { candidateParent ->
            candidateParent.subServices.orEmpty().any { it.id == serviceId }
        }?.id
    }

    private fun normalizeNullable(value: String?): String? {
        val trimmed = value?.trim().orEmpty()
        return trimmed.ifBlank { null }
    }

    override fun emitEffect(effect: ManageServicesContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageServicesContract.State) -> ManageServicesContract.State) {
        _state.update(block)
    }
}
