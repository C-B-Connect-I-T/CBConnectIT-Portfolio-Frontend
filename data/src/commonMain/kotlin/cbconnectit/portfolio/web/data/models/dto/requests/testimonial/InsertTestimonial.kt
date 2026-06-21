package cbconnectit.portfolio.web.data.models.dto.requests.testimonial

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertTestimonial(
    @SerialName("image_url")
    val imageUrl: String,
    val review: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("company_id")
    val companyId: String,
    @SerialName("job_position_id")
    val jobPositionId: String
) {
    val isValid get() = fullName.isNotBlank() && companyId.isNotBlank() && review.isNotBlank() && jobPositionId.isNotBlank()
}
