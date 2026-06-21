package cbconnectit.portfolio.web.pages.testimonials.manage

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
            is ManageTestimonialsContract.Intent.UpdateImageUrl -> updateState { it.copy(imageUrl = intent.imageUrl) }
            is ManageTestimonialsContract.Intent.UpdateReview -> updateState { it.copy(review = intent.review) }
            is ManageTestimonialsContract.Intent.UpdateCompanyId -> updateState { it.copy(companyId = intent.companyId) }
            is ManageTestimonialsContract.Intent.UpdateJobPositionId -> updateState { it.copy(jobPositionId = intent.jobPositionId) }
            is ManageTestimonialsContract.Intent.SaveTestimonial -> saveTestimonial()
            is ManageTestimonialsContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageTestimonialsContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageTestimonialsContract.Intent.ConfirmDelete -> deleteTestimonial()
            is ManageTestimonialsContract.Intent.Cancel -> emitEffect(ManageTestimonialsContract.Effect.NavigateBackToTestimonials)
        }
    }

    private suspend fun loadInitialData() {
        updateState { it.copy(isLoading = true) }

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
                        imageUrl = testimonial.imageUrl,
                        review = testimonial.review,
                        companyId = testimonial.company?.id.orEmpty(),
                        jobPositionId = testimonial.jobPosition.id
                    )
                }
            },
            onError = { error ->
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

        val normalizedImageUrl = currentState.imageUrl.trim()
        if (!isHttpUrl(normalizedImageUrl)) {
            ToastManager.warning("Afbeelding URL moet starten met http:// of https://")
            return
        }

        updateState { it.copy(isSaving = true) }
        if (testimonialId != null && isTestimonialUnchanged(currentState, normalizedImageUrl)) {
            updateState { it.copy(isSaving = false) }
            emitEffect(ManageTestimonialsContract.Effect.NavigateBackToTestimonials)
            return
        }

        val saveResult = if (testimonialId != null) {
            testimonialRepo.updateTestimonial(
                testimonialId,
                UpdateTestimonial(
                    imageUrl = normalizedImageUrl,
                    review = currentState.review.trim(),
                    fullName = currentState.fullName.trim(),
                    companyId = currentState.companyId.trim(),
                    jobPositionId = currentState.jobPositionId.trim()
                )
            )
        } else {
            testimonialRepo.insertTestimonial(
                InsertTestimonial(
                    imageUrl = normalizedImageUrl,
                    review = currentState.review.trim(),
                    fullName = currentState.fullName.trim(),
                    companyId = currentState.companyId.trim(),
                    jobPositionId = currentState.jobPositionId.trim()
                )
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

    private fun isTestimonialUnchanged(
        currentState: ManageTestimonialsContract.State,
        normalizedImageUrl: String
    ): Boolean {
        val currentTestimonial = currentState.testimonial ?: return false
        return currentTestimonial.fullName == currentState.fullName.trim() &&
            currentTestimonial.imageUrl == normalizedImageUrl &&
            currentTestimonial.review == currentState.review.trim() &&
            currentTestimonial.company?.id.orEmpty() == currentState.companyId.trim() &&
            currentTestimonial.jobPosition.id == currentState.jobPositionId.trim()
    }

    private fun isHttpUrl(value: String): Boolean =
        value.startsWith("http://", ignoreCase = true) || value.startsWith("https://", ignoreCase = true)

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

    override fun emitEffect(effect: ManageTestimonialsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageTestimonialsContract.State) -> ManageTestimonialsContract.State) {
        _state.update(block)
    }
}
