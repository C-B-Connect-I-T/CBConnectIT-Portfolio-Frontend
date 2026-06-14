package cbconnectit.portfolio.web.data.models.dto.requests.tag

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTag(
    val name: String
) {
    val isValid get() = name.isNotBlank()
}
