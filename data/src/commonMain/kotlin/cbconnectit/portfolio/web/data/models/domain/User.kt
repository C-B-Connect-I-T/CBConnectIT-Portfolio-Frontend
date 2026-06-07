package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.UserDto
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun UserDto.toUser() = User(
    id = this.id,
    fullName = fullName,
    username = username,
    createdAt = LocalDateTime.parse(createdAt).toInstant(TimeZone.UTC),
    updatedAt = LocalDateTime.parse(updatedAt).toInstant(TimeZone.UTC)
)
