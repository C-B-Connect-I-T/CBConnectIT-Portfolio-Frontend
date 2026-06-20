package cbconnectit.portfolio.web.data.models.dto.requests.jobPosition

import kotlinx.serialization.Serializable

@Serializable
data class UpdateJobPosition(
    val name: String
) {
    val isValid get() = name.isNotBlank()
}