package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.CompactServiceDto

data class CompactService(
    val id: String,
    val title: String
)

fun CompactServiceDto.toCompactService() = CompactService(id, title)
