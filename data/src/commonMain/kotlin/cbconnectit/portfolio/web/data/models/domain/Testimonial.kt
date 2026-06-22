package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.TestimonialDto

data class Testimonial(
    val id: String,
    val fullName: String,
    val company: Company?,
    val jobPosition: JobPosition,
    val avatarImage: MediaFile?,
    val review: String,
    val createdAt: String,
    val updatedAt: String
)

fun TestimonialDto.toTestimonial() = Testimonial(
    id = id,
    fullName = fullName,
    company = company?.toCompany(),
    jobPosition = jobPosition.toJobPosition(),
    avatarImage = avatarImage?.toMediaFile(),
    review = review,
    createdAt = createdAt,
    updatedAt = updatedAt
)
