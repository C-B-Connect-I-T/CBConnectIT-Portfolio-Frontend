package cbconnectit.portfolio.web.data.extensions

import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoErrorKind
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse

internal fun ErrorResponse?.getErrorMessage(defaultMessage: String): String {
    return this?.errorDescription
        ?.takeIf { it.isNotBlank() }
        ?: this?.error?.takeIf { it.isNotBlank() }
        ?: defaultMessage
}

internal inline fun <S, T> NetworkResponse<S, ErrorResponse>.toRepoResult(
    successMapper: (S) -> T,
    defaultServerErrorMessage: String,
    networkErrorMessage: String,
    unknownErrorMessage: String
): RepoResult<T> {
    return when (this) {
        is NetworkResponse.Success -> RepoResult.Success(successMapper(body))
        is NetworkResponse.ServerError -> RepoResult.Error(
            message = body.getErrorMessage(defaultServerErrorMessage),
            kind = RepoErrorKind.SERVER
        )

        is NetworkResponse.NetworkError -> RepoResult.Error(
            message = networkErrorMessage,
            kind = RepoErrorKind.NETWORK,
            cause = error
        )

        is NetworkResponse.UnknownError -> RepoResult.Error(
            message = unknownErrorMessage,
            kind = RepoErrorKind.UNKNOWN,
            cause = error
        )
    }
}
