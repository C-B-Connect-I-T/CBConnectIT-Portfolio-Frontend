package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.ServiceAdminDto

data class ServiceAdmin(
    val id: String,
    val title: String,
    val parentService: CompactService?,
    val tag: Tag?,
    val updatedAt: String
)

fun ServiceAdminDto.toServiceAdmin() = ServiceAdmin(
    id,
    title,
    parentService?.toCompactService(),
    tag?.toTag(),
    updatedAt
)
