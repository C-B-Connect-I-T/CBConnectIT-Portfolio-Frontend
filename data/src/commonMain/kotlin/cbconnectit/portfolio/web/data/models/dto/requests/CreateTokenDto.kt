package cbconnectit.portfolio.web.data.models.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateTokenDto(
    val username: String,
    val password: String
)
