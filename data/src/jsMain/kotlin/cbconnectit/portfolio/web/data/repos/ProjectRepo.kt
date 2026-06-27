package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.extensions.buildFormData
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.models.domain.toProject
import cbconnectit.portfolio.web.data.models.dto.requests.project.InsertProject
import cbconnectit.portfolio.web.data.models.dto.requests.project.UpdateProject
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ProjectDto
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest
import org.w3c.files.File

object ProjectRepo {
    private val projectUrl = "${NetworkingConfig.baseUrl}/api/v1/projects"

    suspend fun getProjects(): RepoResult<List<Project>> {
        val response: NetworkResponse<List<ProjectDto>, ErrorResponse> = getRequest(projectUrl)

        return response.toRepoResult(
            successMapper = { projectDtos -> projectDtos.map { it.toProject() } },
            defaultServerErrorMessage = "Server error while fetching projects",
            networkErrorMessage = "Network error while fetching projects. Check your connection.",
            unknownErrorMessage = "Unknown error while fetching projects"
        )
    }

    suspend fun getProjectById(id: String): RepoResult<Project> {
        val response: NetworkResponse<ProjectDto, ErrorResponse> = getRequest("$projectUrl/$id")

        return response.toRepoResult(
            successMapper = { projectDto -> projectDto.toProject() },
            defaultServerErrorMessage = "Server error while fetching project",
            networkErrorMessage = "Network error while fetching project. Check your connection.",
            unknownErrorMessage = "Unknown error while fetching project"
        )
    }

    suspend fun insertProject(
        project: InsertProject,
        image: File,
        bannerImage: File
    ): RepoResult<Project> {
        val formData = buildFormData(
            data = project,
            extraFields = mapOf(
                "image" to image,
                "bannerImage" to bannerImage
            )
        )

        val response: NetworkResponse<ProjectDto, ErrorResponse> = postRequest(resource = projectUrl, body = formData)

        return response.toRepoResult(
            successMapper = { projectDto -> projectDto.toProject() },
            defaultServerErrorMessage = "Server error while creating project",
            networkErrorMessage = "Network error while creating project. Check your connection.",
            unknownErrorMessage = "Unknown error while creating project"
        )
    }

    suspend fun updateProject(
        id: String,
        update: UpdateProject,
        image: File? = null,
        bannerImage: File? = null
    ): RepoResult<Project> {
        val endpoint = "$projectUrl/$id"
        val response: NetworkResponse<ProjectDto, ErrorResponse> = if (image == null && bannerImage == null) {
            putRequest(resource = endpoint, body = update)
        } else {
            val formData = buildFormData(
                data = update,
                extraFields = mapOf(
                    "image" to image,
                    "bannerImage" to bannerImage
                ).filterValues { it != null }
            )
            putRequest(resource = endpoint, body = formData)
        }

        return response.toRepoResult(
            successMapper = { projectDto -> projectDto.toProject() },
            defaultServerErrorMessage = "Server error while updating project",
            networkErrorMessage = "Network error while updating project. Check your connection.",
            unknownErrorMessage = "Unknown error while updating project"
        )
    }

    suspend fun deleteProject(id: String): RepoResult<Unit> {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$projectUrl/$id")

        return response.toRepoResult(
            successMapper = { },
            defaultServerErrorMessage = "Server error while deleting project",
            networkErrorMessage = "Network error while deleting project. Check your connection.",
            unknownErrorMessage = "Unknown error while deleting project"
        )
    }
}
