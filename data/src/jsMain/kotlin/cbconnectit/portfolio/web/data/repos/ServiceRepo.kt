package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.domain.Service
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.data.models.domain.toService
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ServiceDto
import com.varabyte.kobweb.browser.http.http
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object ServiceRepo {
    private val serviceUrl = "${NetworkingConfig.baseUrl}/api/v1/services"

    suspend fun getServices(): List<Service> {
        val response: NetworkResponse<List<ServiceDto>, ErrorResponse> = getRequest(serviceUrl, allowUnauthenticated = true)

        return when (response) {
            is NetworkResponse.Success -> response.body.map { it.toService() }
            else -> emptyList()
        }
    }

    suspend fun getServiceById(id: String): Service? {
        val url = "$serviceUrl/$id"
        val response: NetworkResponse<ServiceDto, ErrorResponse> = getRequest(url, allowUnauthenticated = true)

        return when (response) {
            is NetworkResponse.Success -> response.body.toService()
            else -> null
        }
    }
}
