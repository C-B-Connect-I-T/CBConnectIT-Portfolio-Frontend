package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.TokenManager
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.dto.requests.CreateTokenDto
import cbconnectit.portfolio.web.data.models.dto.responses.AuthStatusResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.postRequest

object AuthRepo {
    private val oauthUrl = "${NetworkingConfig.baseUrl}/api/oauth"

    @Suppress("TooGenericExceptionThrown")
    suspend fun login(username: String, password: String) {
        val token = CreateTokenDto(username, password)

        val response: NetworkResponse<Unit, ErrorResponse> = postRequest("$oauthUrl/token", token)

        when (response) {
            // Backend sets HTTP-only cookies, we just need to update local auth state
            // Extract role from the response or make a follow-up call to get user info
            is NetworkResponse.Success -> checkAuthStatus() // Update auth state
            is NetworkResponse.ServerError -> throw RuntimeException(response.body?.errorDescription)
            is NetworkResponse.Error -> throw response.error ?: Exception("Unknown error")
        }
    }

    @Suppress("TooGenericExceptionThrown")
    suspend fun refreshToken() {
        // With cookie-based auth, we don't need to send refresh token manually
        // The backend will read it from the HTTP-only cookie
        val response: NetworkResponse<Unit, ErrorResponse> = postRequest("$oauthUrl/refresh", emptyMap<String, String>())

        when (response) {
            // Update auth state after successful refresh
            is NetworkResponse.Success -> checkAuthStatus()
            is NetworkResponse.ServerError -> throw RuntimeException(response.body?.errorDescription)
            is NetworkResponse.Error -> throw response.error ?: Exception("Unknown error")
        }
    }

    /**
     * Check authentication status by verifying the HTTP-only cookies on the backend
     */
    suspend fun checkAuthStatus(): Boolean {
        val response: NetworkResponse<AuthStatusResponse, ErrorResponse> = getRequest("$oauthUrl/status")

        return when (response) {
            is NetworkResponse.Success -> {
                val authStatus = response.body
                TokenManager.setAuthenticated(authStatus)
                authStatus.authenticated
            }

            is NetworkResponse.ServerError, is NetworkResponse.Error -> {
                TokenManager.setAuthenticated(AuthStatusResponse(false))
                false
            }
        }
    }

    /**
     * Logout by clearing cookies on the backend
     */
    suspend fun logout() {
        try {
            // Call backend logout endpoint to clear HTTP-only cookies
            postRequest<Map<String, String>, ErrorResponse>("$oauthUrl/logout", emptyMap())
        } catch (e: Exception) {
            console.error("Logout error:", e)
        } finally {
            // Clear local auth state regardless of response
            TokenManager.clear()
        }
    }
}
