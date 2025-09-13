package cbconnectit.portfolio.web.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MVI<STATE, INTENT, EFFECT> {
    val state: StateFlow<STATE>
    val effect: SharedFlow<EFFECT>
    fun sendIntent(intent: INTENT): Job
    fun emitEffect(effect: EFFECT): Job

    fun updateState(block: (STATE) -> STATE) = Unit
}
