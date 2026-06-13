package cbconnectit.portfolio.web.data.models.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String = "",
    @SerialName("full_name")
    val fullName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    val username: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = ""
)
