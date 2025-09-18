package cbconnectit.portfolio.web.navigation

import cbconnectit.portfolio.web.utils.Res

sealed class Navigation(val route: String) {
    sealed class Screen(route: String) : Navigation(route) {
        data object Home : Screen("/") {
            val HomeSection = SectionItem("home", Res.String.Home, "", "#home")
            val AboutSection = SectionItem("about", Res.String.AboutMe, Res.String.AboutMeSubtitle, "#about")
            val ServiceSection = SectionItem("service", Res.String.Service, Res.String.ServiceSubtitle, "#service")
            val PortfolioSection = SectionItem("portfolio", Res.String.Portfolio, Res.String.PortfolioSubtitle, "#portfolio")
            val ExperienceSection = SectionItem("experience", Res.String.Experience, Res.String.ExperienceSubtitle, "#experience")
            val ContactSection = SectionItem("contact", Res.String.ContactMe, Res.String.ContactMeSubtitle, "#contact")
            val TestimonialSection = SectionItem("testimonial", Res.String.Testimonial, Res.String.TestimonialsSubtitle, "#testimonial")
            val AchievementsSection = SectionItem("achievements", Res.String.Achievements, Res.String.AchievementsSubtitle, "#achievements")
        }

        data object Services : Screen("/services") {
            fun getService(id: String) = "$route/service/?serviceId=$id"
        }

        data object Projects : Screen("/projects") {
            fun getByTagQuery(tagQuery: String) = "$route/?$tagQuery"
            fun getProject(id: String) = "$route/?projectId=$id"
        }
    }

    sealed class External(route: String) : Navigation(route) {
        data object LinkedIn : Navigation("https://www.linkedin.com/in/christiano-bolla/")
        data object Github : Navigation("https://github.com/ShaHar91")
    }
}

data class SectionItem(val id: String, val title: String, val subtitle: String, val path: String)
