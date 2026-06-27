package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.extensions.buildFormData
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.data.models.domain.ServiceAdmin
import cbconnectit.portfolio.web.data.models.domain.toService
import cbconnectit.portfolio.web.data.models.domain.toServiceAdmin
import cbconnectit.portfolio.web.data.models.dto.requests.service.InsertService
import cbconnectit.portfolio.web.data.models.dto.requests.service.UpdateService
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ServiceAdminDto
import cbconnectit.portfolio.web.data.models.dto.responses.ServiceDto
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest
import org.w3c.files.File

object ServiceRepo {
    private val serviceUrl = "${NetworkingConfig.baseUrl}/api/v1/services"

    suspend fun getServices(): RepoResult<List<Service>> {
        val response: NetworkResponse<List<ServiceDto>, ErrorResponse> = getRequest(serviceUrl)

        return response.toRepoResult(
            successMapper = { serviceDtos -> serviceDtos.map { it.toService() } },
            defaultServerErrorMessage = "Server error while fetching services",
            networkErrorMessage = "Network error while fetching services. Check your connection.",
            unknownErrorMessage = "Unknown error while fetching services"
        )
    }

    suspend fun getServiceById(id: String): RepoResult<Service?> {
        val url = "$serviceUrl/$id"
        val response: NetworkResponse<ServiceDto, ErrorResponse> = getRequest(url)

        return response.toRepoResult(
            successMapper = { serviceDto -> serviceDto.toService() } ,
            defaultServerErrorMessage = "Server error while fetching service",
            networkErrorMessage = "Network error while fetching service. Check your connection.",
            unknownErrorMessage = "Unknown error while fetching service"
        )
    }

    suspend fun getServicesOverview(): RepoResult<List<ServiceAdmin>> {
        val response: NetworkResponse<List<ServiceAdminDto>, ErrorResponse> = getRequest("$serviceUrl/overview")

        return response.toRepoResult(
            successMapper = { dtos -> dtos.map { it.toServiceAdmin() } },
            defaultServerErrorMessage = "Server error while fetching services overview",
            networkErrorMessage = "Network error while fetching services overview. Check your connection.",
            unknownErrorMessage = "Unknown error while fetching services overview"
        )
    }

    suspend fun insertService(
        service: InsertService,
        image: File,
        bannerImage: File? = null
    ): RepoResult<Service> {
        val formData = buildFormData(
            data = service,
            extraFields = mapOf(
                "image" to image,
                "bannerImage" to bannerImage
            ).filterValues { it != null }
        )

        val response: NetworkResponse<ServiceDto, ErrorResponse> = postRequest(resource = serviceUrl, body = formData)

        return response.toRepoResult(
            successMapper = { serviceDto -> serviceDto.toService() },
            defaultServerErrorMessage = "Server error while creating service",
            networkErrorMessage = "Network error while creating service. Check your connection.",
            unknownErrorMessage = "Unknown error while creating service"
        )
    }

    suspend fun updateService(
        id: String,
        update: UpdateService,
        image: File? = null,
        bannerImage: File? = null
    ): RepoResult<Service> {
        val endpoint = "$serviceUrl/$id"
        val response: NetworkResponse<ServiceDto, ErrorResponse> = if (image == null && bannerImage == null) {
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
            successMapper = { serviceDto -> serviceDto.toService() },
            defaultServerErrorMessage = "Server error while updating service",
            networkErrorMessage = "Network error while updating service. Check your connection.",
            unknownErrorMessage = "Unknown error while updating service"
        )
    }

    suspend fun deleteService(id: String): RepoResult<Unit> {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$serviceUrl/$id")

        return response.toRepoResult(
            successMapper = { },
            defaultServerErrorMessage = "Server error while deleting service",
            networkErrorMessage = "Network error while deleting service. Check your connection.",
            unknownErrorMessage = "Unknown error while deleting service"
        )
    }
}
