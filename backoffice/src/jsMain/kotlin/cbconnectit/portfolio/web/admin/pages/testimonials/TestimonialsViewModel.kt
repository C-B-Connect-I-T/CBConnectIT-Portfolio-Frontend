package cbconnectit.portfolio.web.admin.pages.testimonials

import cbconnectit.portfolio.web.data.models.fold
import cbconnectit.portfolio.web.data.repos.TestimonialRepo
import cbconnectit.portfolio.web.utils.ViewModel
import com.materialkobweb.components.toast.ToastManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestimonialsViewModel(
    private val testimonialRepo: TestimonialRepo = TestimonialRepo
) : ViewModel(), TestimonialsContract {
    private val _state = MutableStateFlow(TestimonialsContract.State())
    override val state: StateFlow<TestimonialsContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TestimonialsContract.Effect>()
    override val effect: SharedFlow<TestimonialsContract.Effect> = _effect.asSharedFlow()

    override fun sendIntent(intent: TestimonialsContract.Intent) = coroutineScope.launch {
        when (intent) {
            is TestimonialsContract.Intent.LoadTestimonials -> loadTestimonials()
            is TestimonialsContract.Intent.NavigateToManage ->
                emitEffect(TestimonialsContract.Effect.NavigateToManage(intent.testimonialId))
        }
    }

    private suspend fun loadTestimonials() {
        updateState { it.copy(isLoading = true) }

        testimonialRepo.getTestimonials().fold(
            onSuccess = { testimonials ->
                updateState { it.copy(testimonials = testimonials, isLoading = false) }
            },
            onError = { error ->
                updateState { it.copy(isLoading = false) }
                ToastManager.error(error.message)
            }
        )
    }

    override fun emitEffect(effect: TestimonialsContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (TestimonialsContract.State) -> TestimonialsContract.State) {
        _state.update(block)
    }
}
