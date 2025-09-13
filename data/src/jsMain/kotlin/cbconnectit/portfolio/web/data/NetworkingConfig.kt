package cbconnectit.portfolio.web.data

import cbconnectit.portfolio.web.data.models.domain.CredentialTokens
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import com.varabyte.kobweb.browser.http.FetchDefaults
import com.varabyte.kobweb.browser.http.HttpMethod
import com.varabyte.kobweb.browser.http.ResponseException
import com.varabyte.kobweb.browser.http.fetch
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.open
import cbconnectit.portfolio.web.data.extensions.UNAUTHORIZED_STATUS_CODE
import cbconnectit.portfolio.web.data.extensions.parseData
import cbconnectit.portfolio.web.data.models.NetworkResponse
import kotlinx.browser.window
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.url.URL
import org.w3c.fetch.RequestRedirect
import org.w3c.xhr.FormData


object NetworkingConfig {

    var baseUrl: String = ""
        private set

    fun init(baseUrl: String?) {
        NetworkingConfig.baseUrl = baseUrl ?: ""
    }

    fun getTokens(): CredentialTokens? {
        val tokens = window.localStorage.getItem("tokens")
        return tokens?.let { Json.decodeFromString<CredentialTokens>(it) }
    }

    fun getHeadersMap(isMultipart: Boolean = false): Map<String, Any> {
        val tokens = getTokens()
        val headersMap = buildMap {
            if (!isMultipart) {
                put("Content-Type", "application/json")
            }
            tokens?.let { put("Authorization", "${it.tokenType}${it.accessToken}") }
        }

        return headersMap
    }

    val getJson = Json { ignoreUnknownKeys = true; isLenient = true }
}

// <editor-fold desc="Regular calls">
suspend inline fun <reified T, reified S> postRequest(
    resource: String,
    body: T,
    allowUnauthenticated: Boolean = false
): NetworkResponse<S, ErrorResponse> {
    val bodyJson = if (body !is FormData) {
        runCatching { Json.encodeToString(body) }
            .getOrElse { return NetworkResponse.UnknownError(it, null) }.encodeToByteArray()
    } else body

    return fetchWithBody(
        HttpMethod.POST,
        resource = resource,
        body = bodyJson,
        allowUnauthenticated = allowUnauthenticated,
        parseBody = { it.parseData() }
    )
}

suspend inline fun <reified T, reified S> putRequest(resource: String, body: T): NetworkResponse<S, ErrorResponse> {
    val bodyJson = if (body !is FormData) {
        runCatching { Json.encodeToString(body) }
            .getOrElse { return NetworkResponse.UnknownError(it, null) }.encodeToByteArray()
    } else body

    return fetchWithBody(
        HttpMethod.PUT,
        resource = resource,
        body = bodyJson,
        parseBody = { it.parseData() }
    )
}

suspend inline fun <reified S> getRequest(
    resource: String,
    queryOptions: QueryOptions = QueryOptions(),
    allowUnauthenticated: Boolean = false
): NetworkResponse<S, ErrorResponse> {
    val url = URL(resource)
    queryOptions.offset?.let { url.searchParams.append("offset", it.toString()) }
    queryOptions.limit?.let { url.searchParams.append("limit", it.toString()) }
    if (queryOptions.sorts.isNotEmpty()) {
        val sort = queryOptions.sorts.joinToString(",") {
            val appender = if (it.ascending) "" else "-"
            "$appender${it.field}"
        }
        url.searchParams.append("sort", sort)
    }

    return fetchWithBody(
        HttpMethod.GET,
        resource = url.toString(),
        allowUnauthenticated = allowUnauthenticated,
        parseBody = { it.parseData() }
    )
}

suspend fun deleteRequest(resource: String): NetworkResponse<Unit, ErrorResponse> {
    val response = fetchWithBody(
        HttpMethod.DELETE,
        resource = resource,
        parseBody = { it.parseData<Unit>() }
    )

    return response
}

private val refreshMutex = Mutex()
private const val REFRESH_RETRIES = 3

suspend fun <Res> fetchWithBody(
    method: HttpMethod,
    resource: String,
    body: dynamic? = null,
    allowUnauthenticated: Boolean = false,
    redirect: RequestRedirect? = FetchDefaults.Redirect,
    parseBody: (jsonString: String) -> Res
): NetworkResponse<Res, ErrorResponse> {
    val headers = NetworkingConfig.getHeadersMap(body !is ByteArray)

    // TODO [TicketNumber]: not sure if this is still needed, but it was in the original code
//    if (body is ByteArray) {
//        headers.toMutableMap()["Content-Length"] = body.size
//    }

    return try {
        val res = window.fetch(
            method,
            resource,
            headers,
            body ?: undefined,
            redirect ?: undefined
        )

        val data = parseBody(res.decodeToString())
        NetworkResponse.Success(data, null)
    } catch (e: ResponseException) {
        if (e.response.status.toInt() != UNAUTHORIZED_STATUS_CODE || allowUnauthenticated) {
            // If the error is not 401 Unauthorized, we can return a server error response
            val data = e.bodyBytes?.decodeToString().parseData<ErrorResponse>()
            return NetworkResponse.ServerError(data, null)
        }

        // If we reach here, it means the user is unauthorized so we need to check if the user wants to refresh their login
        val remember = window.localStorage.getItem("remember")?.toBoolean() ?: false
        if (!remember) window.open("/admin/login", OpenLinkStrategy.IN_PLACE)

        // Attempt to refresh the token
        val isRefreshedSuccessfully = refreshToken()
        if (!isRefreshedSuccessfully) window.open("/admin/login", OpenLinkStrategy.IN_PLACE)

        // Retry the original request with the new token attached automatically
        fetchWithBody(
            method,
            resource = resource,
            body = body,
            redirect = redirect,
            parseBody = parseBody
        )
    } catch (e: Exception) {
        NetworkResponse.UnknownError(e, null)
    }
}

private suspend fun refreshToken(): Boolean = refreshMutex.withLock {
    repeat(REFRESH_RETRIES) { attempt ->
        try {
            // TODO: add the AuthRepo and implement the refresh logic
//            AuthRepo.refreshToken()
            return true
        } catch (_: Exception) {
            if (attempt == 2) return false
        }
    }
    false
}
// </editor-fold>

// <editor-fold desc="Query Parameters">
//enum class FilterOperator {
//    EQ, NE, GT, GTE, LT, LTE, LIKE
//}

//data class FilterCondition(
//    val field: String,
//    val operator: FilterOperator = FilterOperator.EQ,
//    val value: String
//)

data class SortCondition(
    val field: String,
    val ascending: Boolean = true
)

data class QueryOptions(
//    val filters: List<FilterCondition> = emptyList(),
    val sorts: List<SortCondition> = emptyList(),
    val limit: Int? = null,
    val offset: Int? = null
)
// </editor-fold>
