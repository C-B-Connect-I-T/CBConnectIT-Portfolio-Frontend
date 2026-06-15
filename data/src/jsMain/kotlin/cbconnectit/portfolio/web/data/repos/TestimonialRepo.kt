package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.data.models.domain.toTestimonial
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.TestimonialDto

object TestimonialRepo {
    private val testimonialUrl = "${NetworkingConfig.baseUrl}/api/v1/testimonials"

    suspend fun getTestimonials(): RepoResult<List<Testimonial>> {
        val response: NetworkResponse<List<TestimonialDto>, ErrorResponse> = getRequest(testimonialUrl)

        return response.toRepoResult(
            successMapper = { testimonialDtos -> testimonialDtos.map { it.toTestimonial() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van testimonials",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van testimonials"
        )
    }
}
