package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.ServiceDto

data class Service(
    val id: String,
    val image: MediaFile?,
    val bannerImage: MediaFile?,
    val title: String,
    val shortDescription: String? = null,
    val description: String,
    val bannerDescription: String? = null,
    val subServices: List<Service>? = null,
    val extraInfo: String? = null,
    val tag: Tag? = null,
    val createdAt: String,
    val updatedAt: String
)

fun ServiceDto.toService(): Service = Service(
    id,
    image?.toMediaFile(),
    bannerImage?.toMediaFile(),
    title,
    shortDescription,
    description,
    bannerDescription,
    subServices?.map { it.toService() },
    extraInfo,
    tag?.toTag(),
    createdAt,
    updatedAt
)