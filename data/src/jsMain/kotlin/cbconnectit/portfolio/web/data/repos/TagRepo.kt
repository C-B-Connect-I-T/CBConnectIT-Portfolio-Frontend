package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoErrorKind
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.data.models.domain.toTag
import cbconnectit.portfolio.web.data.models.dto.requests.tag.InsertTag
import cbconnectit.portfolio.web.data.models.dto.requests.tag.UpdateTag
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.TagDto
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest

object TagRepo {
    private val tagUrl = "${NetworkingConfig.baseUrl}/api/v1/tags"

    suspend fun getTags(): RepoResult<List<Tag>> {
        val response: NetworkResponse<List<TagDto>, ErrorResponse> = getRequest(tagUrl)

        return response.toRepoResult(
            successMapper = { tagDtos -> tagDtos.map { it.toTag() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van tags",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van tags"
        )
    }

    suspend fun getTagById(id: String): RepoResult<Tag> {
        val response: NetworkResponse<TagDto, ErrorResponse> = getRequest("$tagUrl/$id")

        return response.toRepoResult(
            successMapper = { tagDto -> tagDto.toTag() },
            defaultServerErrorMessage = "Server fout bij het ophalen van tag",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van tag"
        )
    }

    suspend fun insertTag(tag: InsertTag): RepoResult<Tag> {
        val response: NetworkResponse<TagDto, ErrorResponse> = postRequest(resource = tagUrl, body = tag)

        return response.toRepoResult(
            successMapper = { tagDto -> tagDto.toTag() },
            defaultServerErrorMessage = "Server fout bij het aanmaken van tag",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het aanmaken van tag"
        )
    }

    suspend fun updateTag(id: String, update: UpdateTag): RepoResult<Tag> {
        val response: NetworkResponse<TagDto, ErrorResponse> = putRequest(resource = "$tagUrl/$id", body = update)

        return response.toRepoResult(
            successMapper = { tagDto -> tagDto.toTag() },
            defaultServerErrorMessage = "Server fout bij het bijwerken van tag",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het bijwerken van tag"
        )
    }

    suspend fun deleteTag(id: String): RepoResult<Unit> {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$tagUrl/$id")

        return response.toRepoResult(
            successMapper = { },
            defaultServerErrorMessage = "Server fout bij het verwijderen van tag",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het verwijderen van tag"
        )
    }
}
