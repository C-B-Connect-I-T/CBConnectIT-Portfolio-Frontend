package cbconnectit.portfolio.web.data.models.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceDto(
    val id: String,
    val image: MediaFileDto?,
    @SerialName("banner_image")
    val bannerImage: MediaFileDto?,
    val title: String,
    @SerialName("short_description")
    val shortDescription: String? = null,
    val description: String,
    @SerialName("banner_description")
    val bannerDescription: String? = null,
    @SerialName("sub_services")
    val subServices: List<ServiceDto>? = null,
    @SerialName("extra_info")
    val extraInfo: String? = null,
    val tag: TagDto? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

