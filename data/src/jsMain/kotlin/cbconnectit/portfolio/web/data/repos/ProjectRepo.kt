package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.models.domain.toProject
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ProjectDto

object ProjectRepo {
    private val projectUrl = "${NetworkingConfig.baseUrl}/api/v1/projects"

    suspend fun getProjects(): List<Project> {
        val response: NetworkResponse<List<ProjectDto>, ErrorResponse> = getRequest(projectUrl, allowUnauthenticated = true)

        return when (response) {
            is NetworkResponse.Success -> response.body.map { it.toProject() }
            else -> emptyList()
        }
    }
}
