package cbconnectit.portfolio.web.components.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cbconnectit.portfolio.web.styles.InfoColor
import cbconnectit.portfolio.web.styles.OnInfoColor
import cbconnectit.portfolio.web.styles.OnSuccessColor
import cbconnectit.portfolio.web.styles.OnWarningColor
import cbconnectit.portfolio.web.styles.SuccessColor
import cbconnectit.portfolio.web.styles.WarningColor
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.animation.Keyframes
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba

val SlideIn = Keyframes {
    from { Modifier.translateX((100).percent).opacity(0) }
    to { Modifier.translateX((0).percent).opacity(1) }
}

val SlideOut = Keyframes {
    from { Modifier.translateX((0).percent).opacity(1) }
    to { Modifier.translateX((120).percent).opacity(0) }
}

/**
 * Individual toast notification component with slide-in/slide-out animations.
 *
 * @param toast The toast data to display
 * @param onDismiss Callback when the toast should be dismissed
 */
@Composable
fun Toast(
    toast: ToastData,
    onDismiss: () -> Unit
) {
    val colorMode = ColorMode.current
    val colorScheme = colorMode.toColorScheme

    var isVisible by remember { mutableStateOf(false) }
    var isExiting by remember { mutableStateOf(false) }

    // Auto-hide after duration
    LaunchedEffect(toast.id) {
        // Trigger slide-in animation
        delay(50)
        isVisible = true

        // Wait for auto-hide duration
        delay(ToastManager.AUTO_HIDE_DURATION_MS)

        // Trigger slide-out animation
        isExiting = true
        delay(300) // Wait for animation to complete
        onDismiss()
    }

    val (containerColor, contentColor) = when (toast.type) {
        ToastType.SUCCESS -> colorMode.SuccessColor to colorMode.OnSuccessColor
        ToastType.ERROR -> colorScheme.errorContainer to colorScheme.onErrorContainer
        ToastType.WARNING -> colorMode.WarningColor to colorMode.OnWarningColor
        ToastType.INFO -> colorMode.InfoColor to colorMode.OnInfoColor
    }

    Box(
        modifier = Modifier
            .minWidth(280.px)
            .maxWidth(350.px)
            .backgroundColor(containerColor)
            .borderRadius(8.px)
            .boxShadow(
                offsetX = 0.px,
                offsetY = 4.px,
                blurRadius = 12.px,
                spreadRadius = 0.px,
                color = rgba(0, 0, 0, 0.15)
            )
            .padding(leftRight = 16.px, topBottom = 12.px)
            .margin(bottom = 8.px)
            .overflow(Overflow.Hidden) // Prevent horizontal overflow
            .overflowWrap(OverflowWrap.BreakWord)
            .then(
                when {
                    isExiting -> Modifier.animation(SlideOut.toAnimation(duration = 300.ms, timingFunction = AnimationTimingFunction.EaseOut, iterationCount = AnimationIterationCount.of(1)))
                    isVisible -> Modifier.animation(SlideIn.toAnimation(duration = 300.ms, timingFunction = AnimationTimingFunction.EaseIn, iterationCount = AnimationIterationCount.of(1)))
                    else -> Modifier.opacity(0)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gap(4.px)
        ) {
            // Title (only if provided)
            toast.title?.let { title ->
                SpanText(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .color(contentColor)
                        .fontSize(14.px)
                        .fontWeight(FontWeight.SemiBold)
                )
            }

            // Message
            SpanText(
                text = toast.message,
                modifier = Modifier
                    .fillMaxWidth()
                    .color(contentColor)
                    .fontSize(13.px)
                    .fontWeight(if (toast.title == null) FontWeight.Medium else FontWeight.Normal)
            )
        }
    }
}
