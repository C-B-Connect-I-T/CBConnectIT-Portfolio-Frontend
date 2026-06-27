package cbconnectit.portfolio.web.data.models.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceAdminDto(
    val id: String = "",
    val title: String = "",
    @SerialName("parent_service")
    val parentService: CompactServiceDto? = null,
    val tag: TagDto? = null,
    @SerialName("updated_at")
    val updatedAt: String = ""
)
