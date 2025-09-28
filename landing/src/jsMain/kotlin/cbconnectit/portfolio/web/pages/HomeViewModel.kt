package cbconnectit.portfolio.web.pages

import cbconnectit.portfolio.web.data.repos.ExperienceRepo
import cbconnectit.portfolio.web.data.repos.ProjectRepo
import cbconnectit.portfolio.web.data.repos.ServiceRepo
import cbconnectit.portfolio.web.data.repos.TestimonialRepo
import cbconnectit.portfolio.web.utils.MVI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.js.Date
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HomeViewModel(
    private val serviceRepo: ServiceRepo = ServiceRepo,
    private val projectRepo: ProjectRepo = ProjectRepo,
    private val testimonialRepo: TestimonialRepo = TestimonialRepo,
    private val experienceRepo: ExperienceRepo = ExperienceRepo,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : MVI<HomeContract.State, HomeContract.Intent, HomeContract.Effect> {
    private val _state = MutableStateFlow(getInitialData())
    override val state: StateFlow<HomeContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeContract.Effect>()
    override val effect: SharedFlow<HomeContract.Effect> = _effect.asSharedFlow()

    private fun getInitialData(): HomeContract.State {
        val started = Date.UTC(2017, 11).toDuration(DurationUnit.MILLISECONDS)
        val current = Date.now().toDuration(DurationUnit.MILLISECONDS)

        val yearsExperience = (current - started).inWholeDays / 365

        return HomeContract.State(
            yearsOfExperience = yearsExperience.toInt()
        )
    }

    init {
        sendIntent(HomeContract.Intent.LoadInitialData)
    }

    override fun sendIntent(intent: HomeContract.Intent) = coroutineScope.launch {
        when (intent) {
            is HomeContract.Intent.LoadInitialData -> loadInitialData()
            is HomeContract.Intent.LetsChatClicked -> emitEffect(HomeContract.Effect.NavigateToContactSection)
            is HomeContract.Intent.UpdateSelectedWork -> updateState { it.copy(selectedWork = intent.project) }
        }
    }

    private suspend fun loadInitialData() = withContext(Dispatchers.Default) {
        val servicesDeferred = async { serviceRepo.getServices() }
        val projectsDeferred = async { projectRepo.getProjects() }
        val testimonialsDeferred = async { testimonialRepo.getTestimonials() }
        val experiencesDeferred = async { experienceRepo.getExperiences() }

        val services = servicesDeferred.await()
        val projects = projectsDeferred.await()
        val testimonials = testimonialsDeferred.await()
        val experiences = experiencesDeferred.await()

        updateState {
            it.copy(
                services = services,
                projects = projects,
                testimonials = testimonials,
                experiences = experiences,
                selectedWork = projects.firstOrNull()
            )
        }
    }

    override fun emitEffect(effect: HomeContract.Effect) = coroutineScope.launch {
        _effect.emit(effect)
    }

    override fun updateState(block: (HomeContract.State) -> HomeContract.State) {
        _state.update(block)
    }
}
