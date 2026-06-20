package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.JobPosition
import cbconnectit.portfolio.web.data.models.domain.toJobPosition
import cbconnectit.portfolio.web.data.models.dto.requests.jobPosition.InsertJobPosition
import cbconnectit.portfolio.web.data.models.dto.requests.jobPosition.UpdateJobPosition
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.JobPositionDto
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest

object JobPositionRepo {
    private val jobPositionUrl = "${NetworkingConfig.baseUrl}/api/v1/job_positions"

    suspend fun getJobPositions(): RepoResult<List<JobPosition>> {
        val response: NetworkResponse<List<JobPositionDto>, ErrorResponse> = getRequest(jobPositionUrl)

        return response.toRepoResult(
            successMapper = { jobPositionDtos -> jobPositionDtos.map { it.toJobPosition() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van jobPositions",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van jobPositions"
        )
    }

    suspend fun getJobPositionById(id: String): RepoResult<JobPosition> {
        val response: NetworkResponse<JobPositionDto, ErrorResponse> = getRequest("$jobPositionUrl/$id")

        return response.toRepoResult(
            successMapper = { jobPositionDto -> jobPositionDto.toJobPosition() },
            defaultServerErrorMessage = "Server fout bij het ophalen van jobPosition",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van jobPosition"
        )
    }

    suspend fun insertJobPosition(jobPosition: InsertJobPosition): RepoResult<JobPosition> {
        val response: NetworkResponse<JobPositionDto, ErrorResponse> = postRequest(resource = jobPositionUrl, body = jobPosition)

        return response.toRepoResult(
            successMapper = { jobPositionDto -> jobPositionDto.toJobPosition() },
            defaultServerErrorMessage = "Server fout bij het aanmaken van jobPosition",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het aanmaken van jobPosition"
        )
    }

    suspend fun updateJobPosition(id: String, update: UpdateJobPosition): RepoResult<JobPosition> {
        val response: NetworkResponse<JobPositionDto, ErrorResponse> = putRequest(resource = "$jobPositionUrl/$id", body = update)

        return response.toRepoResult(
            successMapper = { jobPositionDto -> jobPositionDto.toJobPosition() },
            defaultServerErrorMessage = "Server fout bij het bijwerken van jobPosition",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het bijwerken van jobPosition"
        )
    }

    suspend fun deleteJobPosition(id: String): RepoResult<Unit> {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$jobPositionUrl/$id")

        return response.toRepoResult(
            successMapper = { },
            defaultServerErrorMessage = "Server fout bij het verwijderen van jobPosition",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het verwijderen van jobPosition"
        )
    }
}
