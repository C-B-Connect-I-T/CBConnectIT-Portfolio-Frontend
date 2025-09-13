package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.data.models.domain.toTag
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.TagDto

object TagRepo {
    private val tagUrl = "${NetworkingConfig.baseUrl}/api/v1/tags"

    suspend fun getTags(): List<Tag> {
        val response: NetworkResponse<List<TagDto>, ErrorResponse> = getRequest(tagUrl, allowUnauthenticated = true)

        return when (response) {
            is NetworkResponse.Success -> response.body.map { it.toTag() }
            else -> emptyList()
        }
    }
}
