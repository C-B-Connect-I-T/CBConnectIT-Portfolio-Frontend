package cbconnectit.portfolio.web.utils

/**
 * Environment-aware logger that suppresses logs in production.
 * Logs are visible in development and staging environments.
 *
 * Usage:
 *   Logger.debug("MyTag", "Debug message")
 *   Logger.info("MyTag", "Info message")
 *   Logger.warn("MyTag", "Warning message")
 *   Logger.error("MyTag", "Error message")
 */
object Logger {
    private fun shouldLog(): Boolean {
        // Make sure the logs will only be shown on develop and staging environment.
        return SiteGlobals.environment == "development" || SiteGlobals.environment == "staging"
    }

    private fun formatMessage(tag: String?, message: String): String {
        return if (tag != null) "[$tag] $message" else message
    }

    /**
     * Logs a debug message. Only visible in development and staging.
     * @param message The message to log
     */
    fun debug(message: String, vararg o: Any?) {
        if (shouldLog()) console.log(message, *o)
    }

    /**
     * Logs a debug message with a tag for filtering/grouping.
     * @param tag Tag for filtering/grouping logs
     * @param message The message to log
     * @param o Optional additional objects to log (e.g. for structured logging or stack traces)
     */
    fun debug(tag: String, message: String, vararg o: Any?) {
        if (shouldLog()) console.log(formatMessage(tag, message), *o)
    }

    /**
     * Logs an info message. Only visible in development and staging.
     * @param message The message to log
     * @param o Optional additional objects to log (e.g. for structured logging or stack traces)
     */
    fun info(message: String, vararg o: Any?) {
        if (shouldLog()) console.info(message, *o)
    }

    /**
     * Logs an info message with a tag for filtering/grouping.
     * @param tag Tag for filtering/grouping logs
     * @param message The message to log
     * @param o Optional additional objects to log (e.g. for structured logging or stack traces)
     */
    fun info(tag: String, message: String, vararg o: Any?) {
        if (shouldLog()) console.info(formatMessage(tag, message), *o)
    }

    /**
     * Logs a warning message. Only visible in development and staging.
     * @param message The message to log
     * @param o Optional additional objects to log (e.g. for structured logging or stack traces)
     */
    fun warn(message: String, vararg o: Any?) {
        if (shouldLog()) console.warn(message, *o)
    }

    /**
     * Logs a warning message with a tag for filtering/grouping.
     * @param tag Tag for filtering/grouping logs
     * @param message The message to log
     * @param o Optional additional objects to log (e.g. for structured logging or stack traces)
     */
    fun warn(tag: String, message: String, vararg o: Any?) {
        if (shouldLog()) console.warn(formatMessage(tag, message), *o)
    }

    /**
     * Logs an error message. Only visible in development and staging.
     * @param message The message to log
     * @param o Optional additional objects to log (e.g. for structured logging or stack traces)
     */
    fun error(message: String, vararg o: Any?) {
        if (shouldLog()) console.error(message, *o)
    }

    /**
     * Logs an error message with a tag for filtering/grouping.
     * @param tag Tag for filtering/grouping logs
     * @param message The message to log
     * @param o Optional additional objects to log (e.g. for structured logging or stack traces)
     */
    fun error(tag: String, message: String, vararg o: Any?) {
        if (shouldLog()) console.error(formatMessage(tag, message), *o)
    }
}


