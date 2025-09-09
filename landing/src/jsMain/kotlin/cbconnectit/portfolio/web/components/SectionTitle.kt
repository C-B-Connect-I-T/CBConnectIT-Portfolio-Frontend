package cbconnectit.portfolio.web.components

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.navigation.SectionItem
import cbconnectit.portfolio.web.svg.chevronRightSvg
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Identifiers.PropertyName.margin
import cbconnectit.portfolio.web.utils.Identifiers.PropertyName.padding
import cbconnectit.portfolio.web.utils.ObserveViewportEntered
import cbconnectit.portfolio.web.utils.Res
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    section: SectionItem,
    alignment: Alignment.Horizontal = Alignment.Start,
    showSeeAllButton: Boolean = false,
    href: String? = null
) {
    val scope = rememberCoroutineScope()
    var titleMargin by remember { mutableStateOf(100.px) }
    var subtitleMargin by remember { mutableStateOf(125.px) }
    var dividerMargin by remember { mutableStateOf(150.px) }

    ObserveViewportEntered(
        sectionId = section.id,
        distanceFromTop = 700.0
    ) {
        scope.launch {
            titleMargin = 0.px
            subtitleMargin = 0.px
            dividerMargin = 0.px
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = alignment
        ) {
            val textAlignment = when (alignment) {
                Alignment.Start -> TextAlign.Start
                Alignment.End -> TextAlign.End
                else -> TextAlign.Center
            }
            P(
                attrs = Modifier
                    .fillMaxWidth()
                    .textAlign(textAlignment)
                    .margin(top = 0.px, bottom = 0.px)
                    .padding(
                        left = if (alignment != Alignment.End) titleMargin else 0.px,
                        right = if (alignment == Alignment.End) titleMargin else 0.px,
                    )
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(16.px)
                    .fontWeight(FontWeight.Normal)
                    .transition(Transition.of(padding, 1000.ms))
                    .toAttrs()
            ) {
                Text(section.title)
            }

            Spacer(Modifier.height(8.px))

            P(
                attrs = Modifier
                    .fillMaxWidth()
                    .textAlign(textAlignment)
                    .margin(top = 0.px, bottom = 0.px)
                    .padding(
                        left = if (alignment != Alignment.End) subtitleMargin else 0.px,
                        right = if (alignment == Alignment.End) subtitleMargin else 0.px,
                    )
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(28.px)
                    .fontWeight(FontWeight.Bold)
                    .transition(Transition.of(padding, 1000.ms))
                    .toAttrs()
            ) {
                Text(section.subtitle)
            }

            Spacer(Modifier.height(8.px))

            Box(
                modifier = Modifier
                    .height(2.px)
                    .width(80.px)
                    .backgroundColor(ColorMode.current.toColorScheme.primary)
                    .borderRadius(r = 50.px)
                    .margin(
                        left = if (alignment != Alignment.End) dividerMargin else 0.px,
                        right = if (alignment == Alignment.End) dividerMargin else 0.px,
                    )
                    .transition(Transition.of(margin, 1000.ms))
            )
        }

        if (showSeeAllButton) {
            A(
                href = href,
                attrs = Modifier
                    .textDecorationLine(TextDecorationLine.None)
                    .toAttrs()
            ) {
                Button(
                    variant = TextPrimaryButtonVariant,
                    size = ButtonSize.SM,
                    onClick = {}
                ) {
                    Text(Res.String.SeeAll)

                    Spacer(Modifier.width(8.px))

                    chevronRightSvg(Modifier.size(18.px).margin(top = 2.px))
                }
            }
        }
    }
}