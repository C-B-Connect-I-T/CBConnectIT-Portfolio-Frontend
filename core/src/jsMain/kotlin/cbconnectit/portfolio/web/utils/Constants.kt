package cbconnectit.portfolio.web.utils


object Constants {
    const val HEADER_HEIGHT = 85
    const val HEADER_COLLAPSED_LOGO_HEIGHT = 25
    const val SECTION_WIDTH = 1540

    const val QUERY_PARAM = "query"

    const val LOREM_IPSUM_SHORTEST =
        "Lorum ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    const val LOREM_IPSUM_SHORT =
        "Lorum ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorum ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorum ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."

    const val SECTION_PADDING = 100

}

object Res {

    object String {
        const val Home = "Home"
        const val DocumentTitle = "{0} | C.B. Connect I.T"
        const val Projects = "Projects"
        const val PickCategory = "Pick a category"
        const val FilterCategories = "Categories: {0}"
        const val All = "All"
        const val ServiceDocumentTitle = "Services"
        const val SubServicesDocumentTitle = "Services: {0}"

        const val LearnMore = "Learn more"
        const val ReadMore = "Read more"

        const val MyServices = "My Services"
        const val ServicesBannerDescription =
            "Over the years, my knowledge and repertoire has grown and I can be deployed in multiple areas. I am always willing to learn and strive to push my limits. Besides implementing an existing idea, I also like to think along with the customer for new ideas and features.\n\nMy services include, but do not limit, to the following:"
        const val TechnologyStacks = "Technology Stacks"

        const val AboutContent =
            "Very skilled Android Developer with close to 7 years of experience in developing applications using Java and Kotlin. Someone who has a passion for staying up-to-date with all new technologies, constantly seeking to explore and take advantage of the latest advancements in the Android Framework. Committed to delivering robust, user-friendly, and scalable applications. A fast learner with an ability to adapt quickly to new technologies and a strong focus for code quality and best practices."
        const val ExperienceInYears = "{0}+ years"
        const val Completed = "Completed"
        const val CompletedProjects = "15+ projects"

        const val IntroductionHello = "Hello, I am"
        const val IntroductionName = "Christiano Bolla"
        const val IntroductionFunction = "Mobile developer"
        const val IntroductionBody = "I have been working since 2017 as an Android Developer. Proficient in turning an idea into a fully functioning and UI/UX stunning application."
        const val LetsChat = "Let's chat!"

        const val AboutMe = "About me"
        const val AboutMeSubtitle = "Why Hire Me?"
        const val Service = "Service"
        const val ServiceSubtitle = "I'm Good At"
        const val Portfolio = "Portfolio"
        const val PortfolioSubtitle = "My Work"
        const val Experience = "Experience"
        const val ExperienceSubtitle = "Work Experience"
        const val ContactMe = "Contact me"
        const val ContactMeSubtitle = "Get In Touch"
        const val Testimonial = "Testimonial"
        const val TestimonialsSubtitle = "What They Say"
        const val Achievements = "Achievements"
        const val AchievementsSubtitle = "Personal Achievements"

        const val Name = "Name"
        const val FullName = "Full Name"
        const val Email = "Email"
        const val EmailAddress = "Email Address"
        const val Message = "Message"
        const val YourMessage = "Your Message"
        const val Submit = "Submit"

        const val ToggleColorMode = "Toggle color mode"
        const val SeeAll = "See all"
    }

    object Image {
        const val logo = "/logo.svg"
        const val logoDark = "/logo_dark.svg"
        const val mainImage = "/images/img_person_main.png"

        const val laugh = "/laugh.png"

        const val portfolio1 = "/portfolio1.png"

        const val android = "/images/img_android.png"
        const val tutoring = "/images/img_tutoring.png"
        const val teamwork = "/images/img_teamwork.png"

        const val services = "/images/img_services.jpg"
        const val servicesBanner = "/images/img_services_banner.jpg"

        const val servicesMobile = "/images/img_services_mobile.svg"
        const val servicesWeb = "/images/img_services_web.svg"
        const val servicesBackend = "/images/img_services_backend.svg"
        const val servicesTutoring = "/images/img_services_tutoring.svg"
    }

    object Icon {
        const val bold = "/bold.svg"
        const val italic = "/italic.svg"
        const val link = "/link.svg"
        const val title = "/title.svg"
        const val subtitle = "/subtitle.svg"
        const val quote = "/quote.svg"
        const val code = "/code.svg"
        const val image = "/image.svg"
        const val checkmark = "/checkmark.svg"

        object TechStack {
            const val android = "/icons/techStack/android.svg"
            const val angular = "/icons/techStack/angular.svg"
            const val firebase = "/icons/techStack/firebase.svg"
            const val java = "/icons/techStack/java.svg"
            const val kobweb = "/icons/techStack/kobweb.svg"
            const val kotlin = "/icons/techStack/kotlin.svg"
            const val mysql = "/icons/techStack/mysql.svg"
            const val node = "/icons/techStack/node.svg"
            const val php = "/icons/techStack/php.svg"
        }
    }
}

object Identifiers {
    object PathParams {
        const val Tag = "tag"
        const val ServiceId = "serviceId"
    }

    object ProjectsPage {
        const val dropBtn = "dropBtn"
    }

    object TestimonialSectionClasses {
        const val grid = "grid"
        const val item = "item"
        const val content = "content"
    }

    object SocialBar {
        const val socialLink = "socialLink"
        const val socialIcon = "socialIcon"
    }

    object PropertyName {
        const val gridRowEnd = "grid-row-end"
        const val gridAutoRows = "grid-auto-rows"
        const val gridRowGap = "grid-row-gap"

        const val transform = "transform"
        const val padding = "padding"
        const val margin = "margin"
    }

    object AttributeName {
        const val style = "style"
        const val method = "method"
        const val autoComplete = "autocomplete"
        const val placeholder = "placeholder"
        const val name = "name"
        const val required = "required"
        const val data1PasswordIgnore = "data-1p-ignore"
    }

    object ContactForm {
        const val inputName = "inputName"
        const val inputEmail = "inputEmail"
        const val inputMessage = "inputMessage"
    }
}