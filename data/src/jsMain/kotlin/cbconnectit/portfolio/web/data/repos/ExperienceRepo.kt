package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.data.models.domain.toExperience
import cbconnectit.portfolio.web.data.models.domain.toProject
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ExperienceDto

object ExperienceRepo {
    private val experienceUrl = "${NetworkingConfig.baseUrl}/api/v1/experiences"

    suspend fun getExperiences(): RepoResult<List<Experience>> {
        val response: NetworkResponse<List<ExperienceDto>, ErrorResponse> = getRequest(experienceUrl)

        return response.toRepoResult(
            successMapper = { experienceDtos -> experienceDtos.map { it.toExperience() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van ervaringen",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van ervaringen"
        )
    }
}
