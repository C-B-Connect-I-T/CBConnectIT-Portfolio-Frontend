package cbconnectit.portfolio.web.data.models.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class CompactServiceDto(
    val id: String = "",
    val title: String = ""
)
