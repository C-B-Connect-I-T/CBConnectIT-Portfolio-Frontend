package cbconnectit.portfolio.web.data.models.dto.requests.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertProject(
    val title: String,
    @SerialName("short_description")
    val shortDescription: String,
    val description: String,
    @SerialName("image_alt_text")
    val imageAltText: String,
    @SerialName("banner_image_alt_text")
    val bannerImageAltText: String,
    val tags: List<String>,
    val links: List<String> = emptyList()
) {
    val isValid: Boolean
        get() = title.isNotBlank() &&
                shortDescription.isNotBlank() &&
                description.isNotBlank() &&
                imageAltText.isNotBlank() &&
                bannerImageAltText.isNotBlank() &&
                tags.isNotEmpty()
}
