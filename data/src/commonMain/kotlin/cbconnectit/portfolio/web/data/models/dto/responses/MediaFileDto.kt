package cbconnectit.portfolio.web.data.models.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaFileDto(
    val url: String,
    @SerialName("original_filename")
    val originalFilename: String,
    @SerialName("alt_text")
    val altText: String,
    @SerialName("mime_type")
    val mimeType: String
)
