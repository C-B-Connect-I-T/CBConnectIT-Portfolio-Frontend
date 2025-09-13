package cbconnectit.portfolio.web.pages

import cbconnectit.portfolio.web.data.repos.ExperienceRepo
import cbconnectit.portfolio.web.data.repos.ProjectRepo
import cbconnectit.portfolio.web.data.repos.ServiceRepo
import cbconnectit.portfolio.web.data.repos.TestimonialRepo
import cbconnectit.portfolio.web.utils.MVI
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val serviceRepo: ServiceRepo = ServiceRepo,
    private val projectRepo: ProjectRepo = ProjectRepo,
    private val testimonialRepo: TestimonialRepo = TestimonialRepo,
    private val experienceRepo: ExperienceRepo = ExperienceRepo,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : MVI<HomeContract.State, HomeContract.Intent, HomeContract.Effect> {
    private val _state = MutableStateFlow(HomeContract.State())
    override val state: StateFlow<HomeContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeContract.Effect>()
    override val effect: SharedFlow<HomeContract.Effect> = _effect.asSharedFlow()

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