package cbconnectit.portfolio.web.data.models.dto.responses

import cbconnectit.portfolio.web.data.models.enums.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthStatusResponse(
    val authenticated: Boolean,
    val role: String? = null,
    @SerialName("user_id")
    val userId: String? = null
) {
    val roleType: UserRole?
        get() = UserRole.mapValue(role)
}
