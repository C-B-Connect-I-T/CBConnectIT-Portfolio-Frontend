package cbconnectit.portfolio.web.data.models.dto.requests.company

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCompany(
    val name: String,
    val links: List<String>? = null
) {
    val isValid get() = name.isNotBlank()
}
