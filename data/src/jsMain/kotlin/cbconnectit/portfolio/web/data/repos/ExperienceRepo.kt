package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.domain.Experience
import cbconnectit.portfolio.web.data.models.domain.toExperience
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ExperienceDto

object ExperienceRepo {
    private val experienceUrl = "${NetworkingConfig.baseUrl}/api/v1/experiences"

    suspend fun getExperiences(): List<Experience> {
        val response: NetworkResponse<List<ExperienceDto>, ErrorResponse> = getRequest(experienceUrl, allowUnauthenticated = true)

        return when (response) {
            is NetworkResponse.Success -> response.body.map { it.toExperience() }
            else -> emptyList()
        }
    }
}
