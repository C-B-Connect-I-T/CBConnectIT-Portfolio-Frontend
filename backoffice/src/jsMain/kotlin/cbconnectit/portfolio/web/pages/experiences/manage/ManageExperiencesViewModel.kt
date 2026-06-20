package cbconnectit.portfolio.web.pages.experiences.manage

import cbconnectit.portfolio.web.data.models.dto.requests.experience.InsertExperience
import cbconnectit.portfolio.web.data.models.dto.requests.experience.UpdateExperience
import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.CompanyRepo
import cbconnectit.portfolio.web.data.repos.ExperienceRepo
import cbconnectit.portfolio.web.data.repos.JobPositionRepo
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atTime

class ManageExperiencesViewModel(
    private val experienceId: String?,
    private val experienceRepo: ExperienceRepo = ExperienceRepo,
    private val companyRepo: CompanyRepo = CompanyRepo,
    private val jobPositionRepo: JobPositionRepo = JobPositionRepo,
    private val tagRepo: TagRepo = TagRepo
) : ViewModel(), ManageExperiencesContract {
    private val _state = MutableStateFlow(ManageExperiencesContract.State())
    override val state: StateFlow<ManageExperiencesContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ManageExperiencesContract.Effect>()
    override val effect: SharedFlow<ManageExperiencesContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ManageExperiencesContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ManageExperiencesContract.Intent.LoadInitialData -> loadInitialData()
            is ManageExperiencesContract.Intent.UpdateShortDescription -> updateState { it.copy(shortDescription = intent.shortDescription) }
            is ManageExperiencesContract.Intent.UpdateDescription -> updateState { it.copy(description = intent.description) }
            is ManageExperiencesContract.Intent.UpdateFrom -> updateState { it.copy(from = intent.from) }
            is ManageExperiencesContract.Intent.UpdateTo -> updateState { it.copy(to = intent.to) }
            is ManageExperiencesContract.Intent.ToggleFreelance -> updateState { it.copy(asFreelance = !it.asFreelance) }
            is ManageExperiencesContract.Intent.UpdateCompanyId -> updateState { it.copy(companyId = intent.companyId) }
            is ManageExperiencesContract.Intent.UpdateJobPositionId -> updateState { it.copy(jobPositionId = intent.jobPositionId) }
            is ManageExperiencesContract.Intent.ToggleTagId -> toggleTag(intent.tagId)
            is ManageExperiencesContract.Intent.SaveExperience -> saveExperience()
            is ManageExperiencesContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageExperiencesContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageExperiencesContract.Intent.ConfirmDelete -> deleteExperience()
            is ManageExperiencesContract.Intent.Cancel -> emitEffect(ManageExperiencesContract.Effect.NavigateBackToExperiences)
        }
    }

    private suspend fun loadInitialData() {
        updateState { it.copy(isLoading = true) }

        loadCompanies()
        loadJobPositions()
        loadTags()
        loadExperienceIfNeeded()

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

    private suspend fun loadExperienceIfNeeded() {
        if (experienceId == null) return

        experienceRepo.getExperienceById(experienceId).fold(
            onSuccess = { experience ->
                updateState {
                    it.copy(
                        experience = experience,
                        shortDescription = experience.shortDescription,
                        description = experience.description,
                        from = toDateInput(experience.from),
                        to = toDateInput(experience.to),
                        asFreelance = experience.asFreelance,
                        companyId = experience.company.id,
                        jobPositionId = experience.jobPosition.id,
                        selectedTagIds = experience.tags.map { tag -> tag.id }
                    )
                }
            },
            onError = { error ->
                ToastManager.error(error.message)
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

    private suspend fun saveExperience() {
        val currentState = state.value
        updateState { it.copy(hasAttemptedSave = true) }

        if (!currentState.areRequiredFieldsValid) {
            ToastManager.warning("Vul alle verplichte velden in voor de ervaring")
            return
        }

        val fromDate = currentState.from.trim().let(LocalDate::parse)
        val toDate = currentState.to.trim().let(LocalDate::parse)

        if (fromDate > toDate) {
            ToastManager.warning("De startdatum moet voor of gelijk zijn aan de einddatum")
            return
        }

        val normalizedFrom = fromDate.atTime(0, 0, 0).toString()
        val normalizedTo = toDate.atTime(0,0,0).toString()

        updateState { it.copy(isSaving = true) }
        if (experienceId != null && isExperienceUnchanged(currentState, normalizedFrom, normalizedTo)) {
            updateState { it.copy(isSaving = false) }
            emitEffect(ManageExperiencesContract.Effect.NavigateBackToExperiences)
            return
        }

        val saveResult = if (experienceId != null) {
            experienceRepo.updateExperience(
                experienceId,
                UpdateExperience(
                    shortDescription = currentState.shortDescription.trim(),
                    description = currentState.description.trim(),
                    from = normalizedFrom,
                    to = normalizedTo,
                    asFreelance = currentState.asFreelance,
                    tags = currentState.selectedTagIds.ifEmpty { null },
                    companyId = currentState.companyId,
                    jobPositionId = currentState.jobPositionId
                )
            )
        } else {
            experienceRepo.insertExperience(
                InsertExperience(
                    shortDescription = currentState.shortDescription.trim(),
                    description = currentState.description.trim(),
                    from = normalizedFrom,
                    to = normalizedTo,
                    asFreelance = currentState.asFreelance,
                    tags = currentState.selectedTagIds.ifEmpty { null },
                    companyId = currentState.companyId,
                    jobPositionId = currentState.jobPositionId
                )
            )
        }

        saveResult.fold(
            onSuccess = {
                val successMessage = if (experienceId != null) {
                    "Ervaring succesvol bijgewerkt"
                } else {
                    "Ervaring succesvol aangemaakt"
                }
                ToastManager.success(successMessage)
                emitEffect(ManageExperiencesContract.Effect.NavigateBackToExperiences)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isSaving = false) }
    }

    private fun isExperienceUnchanged(
        currentState: ManageExperiencesContract.State,
        normalizedFrom: String,
        normalizedTo: String
    ): Boolean {
        val currentExperience = currentState.experience ?: return false
        val initialTagIds = currentExperience.tags.map { it.id }

        return currentExperience.shortDescription == currentState.shortDescription.trim() &&
                currentExperience.description == currentState.description.trim() &&
                extractDatePart(currentExperience.from) == extractDatePart(normalizedFrom) &&
                extractDatePart(currentExperience.to) == extractDatePart(normalizedTo) &&
                currentExperience.asFreelance == currentState.asFreelance &&
                currentExperience.company.id == currentState.companyId &&
                currentExperience.jobPosition.id == currentState.jobPositionId &&
                initialTagIds == currentState.selectedTagIds
    }

    private suspend fun deleteExperience() {
        if (experienceId == null) return

        updateState { it.copy(isDeleting = true) }
        experienceRepo.deleteExperience(experienceId).fold(
            onSuccess = {
                ToastManager.success("Ervaring succesvol verwijderd")
                updateState { it.copy(showDeleteDialog = false) }
                emitEffect(ManageExperiencesContract.Effect.NavigateBackToExperiences)
            },
            onError = { error ->
                ToastManager.error(error.message)
            }
        )
        updateState { it.copy(isDeleting = false) }
    }

    private fun toDateInput(value: String): String {
        if (value.isBlank()) return ""
        return extractDatePart(value)
    }

    private fun extractDatePart(value: String): String =
        value.substringBefore("T").trim()

    override fun emitEffect(effect: ManageExperiencesContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageExperiencesContract.State) -> ManageExperiencesContract.State) {
        _state.update(block)
    }
}
