package cbconnectit.portfolio.web.data

import cbconnectit.portfolio.web.data.NetworkingConfig.refreshTokenCallback
import cbconnectit.portfolio.web.data.extensions.parseData
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import com.varabyte.kobweb.browser.http.HttpMethod
import com.varabyte.kobweb.browser.http.ResponseException
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.open
import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.url.URL
import org.w3c.fetch.CORS
import org.w3c.fetch.RequestInit
import org.w3c.fetch.RequestMode
import org.w3c.fetch.Response
import org.w3c.xhr.FormData
import kotlin.js.Promise

object NetworkingConfig {

    var baseUrl: String = ""
        private set

    internal var refreshTokenCallback: (suspend () -> Unit)? = null
        private set

    var json: Json = Json
        private set

    fun init(
        baseUrl: String?,
        refreshToken: suspend () -> Unit,
        json: Json? = null
    ) {
        NetworkingConfig.baseUrl = baseUrl ?: ""
        refreshTokenCallback = refreshToken
        this.json = json ?: Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }
    }

    fun getHeadersMap(isMultipart: Boolean = false): Map<String, Any> {
        val headersMap = buildMap {
            if (!isMultipart) {
                put("Content-Type", "application/json")
            }
            // Custom header to identify this as a web client using cookie-based auth
            put("X-Client-Type", "web")
            put("X-Auth-Method", "cookie")
        }

        return headersMap
    }
}

// <editor-fold desc="Regular calls">
suspend inline fun <reified T, reified S> postRequest(
    resource: String,
    body: T
): NetworkResponse<S, ErrorResponse> {
    val bodyJson = if (body !is FormData) {
        runCatching { NetworkingConfig.json.encodeToString(body) }
            .getOrElse { return NetworkResponse.UnknownError(it, null) }.encodeToByteArray()
    } else body

    return fetchWithBody(
        HttpMethod.POST,
        resource = resource,
        body = bodyJson,
        parseBody = { it.parseData() }
    )
}

suspend inline fun <reified T, reified S> putRequest(resource: String, body: T): NetworkResponse<S, ErrorResponse> {
    val bodyJson = if (body !is FormData) {
        runCatching { NetworkingConfig.json.encodeToString(body) }
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
    queryOptions: QueryOptions = QueryOptions()
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
    queryOptions.params.forEach { (key, value) ->
        url.searchParams.append(key, value)
    }

    return fetchWithBody(
        HttpMethod.GET,
        resource = url.toString(),
        parseBody = { it.parseData() }
    )
}

suspend inline fun <reified S> deleteRequest(resource: String): NetworkResponse<S, ErrorResponse> {
    return fetchWithBody(
        HttpMethod.DELETE,
        resource = resource,
        parseBody = { it.parseData() }
    )
}

private val refreshMutex = Mutex()
private const val REFRESH_RETRIES = 3

/**
 * Set of auth endpoints that should not trigger automatic token refresh to prevent deadlocks.
 * These endpoints are part of the authentication flow and may legitimately return 401.
 */
private val AUTH_ENDPOINTS = setOf(
    "/api/oauth/refresh",
    "/api/oauth/status",
    "/api/oauth/token",
    "/api/oauth/logout",
    "/api/oauth/register"
)

/**
 * Checks if the given resource URL is an authentication endpoint.
 */
private fun isAuthEndpoint(resource: String): Boolean {
    return AUTH_ENDPOINTS.any { authEndpoint ->
        resource.contains(authEndpoint)
    }
}

suspend fun <Res> fetchWithBody(
    method: HttpMethod,
    resource: String,
    body: dynamic? = null,
    parseBody: (jsonString: String) -> Res
): NetworkResponse<Res, ErrorResponse> {
    val headers = NetworkingConfig.getHeadersMap(body !is ByteArray)

    return try {
        // Use custom fetch with credentials to send HTTP-only cookies
        val res = fetchWithCredentials(
            method,
            resource,
            headers,
            body ?: undefined,
        )

        val responseText = res.decodeToString()

        @Suppress("UNCHECKED_CAST")
        val data = if (responseText.isEmpty() || responseText.isBlank()) {
            // Handle empty responses by returning Unit cast to Res
            Unit as Res
        } else {
            parseBody(responseText)
        }
        NetworkResponse.Success(data, null)
    } catch (e: ResponseException) {
        // Don't attempt refresh for auth endpoints to prevent deadlocks
        if (e.response.status.toInt() != UNAUTHORIZED_STATUS_CODE || isAuthEndpoint(resource)) {
            // If the error is not 401 Unauthorized, we can return a server error response
            val data = try {
                e.bodyBytes?.decodeToString().parseData<ErrorResponse>()
            } catch (_: Exception) {
                null
            }
            return NetworkResponse.ServerError(data, null)
        }

        // Attempt to refresh the token (via cookie rotation on the backend)
        val isRefreshedSuccessfully = refreshToken()
        if (!isRefreshedSuccessfully) {
            // TODO: we should reset the analytics identity here as well, but the way the modules are ordered, this is not possible for now...
            // Clear auth state and redirect to home
            TokenManager.clear()
            window.open("/", OpenLinkStrategy.IN_PLACE)
        }

        // Retry the original request (cookies will be automatically sent)
        fetchWithBody(
            method,
            resource = resource,
            body = body,
            parseBody = parseBody
        )
    } catch (e: Exception) {
        NetworkResponse.UnknownError(e, null)
    }
}

/**
 * Custom fetch wrapper that includes credentials to send HTTP-only cookies
 */
private suspend fun fetchWithCredentials(
    method: HttpMethod,
    resource: String,
    headers: Map<String, Any>,
    body: dynamic,
): Response {
    val headersObj = js("({})")
    headers.forEach { (key, value) ->
        headersObj[key] = value
    }

    val options = RequestInit(
        method = method.name,
        mode = RequestMode.CORS,
        headers = headersObj,
    )

    // Set credentials to include cookies
    options.asDynamic().credentials = "include"

    if (body != undefined) {
        options.asDynamic().body = body
    }

    return window.fetch(resource, options).await().also { response ->
        if (!response.ok) {
            val bodyText = response.text().await()
            val bodyBytes = bodyText.encodeToByteArray()
            throw ResponseException(response, bodyBytes)
        }
    }
}

private suspend fun Response.decodeToString(): String {
    return this.text().await()
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
private suspend fun <T> Promise<T>.await(): T = suspendCancellableCoroutine { cont ->
    this.then(
        onFulfilled = { cont.resumeWith(Result.success(it)) },
        onRejected = { cont.resumeWith(Result.failure(it)) }
    )
}

private suspend fun refreshToken(): Boolean = refreshMutex.withLock {
    repeat(REFRESH_RETRIES) { attempt ->
        try {
            refreshTokenCallback?.invoke()
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
    val offset: Int? = null,
    val params: Map<String, String> = emptyMap()
)
// </editor-fold>
