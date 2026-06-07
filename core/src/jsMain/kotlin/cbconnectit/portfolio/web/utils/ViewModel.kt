package cbconnectit.portfolio.web.utils

import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Base class for all ViewModels in the application.
 * 
 * Provides lifecycle management through automatic coroutine cancellation when disposed.
 * This class should be extended by all ViewModel classes, regardless of whether
 * they use the MVI pattern or another architecture pattern.
 * 
 * The [coroutineScope] is lazily initialized on first access and provides a stable
 * instance throughout the ViewModel's lifetime. This follows the pattern used by
 * Android's ViewModel.viewModelScope.
 * 
 * Example usage:
 * ```
 * class MyViewModel : ViewModel(), MVI<State, Intent, Effect> {
 *     // coroutineScope is automatically available via lazy initialization
 *     
 *     override fun sendIntent(intent: Intent) = coroutineScope.launch {
 *         // Handle intent
 *     }
 * }
 * ```
 * 
 * To customize the coroutine scope configuration, override [createCoroutineScope]:
 * ```
 * class MyViewModel : ViewModel(), MVI<State, Intent, Effect> {
 *     override fun createCoroutineScope(): CoroutineScope {
 *         return CoroutineScope(SupervisorJob() + Dispatchers.Main)
 *     }
 * }
 * ```
 */
abstract class ViewModel {
    
    private var _coroutineScope: CoroutineScope? = null
    
    /**
     * The coroutine scope for this ViewModel.
     * All coroutines launched in the ViewModel should use this scope.
     * 
     * This scope is lazily initialized on first access using [createCoroutineScope].
     * The same instance is returned for all subsequent accesses.
     * 
     * The scope is automatically cancelled when [dispose] is called.
     * 
     * To customize the scope configuration, override [createCoroutineScope].
     */
    val coroutineScope: CoroutineScope
        get() {
            return _coroutineScope ?: synchronized(this) {
                _coroutineScope ?: createCoroutineScope().also { _coroutineScope = it }
            }
        }

    /**
     * Creates the [CoroutineScope] for this ViewModel.
     * 
     * The default implementation uses [SupervisorJob] + [Dispatchers.Default].
     * Override this method to provide a custom scope configuration.
     * 
     * This method is called lazily on first access to [coroutineScope].
     * 
     * Common configurations:
     * - Default (background work): `CoroutineScope(SupervisorJob() + Dispatchers.Default)`
     * - Main thread: `CoroutineScope(SupervisorJob() + Dispatchers.Main)`
     * - With error handling: `CoroutineScope(SupervisorJob() + Dispatchers.Default + handler)`
     * 
     * @return A new [CoroutineScope] instance
     */
    protected open fun createCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    /**
     * Clean up all resources owned by this ViewModel.
     * 
     * This method is automatically called when:
     * - The ViewModel is evicted from ViewModelStore (LRU eviction)
     * - rememberViewModel is called with cached = false and the composable is disposed
     * - ViewModelStore.remove() is called explicitly
     * - ViewModelStore.clear() is called
     * 
     * The default implementation cancels the [coroutineScope] if it has been initialized.
     * Override this method if you need additional cleanup logic, but make sure
     * to call super.dispose() to ensure the coroutine scope is cancelled.
     * 
     * Example with additional cleanup:
     * ```
     * override fun dispose() {
     *     // Custom cleanup
     *     myListener.remove()
     *     super.dispose()
     * }
     * ```
     */
    open fun dispose() {
        _coroutineScope?.cancel()
        _coroutineScope = null
    }
}

