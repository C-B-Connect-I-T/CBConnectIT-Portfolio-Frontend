package cbconnectit.portfolio.web.components

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.styles.BackToTopButtonStyle
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.icons.fa.FaArrowUp
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun BackToTopButton() {
    val breakpoint = rememberBreakpoint()
    var scroll: Double? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        window.addEventListener(type = "scroll", callback = {
            scroll = document.documentElement?.scrollTop
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .zIndex(1)
            .styleModifier {
                property("pointer-events", "none")
            },
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        val startingModifier = if (breakpoint > Breakpoint.MD) {
            BackToTopButtonStyle.toModifier()
        } else Modifier

        Box(
            modifier = startingModifier
                .size(50.px)
                .visibility(if ((scroll ?: 0.0) > 400.0) Visibility.Visible else Visibility.Hidden)
                .borderRadius(20.percent)
                .margin(right = if (breakpoint <= Breakpoint.SM) 30.px else 40.px, bottom = if (breakpoint <= Breakpoint.SM) 30.px else 40.px)
                .backgroundColor(ColorMode.current.toColorScheme.primary)
                .cursor(Cursor.Pointer)
                .onClick {
                    document.documentElement?.scrollTo(x = 0.0, y = 0.0)
                }
                .styleModifier {
                    property("pointer-events", "auto")
                },
            contentAlignment = Alignment.Center
        ) {
            FaArrowUp(
                modifier = Modifier.color(ColorMode.current.toColorScheme.onPrimary),
                size = IconSize.LG
            )
        }
    }
}