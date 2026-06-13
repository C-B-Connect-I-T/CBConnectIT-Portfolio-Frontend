package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.domain.User
import cbconnectit.portfolio.web.data.models.domain.toUser
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.UserDto
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest

object UserRepo {
    private val usersUrl = "${NetworkingConfig.baseUrl}/api/v1/users"
//
//    @Suppress("TooGenericExceptionThrown")
//    suspend fun createUser(user: InsertNewUser): User {
//        val response: NetworkResponse<UserDto, ErrorResponse> = postRequest(usersUrl, user)
//
//        return when (response) {
//            is NetworkResponse.Success -> response.body.toUser()
//            is NetworkResponse.ServerError -> throw RuntimeException(response.body?.errorDescription ?: "Server error")
//            is NetworkResponse.Error -> throw response.error ?: Exception("Unknown error")
//        }
//    }

    @Suppress("TooGenericExceptionThrown")
    suspend fun getCurrentUser(): User {
        val url = "$usersUrl/me"

        val response: NetworkResponse<UserDto, ErrorResponse> = getRequest(url)

        return when (response) {
            is NetworkResponse.Success -> response.body.toUser()
            is NetworkResponse.ServerError -> throw RuntimeException(response.body?.errorDescription ?: "Server error")
            is NetworkResponse.Error -> throw response.error ?: Exception("Unknown error")
        }
    }
//
//    @Suppress("TooGenericExceptionThrown")
//    suspend fun updateCurrentUser(user: UpdateUser): User {
//        val url = "$usersUrl/me"
//        val response: NetworkResponse<UserDto, ErrorResponse> = putRequest(resource = url, body = user)
//
//        return when (response) {
//            is NetworkResponse.Success -> response.body.toUser()
//            is NetworkResponse.ServerError -> throw RuntimeException(response.body?.errorDescription ?: "Server error")
//            is NetworkResponse.Error -> throw response.error ?: Exception("Unknown error")
//        }
//    }
//
//    @Suppress("TooGenericExceptionThrown")
//    suspend fun updatePasswordCurrentUser(updatePassword: UpdatePassword) {
//        val url = "$usersUrl/me/password"
//        val response: NetworkResponse<UserDto, ErrorResponse> = putRequest(resource = url, body = updatePassword)
//
//        return when (response) {
//            is NetworkResponse.Success -> Unit
//            is NetworkResponse.ServerError -> throw RuntimeException(response.body?.errorDescription ?: "Server error")
//            is NetworkResponse.Error -> throw response.error ?: Exception("Unknown error")
//        }
//    }
//
//    suspend fun verifyEmail(code: String): VerificationStatus {
//        val url = "$usersUrl/verify?code=$code"
//        val response: NetworkResponse<Unit, ErrorResponse> = postRequest(url, Unit)
//
//        return when (response) {
//            is NetworkResponse.Success -> VerificationStatus.Success
//            is NetworkResponse.NetworkError -> VerificationStatus.Error("Network error. Please check your connection and try again.")
//            is NetworkResponse.UnknownError -> VerificationStatus.Error("An unknown error occurred. Please try again later.")
//            is NetworkResponse.ServerError -> {
//                val errorBody = response.body
//                if (errorBody != null) {
//                    when (errorBody.error) {
//                        "already_verified" -> VerificationStatus.AlreadyVerified
//                        "verification_code_expired" -> VerificationStatus.Expired
//                        else -> VerificationStatus.Error(errorBody.errorDescription)
//                    }
//                } else {
//                    VerificationStatus.Error("An error occurred during verification.")
//                }
//            }
//        }
//    }
//
//    @Suppress("TooGenericExceptionThrown")
//    suspend fun resendVerificationEmail(): VerificationStatus {
//        val url = "$usersUrl/resend-verification"
//        val response: NetworkResponse<Unit, ErrorResponse> = postRequest(url, Unit)
//
//        return when (response) {
//            is NetworkResponse.Success -> VerificationStatus.SuccessfullySent
//            is NetworkResponse.ServerError -> throw RuntimeException(response.body?.errorDescription ?: "Failed to send verification email")
//            is NetworkResponse.Error -> throw response.error ?: Exception("Unknown error")
//        }
//    }
}
