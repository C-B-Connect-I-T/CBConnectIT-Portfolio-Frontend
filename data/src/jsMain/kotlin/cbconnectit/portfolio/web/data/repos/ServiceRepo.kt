package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.data.models.domain.toProject
import cbconnectit.portfolio.web.data.models.domain.toService
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ServiceDto
import com.varabyte.kobweb.browser.http.http
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object ServiceRepo {
    private val serviceUrl = "${NetworkingConfig.baseUrl}/api/v1/services"

    suspend fun getServices(): RepoResult<List<Service>> {
        val response: NetworkResponse<List<ServiceDto>, ErrorResponse> = getRequest(serviceUrl)

        return response.toRepoResult(
            successMapper = { serviceDtos -> serviceDtos.map { it.toService() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van services",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van services"
        )
    }

    suspend fun getServiceById(id: String): RepoResult<Service?> {
        val url = "$serviceUrl/$id"
        val response: NetworkResponse<ServiceDto, ErrorResponse> = getRequest(url)

        return response.toRepoResult(
            successMapper = { serviceDto -> serviceDto.toService() } ,
            defaultServerErrorMessage = "Server fout bij het ophalen van service",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van service"
        )
    }
}
