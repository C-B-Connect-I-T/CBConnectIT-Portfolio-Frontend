package cbconnectit.portfolio.web.data.models.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.w3c.fetch.Headers
import org.w3c.fetch.Response

@Serializable
data class ErrorResponse(
    val error: String,
    @SerialName("error_description")
    val errorDescription: String,
    val status: Int
//    val errors: ArrayList<String>? = null
)
