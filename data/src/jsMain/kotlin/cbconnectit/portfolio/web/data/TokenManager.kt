package cbconnectit.portfolio.web.data

import cbconnectit.portfolio.web.data.models.dto.responses.AuthStatusResponse
import cbconnectit.portfolio.web.data.models.enums.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * TokenManager for cookie-based authentication.
 * Tokens are stored in HTTP-only cookies on the backend, so this manager
 * only tracks authentication state without storing actual tokens.
 */
object TokenManager {
    private val _authStatus = MutableStateFlow<AuthStatusResponse?>(null)
    val authStatus: StateFlow<AuthStatusResponse?> = _authStatus.asStateFlow()

    /**
     * Update authentication state after successful login or when checking auth status
     */
    fun setAuthenticated(authStatusResponse: AuthStatusResponse) {
        _authStatus.value = authStatusResponse
    }

    /**
     * Clear authentication state on logout
     */
    fun clear() {
        _authStatus.value = AuthStatusResponse(false)
    }

//    /**
//     * Check if user has valid authentication (tokens stored in HTTP-only cookies)
//     * This should be used in combination with a backend endpoint that verifies the cookies
//     */
//    fun areTokensStillValid(): Boolean? {
//        return _isAuthenticated.value
//    }

    /**
     * Check if the authenticated user is an admin
     */
    fun isAdmin(): Boolean {
        return _authStatus.value?.roleType == UserRole.Admin
    }

    /**
     * Check if the authenticated user is a moderator
     */
    fun isModerator(): Boolean {
        return _authStatus.value?.roleType == UserRole.Moderator
    }

//    /**
//     * Check if tokens are present (used for compatibility with existing code)
//     */
//    fun hasTokens(): Boolean? {
//        return _isAuthenticated.value
//    }
}
