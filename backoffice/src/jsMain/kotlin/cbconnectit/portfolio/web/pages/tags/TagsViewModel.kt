package cbconnectit.portfolio.web.pages.tags

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

class TagsViewModel(
    private val tagsRepo: TagRepo = TagRepo
) : ViewModel(), TagsContract {
    private val _state = MutableStateFlow(TagsContract.State())
    override val state: StateFlow<TagsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TagsContract.Effect>()
    override val effect: SharedFlow<TagsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: TagsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is TagsContract.Intent.LoadTags -> loadTags()
            is TagsContract.Intent.NavigateToManage -> emitEffect(TagsContract.Effect.NavigateToManage(intent.tagId))
        }
    }

    private suspend fun loadTags() {
        updateState { it.copy(isLoading = true) }

        try {
            val tags = tagsRepo.getTags()
            updateState { it.copy(tags = tags, isLoading = false) }
        } catch (e: Exception) {
            updateState { it.copy(isLoading = false) }
            ToastManager.error(e.message ?: "Er is een fout opgetreden bij het ophalen van tags")
        }
    }

    override fun emitEffect(effect: TagsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (TagsContract.State) -> TagsContract.State) {
        _state.update(block)
    }
}
