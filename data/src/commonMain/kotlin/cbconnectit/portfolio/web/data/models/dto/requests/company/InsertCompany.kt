package cbconnectit.portfolio.web.data.models.dto.requests.company

import kotlinx.serialization.Serializable

@Serializable
data class InsertCompany(
    val name: String,
    val links: List<String>? = null
) {
    val isValid get() = name.isNotBlank()
}
