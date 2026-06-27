package cbconnectit.portfolio.web.data.models.dto.requests.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateService(
    val title: String,
    val description: String,
    @SerialName("short_description")
    val shortDescription: String? = null,
    @SerialName("image_alt_text")
    val imageAltText: String,
    @SerialName("banner_image_alt_text")
    val bannerImageAltText: String? = null,
    @SerialName("remove_banner_image")
    val removeBannerImage: Boolean = false,
    @SerialName("banner_description")
    val bannerDescription: String? = null,
    @SerialName("extra_info")
    val extraInfo: String? = null,
    @SerialName("parent_service_id")
    val parentServiceId: String? = null,
    @SerialName("tag_id")
    val tagId: String? = null
) {
    val isValid get() = title.isNotBlank() &&
            description.isNotBlank() &&
            (shortDescription?.isNotBlank() != false) &&
            imageAltText.isNotBlank()
}
