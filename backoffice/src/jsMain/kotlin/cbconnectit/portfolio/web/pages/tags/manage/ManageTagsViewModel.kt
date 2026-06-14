package cbconnectit.portfolio.web.pages.tags.manage

import cbconnectit.portfolio.web.data.models.dto.requests.tag.InsertTag
import cbconnectit.portfolio.web.data.models.dto.requests.tag.UpdateTag
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

class ManageTagsViewModel(
    private val tagId: String?,
    private val tagsRepo: TagRepo = TagRepo
) : ViewModel(), ManageTagsContract {
    private val _state = MutableStateFlow(ManageTagsContract.State())
    override val state: StateFlow<ManageTagsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ManageTagsContract.Effect>()
    override val effect: SharedFlow<ManageTagsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: ManageTagsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is ManageTagsContract.Intent.LoadInitialData -> loadTagIfNeeded()
            is ManageTagsContract.Intent.UpdateName -> updateState { it.copy(name = intent.name) }
            is ManageTagsContract.Intent.SaveTag -> saveTag()
            is ManageTagsContract.Intent.ShowDeleteDialog -> updateState { it.copy(showDeleteDialog = true) }
            is ManageTagsContract.Intent.HideDeleteDialog -> updateState { it.copy(showDeleteDialog = false) }
            is ManageTagsContract.Intent.ConfirmDelete -> deleteTag()
            is ManageTagsContract.Intent.Cancel -> emitEffect(ManageTagsContract.Effect.NavigateBackToTags)
        }
    }

    private suspend fun loadTagIfNeeded() {
        if (tagId == null) return

        updateState { it.copy(isLoading = true) }
        try {
            val tag = tagsRepo.getTagById(tagId)
            updateState {
                it.copy(
                    tag = tag,
                    name = tag.name,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            updateState { it.copy(isLoading = false) }
            ToastManager.error(e.message ?: "Er is een fout opgetreden bij het ophalen van de tag")
        }
    }

    private suspend fun saveTag() {
        val currentState = state.value
        updateState { it.copy(hasAttemptedSave = true) }

        val name = currentState.name.trim()
        if (name.isBlank()) {
            ToastManager.warning("Vul een geldige naam in voor de tag")
            return
        }

        updateState { it.copy(isSaving = true) }
        try {
            if (tagId != null) {
                if (currentState.tag?.name != name) {
                    tagsRepo.updateTag(tagId, UpdateTag(name = name))
                    ToastManager.success("Tag succesvol bijgewerkt")
                }
            } else {
                tagsRepo.insertTag(InsertTag(name = name))
                ToastManager.success("Tag succesvol aangemaakt")
            }
            emitEffect(ManageTagsContract.Effect.NavigateBackToTags)
        } catch (e: Exception) {
            ToastManager.error(e.message ?: "Er is een fout opgetreden bij het opslaan van de tag")
        } finally {
            updateState { it.copy(isSaving = false) }
        }
    }

    private suspend fun deleteTag() {
        if (tagId == null) return

        updateState { it.copy(isDeleting = true) }
        try {
            tagsRepo.deleteTag(tagId)
            ToastManager.success("Tag succesvol verwijderd")
            updateState { it.copy(showDeleteDialog = false) }
            emitEffect(ManageTagsContract.Effect.NavigateBackToTags)
        } catch (e: Exception) {
            ToastManager.error(e.message ?: "Er is een fout opgetreden bij het verwijderen van de tag")
        } finally {
            updateState { it.copy(isDeleting = false) }
        }
    }

    override fun emitEffect(effect: ManageTagsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageTagsContract.State) -> ManageTagsContract.State) {
        _state.update(block)
    }
}
