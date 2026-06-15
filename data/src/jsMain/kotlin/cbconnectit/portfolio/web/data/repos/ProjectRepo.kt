package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.models.domain.toProject
import cbconnectit.portfolio.web.data.models.domain.toTag
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ProjectDto

object ProjectRepo {
    private val projectUrl = "${NetworkingConfig.baseUrl}/api/v1/projects"

    suspend fun getProjects(): RepoResult<List<Project>> {
        val response: NetworkResponse<List<ProjectDto>, ErrorResponse> = getRequest(projectUrl)

        return response.toRepoResult(
            successMapper = { projectDtos -> projectDtos.map { it.toProject() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van projecten",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van projecten"
        )
    }
}
