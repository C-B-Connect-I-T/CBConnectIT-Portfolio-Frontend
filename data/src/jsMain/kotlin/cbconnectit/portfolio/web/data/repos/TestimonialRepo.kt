package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.extensions.buildFormData
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.data.models.domain.toTestimonial
import cbconnectit.portfolio.web.data.models.dto.requests.testimonial.InsertTestimonial
import cbconnectit.portfolio.web.data.models.dto.requests.testimonial.UpdateTestimonial
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.models.dto.responses.TestimonialDto
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest
import org.w3c.files.File

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

    suspend fun getTestimonialById(id: String): RepoResult<Testimonial> {
        val response: NetworkResponse<TestimonialDto, ErrorResponse> = getRequest("$testimonialUrl/$id")

        return response.toRepoResult(
            successMapper = { testimonialDto -> testimonialDto.toTestimonial() },
            defaultServerErrorMessage = "Server fout bij het ophalen van testimonial",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van testimonial"
        )
    }

    suspend fun insertTestimonial(testimonial: InsertTestimonial, avatarImage: File?): RepoResult<Testimonial> {
        val formData = buildFormData(
            data = testimonial,
            extraFields = mapOf("image" to avatarImage).filterValues { it != null }
        )

        val response: NetworkResponse<TestimonialDto, ErrorResponse> = postRequest(resource = testimonialUrl, body = formData)

        return response.toRepoResult(
            successMapper = { testimonialDto -> testimonialDto.toTestimonial() },
            defaultServerErrorMessage = "Server fout bij het aanmaken van testimonial",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het aanmaken van testimonial"
        )
    }

    suspend fun updateTestimonial(id: String, update: UpdateTestimonial): RepoResult<Testimonial> {
        val response: NetworkResponse<TestimonialDto, ErrorResponse> = putRequest(resource = "$testimonialUrl/$id", body = update)

        return response.toRepoResult(
            successMapper = { testimonialDto -> testimonialDto.toTestimonial() },
            defaultServerErrorMessage = "Server fout bij het bijwerken van testimonial",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het bijwerken van testimonial"
        )
    }

    suspend fun updateTestimonialAvatar(id: String, file: File, altText: String): RepoResult<Testimonial> {
        val formData = buildFormData(
            extraFields = mapOf("image" to file),
            data = mapOf("altText" to altText)
        )

        val response: NetworkResponse<TestimonialDto, ErrorResponse> = putRequest(resource = "$testimonialUrl/$id/image", body = formData)

        return response.toRepoResult(
            successMapper = { testimonialDto -> testimonialDto.toTestimonial() },
            defaultServerErrorMessage = "Server fout bij het uploaden van afbeelding",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het uploaden van afbeelding"
        )
    }

    suspend fun deleteTestimonialAvatar(id: String): RepoResult<Testimonial> {
        val response: NetworkResponse<TestimonialDto, ErrorResponse> = deleteRequest(resource = "$testimonialUrl/$id/image")

        return response.toRepoResult(
            successMapper = { testimonialDto -> testimonialDto.toTestimonial() },
            defaultServerErrorMessage = "Server fout bij het verwijderen van afbeelding",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het verwijderen van afbeelding"
        )
    }

    suspend fun deleteTestimonial(id: String): RepoResult<Unit> {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$testimonialUrl/$id")

        return response.toRepoResult(
            successMapper = { },
            defaultServerErrorMessage = "Server fout bij het verwijderen van testimonial",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het verwijderen van testimonial"
        )
    }
}
