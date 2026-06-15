package cbconnectit.portfolio.web.data.models

enum class RepoErrorKind {
    SERVER,
    NETWORK,
    UNKNOWN
}

sealed interface RepoResult<out T> {
    data class Success<T>(val data: T) : RepoResult<T>

    data class Error(
        val message: String,
        val kind: RepoErrorKind,
        val cause: Throwable? = null
    ) : RepoResult<Nothing>
}

inline fun <T, R> RepoResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (RepoResult.Error) -> R
): R {
    return when (this) {
        is RepoResult.Success -> onSuccess(data)
        is RepoResult.Error -> onError(this)
    }
}
