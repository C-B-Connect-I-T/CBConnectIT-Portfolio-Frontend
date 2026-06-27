package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.ProjectDto

data class Project(
    val id: String,
    val bannerImage: MediaFile?,
    val image: MediaFile?,
    val title: String,
    val shortDescription: String,
    val description: String,
    val links: List<Link>,
    val tags: List<Tag>,
    val createdAt: String,
    val updatedAt: String
)

fun ProjectDto.toProject() = Project(
    id,
    bannerImage?.toMediaFile(),
    image?.toMediaFile(),
    title,
    shortDescription,
    description,
    links.map { it.toLink() },
    tags.map { it.toTag() },
    createdAt,
    updatedAt
)