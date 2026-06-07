package cbconnectit.portfolio.web.components.toast

import kotlin.js.Date

/**
 * Data class representing a single toast notification.
 *
 * @property id Unique identifier for the toast
 * @property title Optional title of the toast
 * @property message The main message content
 * @property type The type/severity of the toast (success, error, warning, info)
 * @property createdAt Timestamp when the toast was created
 */
data class ToastData(
    val id: String = generateId(),
    val title: String? = null,
    val message: String,
    val type: ToastType = ToastType.INFO,
    val createdAt: Double = Date.now()
) {
    companion object {
        private var counter = 0
        private fun generateId(): String = "toast_${Date.now()}_${counter++}"
    }
}

/**
 * Configuration for toast positioning.
 * Allows easy modification of toast container position in the future.
 */
enum class ToastPosition {
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_CENTER,
    BOTTOM_RIGHT;

    companion object {
        /**
         * Default position for toasts.
         * Change this value to modify the default position globally.
         */
        val DEFAULT = BOTTOM_RIGHT
    }
}

/**
 * Represents the type/severity of a toast notification.
 * Each type will have different color schemes matching Material Design theme.
 */
enum class ToastType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}
