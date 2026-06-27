package cbconnectit.portfolio.web.admin.pages.testimonials.manage

import cbconnectit.portfolio.web.data.models.dto.requests.testimonial.InsertTestimonial
import cbconnectit.portfolio.web.data.models.dto.requests.testimonial.UpdateTestimonial
import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.CompanyRepo
import cbconnectit.portfolio.web.data.repos.JobPositionRepo
import cbconnectit.portfolio.web.data.repos.TestimonialRepo
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

class ManageTestimonialsViewModel(
    private val testimonialId: String?,
    private val testimonialRepo: TestimonialRepo = TestimonialRepo,
    private val companyRepo: CompanyRepo = CompanyRepo,
    private val jobPositionRepo: JobPositionRepo = JobPositionRepo
) : ViewModel(), ManageTestimonialsContract {
    private val _state = MutableStateFlow(ManageTestimonialsContract.State())
    override val state: StateFlow<ManageTestimonialsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ManageTestimonialsContract.Effect>()
    override val effect: SharedFlow<ManageTestimonialsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ManageTestimonialsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ManageTestimonialsContract.Intent.LoadInitialData -> loadInitialData()
            is ManageTestimonialsContract.Intent.UpdateFullName -> updateState { it.copy(fullName = intent.fullName) }
            is ManageTestimonialsContract.Intent.UpdateReview -> updateState { it.copy(review = intent.review) }
            is ManageTestimonialsContract.Intent.UpdateCompanyId -> updateState { it.copy(companyId = intent.companyId) }
            is ManageTestimonialsContract.Intent.UpdateJobPositionId -> updateState { it.copy(jobPositionId = intent.jobPositionId) }
            is ManageTestimonialsContract.Intent.UpdateAvatarAltText -> updateState { it.copy(avatarAltText = intent.avatarAltText) }
            is ManageTestimonialsContract.Intent.UpdateAvatarFile -> updateState {
                // Revoke previous URL if it exists
                state.value.avatarImageUrl?.let(URL::revokeObjectURL)

                it.copy(
                    avatarImageFile = intent.file,
                    avatarImageUrl = intent.file.let(URL::createObjectURL)
                )
            }

            is ManageTestimonialsContract.Intent.UploadAvatar -> uploadImage(intent.file)
            is ManageTestimonialsContract.Intent.RemoveAvatar -> removeImage()
            is ManageTestimonialsContract.Intent.SaveTestimonial -> saveTestimonial()
            is ManageTestimonialsContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageTestimonialsContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageTestimonialsContract.Intent.ConfirmDelete -> deleteTestimonial()
            is ManageTestimonialsContract.Intent.Cancel -> emitEffect(ManageTestimonialsContract.Effect.NavigateBackToTestimonials)
        }
    }

    private suspend fun loadInitialData() {
        updateState { it.copy(isLoading = true) }

        // TODO: check if all of them are concurrent or serial...
        loadCompanies()
        loadJobPositions()
        loadTestimonialIfNeeded()

        updateState { it.copy(isLoading = false) }
    }

    private suspend fun loadCompanies() {
        companyRepo.getCompanies().fold(
            onSuccess = { companies ->
                updateState { it.copy(companies = companies) }
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun loadJobPositions() {
        jobPositionRepo.getJobPositions().fold(
            onSuccess = { jobPositions ->
                updateState { it.copy(jobPositions = jobPositions) }
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun loadTestimonialIfNeeded() {
        if (testimonialId == null) return

        testimonialRepo.getTestimonialById(testimonialId).fold(
            onSuccess = { testimonial ->
                updateState {
                    it.copy(
                        testimonial = testimonial,
                        fullName = testimonial.fullName,
                        review = testimonial.review,
                        companyId = testimonial.company?.id.orEmpty(),
                        jobPositionId = testimonial.jobPosition.id,
                        avatarAltText = testimonial.avatarImage?.altText ?: ""
                    )
                }
            },
            onError = { error ->
                // TODO: check if we want to Navigate Back to the previous page, or stay on this one?
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun saveTestimonial() {
        val currentState = state.value
        updateState { it.copy(hasAttemptedSave = true) }

        if (!currentState.areRequiredFieldsValid) {
            ToastManager.warning("Vul alle verplichte velden in voor het testimonial")
            return
        }

        updateState { it.copy(isSaving = true) }

        // Shortcircuit by closing this form when nothing has changed and user presses 'save'
        if (testimonialId != null && isTestimonialUnchanged(currentState)) {
            updateState { it.copy(isSaving = false) }
            emitEffect(ManageTestimonialsContract.Effect.NavigateBackToTestimonials)
            return
        }

        val saveResult = if (testimonialId != null) {
            testimonialRepo.updateTestimonial(
                testimonialId,
                UpdateTestimonial(
                    review = currentState.review.trim(),
                    fullName = currentState.fullName.trim(),
                    companyId = currentState.companyId.trim(),
                    jobPositionId = currentState.jobPositionId.trim(),
                    avatarAltText = currentState.avatarAltText.trim(),
                )
            )
        } else {
            testimonialRepo.insertTestimonial(
                InsertTestimonial(
                    review = currentState.review.trim(),
                    fullName = currentState.fullName.trim(),
                    companyId = currentState.companyId.trim(),
                    jobPositionId = currentState.jobPositionId.trim(),
                    avatarAltText = currentState.avatarAltText.trim(),
                ),
                avatarImage = currentState.avatarImageFile
            )
        }

        saveResult.fold(
            onSuccess = {
                val successMessage = if (testimonialId != null) {
                    "Testimonial succesvol bijgewerkt"
                } else {
                    "Testimonial succesvol aangemaakt"
                }
                ToastManager.success(successMessage)
                emitEffect(ManageTestimonialsContract.Effect.NavigateBackToTestimonials)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isSaving = false) }
    }

    // TODO: integrate this in the State itself like we did in Klantenstop
    private fun isTestimonialUnchanged(currentState: ManageTestimonialsContract.State): Boolean {
        val currentTestimonial = currentState.testimonial ?: return false
        return currentTestimonial.fullName == currentState.fullName.trim() &&
                currentTestimonial.review == currentState.review.trim() &&
                currentTestimonial.company?.id.orEmpty() == currentState.companyId.trim() &&
                currentTestimonial.jobPosition.id == currentState.jobPositionId.trim() &&
                currentTestimonial.avatarImage?.altText == currentState.avatarAltText.trim()
    }

    private suspend fun deleteTestimonial() {
        if (testimonialId == null) return

        updateState { it.copy(isDeleting = true) }
        testimonialRepo.deleteTestimonial(testimonialId).fold(
            onSuccess = {
                ToastManager.success("Testimonial succesvol verwijderd")
                updateState { it.copy(showDeleteDialog = false) }
                emitEffect(ManageTestimonialsContract.Effect.NavigateBackToTestimonials)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isDeleting = false) }
    }

    private suspend fun uploadImage(file: org.w3c.files.File) {
        val id = testimonialId ?: return
        updateState { it.copy(isImageLoading = true) }

        // TODO: this is a weird flow... The user is not aware that this is required when just updating the image... Maybe have the image updated only when pressing on 'save'?
        testimonialRepo.updateTestimonialAvatar(id, file, state.value.avatarAltText).fold(
            onSuccess = { updated ->
                // Revoke previous URL if it exists
                state.value.avatarImageUrl?.let(URL::revokeObjectURL)

                updateState { it.copy(testimonial = updated, isImageLoading = false) }
            },
            onError = { error ->
                updateState { it.copy(isImageLoading = false) }
                ToastManager.error(error.message)
            }
        )
    }

    private suspend fun removeImage() {
        val id = testimonialId ?: return
        updateState { it.copy(isImageLoading = true) }

        testimonialRepo.deleteTestimonialAvatar(id).fold(
            onSuccess = { updated ->
                // Revoke previous URL if it exists
                state.value.avatarImageUrl?.let(URL::revokeObjectURL)

                updateState { it.copy(testimonial = updated, isImageLoading = false) }
            },
            onError = { error ->
                updateState { it.copy(isImageLoading = false) }
                ToastManager.error(error.message)
            }
        )
    }

    override fun emitEffect(effect: ManageTestimonialsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageTestimonialsContract.State) -> ManageTestimonialsContract.State) {
        _state.update(block)
    }
}
