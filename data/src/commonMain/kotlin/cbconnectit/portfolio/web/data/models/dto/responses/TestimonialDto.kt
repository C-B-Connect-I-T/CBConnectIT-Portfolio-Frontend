package cbconnectit.portfolio.web.data.models.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestimonialDto(
    val id: String = "",
    @SerialName("avatar_image")
    val avatarImage: MediaFileDto? = null,
    @SerialName("full_name")
    val fullName: String = "",
    val company: CompanyDto? = null,
    @SerialName("job_position")
    val jobPosition: JobPositionDto = JobPositionDto(),
    val review: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = ""
)