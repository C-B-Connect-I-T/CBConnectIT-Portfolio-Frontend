package cbconnectit.portfolio.web

import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px

object Tag {
    const val video = "video"
}

object Attr {

    const val StrokeWidth = "stroke-width"
    const val Fill = "fill"
    const val Border = "border"

    const val Loop = "loop"
    const val Muted = "muted"
    const val Poster = "poster"
    const val OnCanPlay = "oncanplay"
    const val Controls = "controls"
    const val Autoplay = "autoplay"
    const val PlaysInline = "playsinline"
    const val Preload = "preload"
    const val Src = "src"
}

object Constants {
    const val BASE_URL = "BASE_URL"
}

object G {
    object Ui {
        object Width {
            val Small = 200.px
            val Medium = 400.px
            val Large = 600.px
        }

        object Text {
            val Small = 18.px
            val MediumSmall = 22.px
            val Medium = 28.px
            val Large = 38.px
        }
    }
}

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
