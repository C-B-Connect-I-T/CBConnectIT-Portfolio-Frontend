package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.MediaFileDto

data class MediaFile(
    val url: String,
    val originalFilename: String,
    val altText: String,
    val mimeType: String
)

fun MediaFileDto.toMediaFile() = MediaFile(
    url,
    originalFilename,
    altText,
    mimeType
)
