package cbconnectit.portfolio.web

import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.theme.colors.ColorMode

val ColorMode.backdropGradient: LinearGradient
    get() = if (this.isLight) {
        linearGradient(LinearGradient.Direction.ToBottomRight) {
            add(Color.rgb(237, 240, 242))
            add(Colors.White)
        }
    } else {
        linearGradient(LinearGradient.Direction.ToBottomRight) {
            add(Color.rgb(20, 22, 28))
            add(Color.rgb(76, 79, 82))
        }
    }

val ColorMode.primaryGradient: LinearGradient
    get() = linearGradient(LinearGradient.Direction.ToBottomRight) {
        add(this@primaryGradient.toColorScheme.primary)
        add(this@primaryGradient.toColorScheme.primary)
    }
