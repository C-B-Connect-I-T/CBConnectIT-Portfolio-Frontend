package cbconnectit.portfolio.web.components.layout

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.format
import cbconnectit.portfolio.web.utils.withAlpha
import com.materialkobweb.styles.MaterialColorVars
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.web.css.px

private const val FOOTER_TEXT_ALPHA = 0.25f

@Composable
fun AdminFooter() {
    val currentYear = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .year
        .toString()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            modifier = Modifier
                .fontSize(14.px)
                .color(MaterialColorVars.OnBackground.withAlpha(FOOTER_TEXT_ALPHA))
                .textAlign(TextAlign.Center),
            text = Res.String.Copyright.format(currentYear),
        )
        SpanText(
            modifier = Modifier
                .margin(top = 6.px)
                .fontSize(14.px)
                .color(MaterialColorVars.OnBackground.withAlpha(FOOTER_TEXT_ALPHA))
                .textAlign(TextAlign.Center),
            text = Res.String.WebsiteCreatedBy
        )
    }
}

