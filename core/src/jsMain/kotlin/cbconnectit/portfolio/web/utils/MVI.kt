package cbconnectit.portfolio.web.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.cancellation.CancellationException

/**
 * MVI (Model-View-Intent) interface for ViewModels.
 *
 * Note: ViewModels implementing MVI should also implement the ViewModel interface
 * for proper lifecycle management.
 */
interface MVI<STATE, INTENT, EFFECT> {
    val state: StateFlow<STATE>
    val effect: SharedFlow<EFFECT>
    fun sendIntent(intent: INTENT): Job
    fun emitEffect(effect: EFFECT): Job

    fun updateState(block: (STATE) -> STATE) = Unit
}

/**
 * Executes a suspend block and handles exceptions while preserving structured concurrency.
 *
 * CancellationException is always rethrown to ensure proper coroutine cancellation.
 * Other exceptions are passed to the error handler.
 *
 * @param block The suspend function to execute
 * @param onError Handler for non-cancellation exceptions
 * @return The result of the block execution, or null if an exception occurred
 */
suspend inline fun <T> catchWithCancellation(
    block: suspend () -> T,
    onError: (Exception) -> Unit = {}
): T? {
    return try {
        block()
    } catch (e: CancellationException) {
        throw e // Rethrow to preserve structured concurrency
    } catch (e: Exception) {
        onError(e)
        null
    }
}
