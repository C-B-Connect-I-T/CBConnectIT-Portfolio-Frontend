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

object Id {
    const val adminSearchBar = "adminSearchBar"
    const val usernameInput = "usernameInput"
    const val passwordInput = "passwordInput"
    const val svgParent = "svgParent"
    const val vectorIcon = "vectorIcon"
    const val navigationText = "navigationText"
    const val editorPreview = "editorPreview"

    const val titleInput = "titleInput"
    const val subtitleInput = "subtitleInput"
    const val thumbnailInput = "thumbnailInput"
    const val contentInput = "contentInput"

    const val linkHrefInput = "linkHrefInput"
    const val linkTitleInput = "linkTitleInput"

    const val emailInput = "emailInput"
    const val postContent = "postContent"
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

    object PathIcon {
        const val home =
            "M3 12L5 10M5 10L12 3L19 10M5 10V20C5 20.2652 5.10536 20.5196 5.29289 20.7071C5.48043 20.8946 5.73478 21 6 21H9M19 10L21 12M19 10V20C19 20.2652 18.8946 20.5196 18.7071 20.7071C18.5196 20.8946 18.2652 21 18 21H15M9 21C9.26522 21 9.51957 20.8946 9.70711 20.7071C9.89464 20.5196 10 20.2652 10 20V16C10 15.7348 10.1054 15.4804 10.2929 15.2929C10.4804 15.1054 10.7348 15 11 15H13C13.2652 15 13.5196 15.1054 13.7071 15.2929C13.8946 15.4804 14 15.7348 14 16V20C14 20.2652 14.1054 20.5196 14.2929 20.7071C14.4804 20.8946 14.7348 21 15 21M9 21H15"
        const val create =
            "M12 9.52148V12.5215M12 12.5215V15.5215M12 12.5215H15M12 12.5215H9M21 12.5215C21 13.7034 20.7672 14.8737 20.3149 15.9656C19.8626 17.0576 19.1997 18.0497 18.364 18.8854C17.5282 19.7212 16.5361 20.3841 15.4442 20.8364C14.3522 21.2887 13.1819 21.5215 12 21.5215C10.8181 21.5215 9.64778 21.2887 8.55585 20.8364C7.46392 20.3841 6.47177 19.7212 5.63604 18.8854C4.80031 18.0497 4.13738 17.0576 3.68508 15.9656C3.23279 14.8737 3 13.7034 3 12.5215C3 10.1345 3.94821 7.84535 5.63604 6.15752C7.32387 4.4697 9.61305 3.52148 12 3.52148C14.3869 3.52148 16.6761 4.4697 18.364 6.15752C20.0518 7.84535 21 10.1345 21 12.5215Z"
        const val posts =
            "M9 5H7C6.46957 5 5.96086 5.21071 5.58579 5.58579C5.21071 5.96086 5 6.46957 5 7V19C5 19.5304 5.21071 20.0391 5.58579 20.4142C5.96086 20.7893 6.46957 21 7 21H17C17.5304 21 18.0391 20.7893 18.4142 20.4142C18.7893 20.0391 19 19.5304 19 19V7C19 6.46957 18.7893 5.96086 18.4142 5.58579C18.0391 5.21071 17.5304 5 17 5H15M9 5C9 5.53043 9.21071 6.03914 9.58579 6.41421C9.96086 6.78929 10.4696 7 11 7H13C13.5304 7 14.0391 6.78929 14.4142 6.41421C14.7893 6.03914 15 5.53043 15 5M9 5C9 4.46957 9.21071 3.96086 9.58579 3.58579C9.96086 3.21071 10.4696 3 11 3H13C13.5304 3 14.0391 3.21071 14.4142 3.58579C14.7893 3.96086 15 4.46957 15 5M12 12H15M12 16H15M9 12H9.01M9 16H9.01"
        const val logout =
            "M11 16.5215L7 12.5215M7 12.5215L11 8.52148M7 12.5215H21M16 16.5215V17.5215C16 18.3171 15.6839 19.0802 15.1213 19.6428C14.5587 20.2054 13.7956 20.5215 13 20.5215H6C5.20435 20.5215 4.44129 20.2054 3.87868 19.6428C3.31607 19.0802 3 18.3171 3 17.5215V7.52148C3 6.72583 3.31607 5.96277 3.87868 5.40016C4.44129 4.83755 5.20435 4.52148 6 4.52148H13C13.7956 4.52148 14.5587 4.83755 15.1213 5.40016C15.6839 5.96277 16 6.72583 16 7.52148V8.52148"
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

    object PortfolioCards {
        const val scrollableContainer = "scrollableContainer"
    }

    object PortfolioCard {
        const val columnParent = "columnParent"
        const val boxParent = "boxParent"
        const val greenOverlay = "greenOverlay"
        const val linkIcon = "linkIcon"

        const val portfolioTitle = "portfolioTitle"
        const val portfolioDesc = "portfolioDesc"
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

    object ClassNames {
        const val formLabel = "form-label"
        const val formControl = "form-control"
    }

    object ContactForm {
        const val inputName = "inputName"
        const val inputEmail = "inputEmail"
        const val inputMessage = "inputMessage"
    }
}