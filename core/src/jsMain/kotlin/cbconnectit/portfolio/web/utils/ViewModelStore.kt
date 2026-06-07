package cbconnectit.portfolio.web.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.varabyte.kobweb.core.rememberPageContext

/**
 * Global store for ViewModels that persists across navigation events.
 * This ensures ViewModels are not recreated when using browser back/forward navigation.
 *
 * Implements an LRU (Least Recently Used) eviction strategy to prevent unbounded memory growth
 * in long-running sessions, particularly with dynamic search pages that can generate many unique keys.
 * 
 * When a ViewModel is evicted, its dispose() method is called to clean up resources.
 */
object ViewModelStore {
    private const val MAX_STORE_SIZE = 50 // Maximum number of ViewModels to cache

    private val store = mutableMapOf<String, ViewModel>()
    private val accessOrder = mutableListOf<String>() // Track access order for LRU

    /**
     * Get or create a ViewModel with the given key.
     * The ViewModel will be cached and reused for the same key.
     * When the cache exceeds MAX_STORE_SIZE, the least recently used ViewModel is evicted.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ViewModel> getOrCreate(key: String, factory: () -> T): T {
        val existing = store[key]

        return if (existing != null) {
            // Update access order (move to end = most recently used)
            accessOrder.remove(key)
            accessOrder.add(key)
            existing as T
        } else {
            val newInstance = factory()
            store[key] = newInstance
            accessOrder.add(key)

            // Evict least recently used if store is too large
            if (store.size > MAX_STORE_SIZE) {
                val lruKey = accessOrder.removeFirstOrNull()
                if (lruKey != null) {
                    val evicted = store.remove(lruKey)
                    evicted?.dispose()
                    Logger.debug("ViewModelStore", "Evicted and disposed LRU ViewModel with key '$lruKey' (store size: ${store.size})")
                }
            }

            newInstance
        }
    }

    /**
     * Remove a ViewModel from the store and dispose it.
     */
    fun remove(key: String) {
        val removed = store.remove(key)
        accessOrder.remove(key)
        removed?.dispose()
    }

    fun clear() {
        store.values.forEach { it.dispose() }
        store.clear()
        accessOrder.clear()
    }

    fun size(): Int = store.size
}

/**
 * Remember a ViewModel with a stable key that persists across navigation.
 *
 * This is specifically designed for Kobweb/Compose for Web where browser back/forward
 * navigation unmounts and remounts the entire composition, which would normally
 * cause ViewModels to be recreated.
 *
 * The [key] defaults to the current page's `pathQueryAndFragment` (path + query string + fragment),
 * which uniquely identifies the page including any dynamic route parameters and query string.
 * Override it when you need to share a single ViewModel across multiple pages, or when
 * a component-level (rather than page-level) key is more appropriate.
 *
 * **Important**: This function caches ViewModels globally. For pages where you don't want
 * caching (e.g., create/edit forms), use `cached = false` instead.
 *
 * @param key Unique key for this ViewModel instance. Defaults to the current page's path + query + fragment.
 * @param cached If true (default), the ViewModel is stored in [ViewModelStore] and survives navigation.
 *               If false, a fresh instance is created every time and disposed when the composable leaves composition.
 * @param factory Lambda to create the ViewModel if not already cached.
 * @return The ViewModel instance.
 */
@Composable
fun <T : ViewModel> rememberViewModel(
    key: String = rememberPageContext().route.path,
    cached: Boolean = true,
    factory: () -> T
): T {
    return if (cached) {
        // Cached version - persists across navigation
        ViewModelStore.getOrCreate(key, factory)
    } else {
        // Non-cached version - disposed when composable leaves composition
        val viewModel = remember(key) { factory() }

        DisposableEffect(key) {
            onDispose {
                viewModel.dispose()
                Logger.debug("ViewModel", "Disposed non-cached ViewModel with key '$key'")
            }
        }

        viewModel
    }
}
