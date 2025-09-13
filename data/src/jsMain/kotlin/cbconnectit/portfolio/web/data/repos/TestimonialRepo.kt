package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.data.models.domain.toTestimonial
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.TestimonialDto

object TestimonialRepo {
    private val testimonialUrl = "${NetworkingConfig.baseUrl}/api/v1/testimonials"

    suspend fun getTestimonials(): List<Testimonial> {
        val response: NetworkResponse<List<TestimonialDto>, ErrorResponse> = getRequest(testimonialUrl, allowUnauthenticated = true)

        return when (response) {
            is NetworkResponse.Success -> response.body.map { it.toTestimonial() }
            else -> emptyList()
        }
    }
}
