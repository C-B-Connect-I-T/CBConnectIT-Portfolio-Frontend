package cbconnectit.portfolio.web.components

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.pages.sections.NavigationItems
import cbconnectit.portfolio.web.utils.logoImage
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.overlay.Overlay
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*

@Composable
fun OverlowMenu(onMenuClosed: () -> Unit) {
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    var translateX by remember { mutableStateOf((-100).percent) }
    var opacity by remember { mutableStateOf(0.percent) }

    fun CoroutineScope.closeMenu() {
        launch {
            translateX = (-100).percent
            opacity = 0.percent
            delay(500)
            onMenuClosed()
        }
    }

    LaunchedEffect(breakpoint) {
        delay(100) // This delay is needed for the translateX
        translateX = 0.percent
        opacity = 100.percent

        if (breakpoint > Breakpoint.MD) {
            scope.closeMenu()
        }
    }

    Overlay(
        modifier = Modifier
            .zIndex(2)
            .opacity(opacity)
            .transition(
                Transition.of("opacity", 500.ms)
            )
            .onClick { scope.closeMenu() }) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.vh)
                .position(Position.Fixed)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = 25.px)
                    .width(if (breakpoint < Breakpoint.MD) 50.percent else 25.percent)
                    .overflow(Overflow.Auto)
                    .scrollBehavior(ScrollBehavior.Smooth)
                    .backgroundColor(ColorMode.current.toColorScheme.background)
                    .translateX(tx = translateX)
                    .transition(Transition.of("translate", 500.ms))
            ) {
                Row(
                    modifier = Modifier
                        .margin(bottom = 25.px),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FaXmark(
                        modifier = Modifier
                            .cursor(Cursor.Pointer)
                            .margin(right = 20.px, bottom = 3.px)
                            .onClick {
                                scope.closeMenu()
                            },
                        size = IconSize.LG
                    )

                    Image(
                        modifier = Modifier.width(110.px),
                        src = logoImage(ColorMode.current),
                        alt = "Logo Image"
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.px)
                ) {
                    NavigationItems { scope.closeMenu() }
                }
            }
        }
    }
}