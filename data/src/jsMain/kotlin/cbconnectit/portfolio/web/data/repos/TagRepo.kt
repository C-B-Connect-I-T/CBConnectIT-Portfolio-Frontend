package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
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

    suspend fun getTags(): List<Tag> {
        val response: NetworkResponse<List<TagDto>, ErrorResponse> = getRequest(tagUrl)

        return when (response) {
            is NetworkResponse.Success -> response.body.map { it.toTag() }
            is NetworkResponse.ServerError -> {
                val errorMessage = response.body?.errorDescription ?: response.body?.error ?: "Server fout bij het ophalen van tags"
                throw Exception(errorMessage)
            }

            is NetworkResponse.NetworkError -> throw Exception("Netwerkfout: controleer je internetverbinding")
            is NetworkResponse.UnknownError -> throw Exception("Onbekende fout bij het ophalen van tags")
        }
    }

    suspend fun getTagById(id: String): Tag {
        val response: NetworkResponse<TagDto, ErrorResponse> = getRequest("$tagUrl/$id")

        return when (response) {
            is NetworkResponse.Success -> response.body.toTag()
            is NetworkResponse.ServerError -> {
                val errorMessage = response.body?.errorDescription ?: response.body?.error ?: "Server fout bij het ophalen van tag"
                throw Exception(errorMessage)
            }

            is NetworkResponse.NetworkError -> throw Exception("Netwerkfout: controleer je internetverbinding")
            is NetworkResponse.UnknownError -> throw Exception("Onbekende fout bij het ophalen van tag")
        }
    }

    suspend fun insertTag(tag: InsertTag): Tag {
        val response: NetworkResponse<TagDto, ErrorResponse> = postRequest(resource = tagUrl, body = tag)

        return when (response) {
            is NetworkResponse.Success -> response.body.toTag()
            is NetworkResponse.ServerError -> {
                val errorMessage = response.body?.errorDescription ?: response.body?.error ?: "Server fout bij het aanmaken van tag"
                throw Exception(errorMessage)
            }

            is NetworkResponse.NetworkError -> throw Exception("Netwerkfout: controleer je internetverbinding")
            is NetworkResponse.UnknownError -> throw Exception("Onbekende fout bij het aanmaken van tag")
        }
    }

    suspend fun updateTag(id: String, update: UpdateTag): Tag {
        val response: NetworkResponse<TagDto, ErrorResponse> = putRequest(resource = "$tagUrl/$id", body = update)

        return when (response) {
            is NetworkResponse.Success -> response.body.toTag()
            is NetworkResponse.ServerError -> {
                val errorMessage = response.body?.errorDescription ?: response.body?.error ?: "Server fout bij het bijwerken van tag"
                throw Exception(errorMessage)
            }

            is NetworkResponse.NetworkError -> throw Exception("Netwerkfout: controleer je internetverbinding")
            is NetworkResponse.UnknownError -> throw Exception("Onbekende fout bij het bijwerken van tag")
        }
    }

    suspend fun deleteTag(id: String) {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$tagUrl/$id")

        when (response) {
            is NetworkResponse.Success -> Unit
            is NetworkResponse.ServerError -> {
                val errorMessage = response.body?.errorDescription ?: response.body?.error ?: "Server fout bij het verwijderen van tag"
                throw Exception(errorMessage)
            }

            is NetworkResponse.NetworkError -> throw Exception("Netwerkfout: controleer je internetverbinding")
            is NetworkResponse.UnknownError -> throw Exception("Onbekende fout bij het verwijderen van tag")
        }
    }
}
