package cbconnectit.portfolio.web.pages.tags.manage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cbconnectit.portfolio.web.components.layout.AdminPageLayout
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.data.repos.TagRepo
import cbconnectit.portfolio.web.navigation.authenticatedGuard
import cbconnectit.portfolio.web.utils.MVI
import cbconnectit.portfolio.web.utils.ViewModel
import cbconnectit.portfolio.web.utils.rememberViewModel
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Page("/admin/tags/manage")
@Composable
fun AdminManageTagsPage() = authenticatedGuard {
    val ctx = rememberPageContext()
    val categoryId = ctx.route.params["id"]
    val viewModel = rememberViewModel(cached = false) { ManageTagsViewModel(categoryId) }
    val state by viewModel.state.collectAsState()
    val isEdit = categoryId != null

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                else -> Unit
            }
        }
    }

    AdminPageLayout(
        title = if (isEdit) "Categorie bewerken" else "Categorie aanmaken"
    ) {

    }
}

interface ManageTagsContract : MVI<ManageTagsContract.State, ManageTagsContract.Intent, ManageTagsContract.Effect> {

    data class State(
        val tag: Tag? = null,
        val isLoading: Boolean = false
    )

    sealed class Intent

    sealed class Effect
}

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
            else -> Unit
        }
    }

    override fun emitEffect(effect: ManageTagsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (ManageTagsContract.State) -> ManageTagsContract.State) {
        _state.update(block)
    }
}