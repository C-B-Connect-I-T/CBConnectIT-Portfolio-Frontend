package cbconnectit.portfolio.web.utils

import com.varabyte.kobweb.core.AppGlobals
import com.varabyte.kobweb.core.isExporting

object SiteGlobals {
    val baseUrl: String by lazy {
        AppGlobals["BASE_URL"] ?: ""
    }

    // <editor-fold desc="environment">

    // Lazy-initialized AppGlobals value (only accessed when needed, not during tests if override is set)
    private val _appEnvironment: String by lazy {
        AppGlobals["ENVIRONMENT"] ?: "development"
    }

    // Internal test override — only used in tests. Reset to null to restore AppGlobals behaviour.
    internal var _testEnvironmentOverride: String? = null

    val environment: String
        get() = _testEnvironmentOverride ?: _appEnvironment
    // </editor-fold>

    val isExporting get() = AppGlobals.isExporting
}
