package cbconnectit.portfolio.web.data.models.domain

import cbconnectit.portfolio.web.data.models.dto.responses.CredentialsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CredentialTokens(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("token_type")
    val tokenType: String
)

fun CredentialTokens.toDto(): CredentialsResponse = CredentialsResponse(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    tokenType = tokenType
)

fun CredentialsResponse.toCredentialTokens(): CredentialTokens = CredentialTokens(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
    tokenType = tokenType
)