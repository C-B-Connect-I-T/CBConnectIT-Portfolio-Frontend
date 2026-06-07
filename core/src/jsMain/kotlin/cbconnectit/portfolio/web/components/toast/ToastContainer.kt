package cbconnectit.portfolio.web.components.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

/**
 * Toast container component that displays all active toasts.
 * This should be added at the root level of your application (in AppEntry).
 *
 * @param position The position where toasts should appear (default: BOTTOM_RIGHT)
 */
@Composable
fun ToastContainer(
    position: ToastPosition = ToastPosition.DEFAULT
) {
    val toasts by ToastManager.toasts.collectAsState()

    if (toasts.isEmpty()) return

    val positionModifier = when (position) {
        ToastPosition.TOP_LEFT -> Modifier.top(24.px).left(24.px).alignItems(AlignItems.FlexStart)
        ToastPosition.TOP_CENTER -> Modifier.top(24.px).left(0.px).right(0.px).alignItems(AlignItems.Center)
        ToastPosition.TOP_RIGHT -> Modifier.top(24.px).right(24.px).alignItems(AlignItems.FlexEnd)
        ToastPosition.BOTTOM_LEFT -> Modifier.bottom(24.px).left(24.px).alignItems(AlignItems.FlexStart)
        ToastPosition.BOTTOM_CENTER -> Modifier.bottom(24.px).left(0.px).right(0.px).alignItems(AlignItems.Center)
        ToastPosition.BOTTOM_RIGHT -> Modifier.bottom(24.px).right(24.px).alignItems(AlignItems.FlexEnd)
    }

    Column(
        modifier = Modifier
            .position(Position.Fixed)
            .zIndex(9999)
            .gap(8.px)
            .maxWidth(90.percent) // Prevent overflow on small screens
            .padding(leftRight = 12.px) // Add horizontal padding for mobile
            .then(positionModifier)
    ) {
        toasts.forEach { toast ->
            Toast(
                toast = toast,
                onDismiss = { ToastManager.remove(toast.id) }
            )
        }
    }
}
