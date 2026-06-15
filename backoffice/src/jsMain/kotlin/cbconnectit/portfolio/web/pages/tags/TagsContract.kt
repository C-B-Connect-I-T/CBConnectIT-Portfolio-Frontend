package cbconnectit.portfolio.web.pages.tags

import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.utils.MVI

interface TagsContract : MVI<TagsContract.State, TagsContract.Intent, TagsContract.Effect> {

    data class State(
        val tags: List<Tag> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data object LoadTags : Intent()
        data class NavigateToManage(val tagId: String? = null) : Intent()
    }

    sealed class Effect {
        data class NavigateToManage(val tagId: String? = null) : Effect()
    }
}
