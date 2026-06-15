package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import com.materialkobweb.styles.MaterialColorVars
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.overlay.Overlay
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun Dialog(
    onHideDialog: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val breakpoint = rememberBreakpoint()

    Overlay(
        modifier = Modifier
            .zIndex(100)
            .onClick {
                onHideDialog()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 50.percent else 90.percent)
                .maxWidth(500.px)
                .backgroundColor(MaterialColorVars.SurfaceContainer.value())
                .borderRadius(24.px)
                .color(MaterialColorVars.OnSurface.value())
                .padding(24.px)
                .onClick {
                    // Prevent click from propagating to the overlay background and closing unintentionally
                    it.stopPropagation()
                },
            content = content
        )
    }
}
