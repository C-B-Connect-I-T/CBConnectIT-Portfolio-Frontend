package cbconnectit.portfolio.web.data.extensions

import cbconnectit.portfolio.web.data.NetworkingConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.files.File
import org.w3c.xhr.FormData
import kotlin.collections.iterator

const val UNAUTHORIZED_STATUS_CODE = 401

fun buildFormData(
    extraFields: Map<String, Any?> = emptyMap()
): FormData = buildFormData<Any>(null, extraFields = extraFields)

inline fun <reified T> buildFormData(
    data: T? = null,
    jsonFieldName: String = "payload",
    extraFields: Map<String, Any?> = emptyMap()
): FormData {
    val formData = FormData()

    // Add the serialized JSON body
    if (data != null) {
        val json = Json.encodeToString(data)
        formData.append(jsonFieldName, json)
    }

    // Add any extra fields like files or strings
    for ((key, value) in extraFields) {
        when (value) {
            is File -> formData.append(key, value)
            is String -> formData.append(key, value)
            is Number -> formData.append(key, value.toString())
            is Boolean -> formData.append(key, value.toString())
            null -> {} // Skip nulls
            else -> console.warn("Unsupported form data value type for key '$key': $value")
        }
    }

    return formData
}

inline fun <reified T> String?.parseData(): T = NetworkingConfig.getJson.decodeFromString(this ?: "")
