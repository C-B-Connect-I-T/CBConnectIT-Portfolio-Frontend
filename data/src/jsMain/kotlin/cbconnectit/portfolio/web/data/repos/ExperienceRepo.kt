package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.data.models.domain.toExperience
import cbconnectit.portfolio.web.data.models.dto.requests.experience.InsertExperience
import cbconnectit.portfolio.web.data.models.dto.requests.experience.UpdateExperience
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ExperienceDto
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest

object ExperienceRepo {
    private val experienceUrl = "${NetworkingConfig.baseUrl}/api/v1/experiences"

    suspend fun getExperiences(): RepoResult<List<Experience>> {
        val response: NetworkResponse<List<ExperienceDto>, ErrorResponse> = getRequest(experienceUrl)

        return response.toRepoResult(
            successMapper = { experienceDtos -> experienceDtos.map { it.toExperience() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van experiences",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van experiences"
        )
    }

    suspend fun getExperienceById(id: String): RepoResult<Experience> {
        val response: NetworkResponse<ExperienceDto, ErrorResponse> = getRequest("$experienceUrl/$id")

        return response.toRepoResult(
            successMapper = { experienceDto -> experienceDto.toExperience() },
            defaultServerErrorMessage = "Server fout bij het ophalen van experience",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van experience"
        )
    }

    suspend fun insertExperience(experience: InsertExperience): RepoResult<Experience> {
        val response: NetworkResponse<ExperienceDto, ErrorResponse> = postRequest(resource = experienceUrl, body = experience)

        return response.toRepoResult(
            successMapper = { experienceDto -> experienceDto.toExperience() },
            defaultServerErrorMessage = "Server fout bij het aanmaken van experience",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het aanmaken van experience"
        )
    }

    suspend fun updateExperience(id: String, update: UpdateExperience): RepoResult<Experience> {
        val response: NetworkResponse<ExperienceDto, ErrorResponse> = putRequest(resource = "$experienceUrl/$id", body = update)

        return response.toRepoResult(
            successMapper = { experienceDto -> experienceDto.toExperience() },
            defaultServerErrorMessage = "Server fout bij het bijwerken van experience",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het bijwerken van experience"
        )
    }

    suspend fun deleteExperience(id: String): RepoResult<Unit> {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$experienceUrl/$id")

        return response.toRepoResult(
            successMapper = { },
            defaultServerErrorMessage = "Server fout bij het verwijderen van experience",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het verwijderen van experience"
        )
    }
}
