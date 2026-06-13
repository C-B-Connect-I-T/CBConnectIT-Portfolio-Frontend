package cbconnectit.portfolio.web.pages

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.navigation.Navigation
import cbconnectit.portfolio.web.utils.Res
import com.materialkobweb.components.Spacer
import com.materialkobweb.components.widgets.FilledButton
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.init.InitKobweb
import com.varabyte.kobweb.core.init.InitKobwebContext
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

@InitKobweb
fun initErrorPage(ctx: InitKobwebContext) {
    ctx.router.setErrorPage {
        ErrorPage()
    }
}

@Composable
fun ErrorPage() {
    PageLayout(
        modifier = Modifier.fillMaxSize(),
        title = Res.String.NotFoundDocumentTitle,
        showMenuItems = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SpanText(
                    text = "404",
                    modifier = Modifier.fontSize(64.px)
                )

                Spacer(Modifier.height(8.px))

                SpanText(
                    text = Res.String.NotFoundTitle,
                    modifier = Modifier.fontSize(32.px)
                )

                Spacer(Modifier.height(12.px))

                SpanText(
                    text = Res.String.NotFoundDescription,
                    modifier = Modifier
                        .fontSize(18.px)
                        .textAlign(TextAlign.Center)
                )

                Spacer(Modifier.height(24.px))

                A(
                    href = Navigation.Screen.Home.route,
                    attrs = Modifier
                        .textDecorationLine(TextDecorationLine.None)
                        .toAttrs()
                ) {
                    FilledButton(onClick = {}) {
                        Text(Res.String.GoToHome)
                    }
                }
            }
        }
    }
}
