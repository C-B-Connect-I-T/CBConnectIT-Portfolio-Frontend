package cbconnectit.portfolio.web.components.toast

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton manager for toast notifications.
 * Manages a stack of up to 4 toasts, automatically removing the oldest when the limit is exceeded.
 * Each toast auto-hides after 5 seconds.
 */
object ToastManager {
    private const val MAX_TOASTS = 4
    const val AUTO_HIDE_DURATION_MS = 5000L

    private val _toasts = MutableStateFlow<List<ToastData>>(emptyList())
    val toasts: StateFlow<List<ToastData>> = _toasts.asStateFlow()

    /**
     * Shows a new toast notification.
     * If the maximum number of toasts is reached, the oldest toast will be removed.
     *
     * @param message The main message content (required)
     * @param title Optional title for the toast
     * @param type The type/severity of the toast (default: INFO)
     */
    fun show(
        message: String,
        title: String? = null,
        type: ToastType = ToastType.INFO
    ) {
        val toast = ToastData(
            title = title,
            message = message,
            type = type
        )

        _toasts.value = _toasts.value.let { currentToasts ->
            if (currentToasts.size >= MAX_TOASTS) {
                // Remove the oldest toast (first in the list)
                currentToasts.drop(1) + toast
            } else {
                currentToasts + toast
            }
        }
    }

    /**
     * Shows a success toast notification.
     */
    fun success(message: String, title: String? = null) {
        show(message = message, title = title, type = ToastType.SUCCESS)
    }

    /**
     * Shows an error toast notification.
     */
    fun error(message: String, title: String? = null) {
        show(message = message, title = title, type = ToastType.ERROR)
    }

    /**
     * Shows a warning toast notification.
     */
    fun warning(message: String, title: String? = null) {
        show(message = message, title = title, type = ToastType.WARNING)
    }

    /**
     * Shows an info toast notification.
     */
    fun info(message: String, title: String? = null) {
        show(message = message, title = title, type = ToastType.INFO)
    }

    /**
     * Removes a specific toast by its ID.
     */
    fun remove(toastId: String) {
        _toasts.value = _toasts.value.filter { it.id != toastId }
    }

    /**
     * Removes all toasts.
     */
    fun clear() {
        _toasts.value = emptyList()
    }
}
