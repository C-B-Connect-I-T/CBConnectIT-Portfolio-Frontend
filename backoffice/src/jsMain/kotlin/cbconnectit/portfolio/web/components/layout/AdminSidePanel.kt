package cbconnectit.portfolio.web.components.layout

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.components.AdminNavigationItems
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.logoImage
import com.materialkobweb.constants.Constants
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.OverscrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.overscrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun AdminSidePanel() {
    val ctx = rememberPageContext()
    val colorMode = ColorMode.current

    Column(
        modifier = Modifier
            .padding(topBottom = 50.px, leftRight = 40.px)
            .fillMaxWidth(30.percent)
            .maxWidth(Constants.SIDE_PANEL_WIDTH.px)
            .height(100.vh)
            .position(Position.Sticky)
            .top(0.px)
            .backgroundColor(colorMode.toColorScheme.surfaceContainer)
            .color(colorMode.toColorScheme.onSurface)
            .overflow(Overflow.Hidden, Overflow.Auto)
            .overscrollBehavior(OverscrollBehavior.Contain)
            .alignSelf(AlignSelf.Start),
        verticalArrangement = Arrangement.spacedBy(10.px),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            modifier = Modifier
                .maxWidth(250.px)
                .fillMaxWidth()
                .margin(bottom = 60.px)
                .cursor(Cursor.Pointer)
                .onClick {
                    ctx.router.navigateTo(Navigation.Screen.Admin.Home.route)
                },
            src = logoImage(colorMode),
            alt = "Logo Image"
        )

        AdminNavigationItems()
    }
}
