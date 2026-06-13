package cbconnectit.portfolio.web.components

import androidx.compose.runtime.*
import com.materialkobweb.components.widgets.BorderRadius
import com.materialkobweb.components.widgets.DsFloatingActionButton
import com.materialkobweb.components.widgets.DsMaterialSymbols
import com.materialkobweb.components.widgets.FilledIconButtonStyle
import com.materialkobweb.extensions.ButtonSizeXL
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.minus
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

    // TODO: look into rotation style on hover
    //     base {
    //        Modifier.rotate(a = 180.deg)
    //            .transition(Transition.of("rotate", 300.ms))
    //    }
    //    hover {
    //        Modifier.rotate(a = 0.deg)
    //    }

    DsFloatingActionButton(
        modifier = Modifier
            .position(Position.Fixed)
            .visibility(if ((scroll ?: 0.0) > 400.0) Visibility.Visible else Visibility.Hidden)
            .pointerEvents(PointerEvents.Auto)
            .right(if (breakpoint <= Breakpoint.SM) 30.px else 100.px)
            .bottom(if (breakpoint <= Breakpoint.SM) 30.px else 100.px)
            .setVariable(ButtonVars.BorderRadius, 12.px),
        onClick = {
            document.documentElement?.scrollTo(x = 0.0, y = 0.0)
        },
        variant = FilledIconButtonStyle,
        enabled = true,
        size = ButtonSizeXL
    ) {
        DsMaterialSymbols(
            modifier = Modifier.fontSize(ButtonVars.Height.value() - ButtonVars.PaddingHorizontal.value()),
            icon = "arrow_upward"
        )
    }
}