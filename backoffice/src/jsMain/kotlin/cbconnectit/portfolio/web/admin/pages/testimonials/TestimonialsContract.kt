package cbconnectit.portfolio.web.admin.pages.testimonials

import cbconnectit.portfolio.web.data.models.domain.Testimonial
import cbconnectit.portfolio.web.utils.MVI

interface TestimonialsContract :
    MVI<TestimonialsContract.State, TestimonialsContract.Intent, TestimonialsContract.Effect> {

    data class State(
        val testimonials: List<Testimonial> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data object LoadTestimonials : Intent()
        data class NavigateToManage(val testimonialId: String? = null) : Intent()
    }

    sealed class Effect {
        data class NavigateToManage(val testimonialId: String? = null) : Effect()
    }
}
