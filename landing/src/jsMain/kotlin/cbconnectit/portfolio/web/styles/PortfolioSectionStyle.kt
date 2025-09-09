package cbconnectit.portfolio.web.styles

import cbconnectit.portfolio.web.utils.Identifiers.PortfolioCard.boxParent
import cbconnectit.portfolio.web.utils.Identifiers.PortfolioCard.columnParent
import cbconnectit.portfolio.web.utils.Identifiers.PortfolioCard.greenOverlay
import cbconnectit.portfolio.web.utils.Identifiers.PortfolioCard.linkIcon
import cbconnectit.portfolio.web.utils.Identifiers.PortfolioCard.portfolioDesc
import cbconnectit.portfolio.web.utils.Identifiers.PortfolioCard.portfolioTitle
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

// Passing the animation through from the parent to a child can be done like this.
// Id's need to be added to the views and then can be accessed like this.
val PortfolioSectionStyle = CssStyle {
    cssRule(" > #$columnParent > #${boxParent} > #${greenOverlay}") {
        Modifier.width(0.px)
            .transition(Transition.of(property = "width", duration = 500.ms))
    }

    cssRule(":hover > #$columnParent > #${boxParent} > #${greenOverlay}") {
        Modifier.width(300.px)
    }

    cssRule(" > #$columnParent > #${boxParent} > #${greenOverlay} > #${linkIcon}") {
        Modifier.visibility(Visibility.Hidden)
    }

    cssRule(":hover > #$columnParent > #${boxParent} > #${greenOverlay} > #${linkIcon}") {
        Modifier.visibility(Visibility.Visible)
    }

    cssRule(" > #$columnParent > #${portfolioTitle}") {
        Modifier
            .translateX(0.percent)
            .transition(
                Transition.of(property = "color", duration = 200.ms),
                Transition.of(property = "translate", duration = 200.ms)
            )
    }

    cssRule(":hover > #$columnParent > #${portfolioTitle}") {
        Modifier
            .translateX(5.percent)
    }

    cssRule(" > #$columnParent > #${portfolioDesc}") {
        Modifier
            .translateX(0.percent)
            .transition(Transition.of(property = "translate", duration = 200.ms))
    }

    cssRule(":hover > #$columnParent > #${portfolioDesc}") {
        Modifier
            .translateX(5.percent)
    }
}

val PortfolioArrowIconStyle = CssStyle {
    base {
        Modifier.color(colorMode.toColorScheme.surface)
            .transition(Transition.of(property = "color", duration = 200.ms))
    }

    hover {
        Modifier.color(colorMode.toColorScheme.primary)
    }
}