package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.data.models.domain.Link
import cbconnectit.portfolio.web.data.models.enums.LinkIcon
import cbconnectit.portfolio.web.utils.Identifiers.SocialBar.socialIcon
import com.materialkobweb.components.widgets.DsBorderRadius
import com.materialkobweb.components.widgets.ElevatedIconButton
import com.materialkobweb.constants.AttributeValue
import com.materialkobweb.constants.Attributes
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.navigation.Anchor
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.px

@Composable
fun SocialBar(
    row: Boolean = false,
    links: List<Link> = emptyList(),
    itemGap: CSSLengthOrPercentageNumericValue
) {
    Box(
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(if (row) FlexDirection.Row else FlexDirection.Column)
            .flexWrap(FlexWrap.Wrap)
            .justifyContent(JustifyContent.Center)
            .gap(itemGap)
    ) {
        links.forEach { link ->
            Anchor(
                attrs = Modifier
                    .toAttrs {
                        attr(Attributes.Target, AttributeValue.Blank)
                        attr(Attributes.Rel, AttributeValue.NoReferrer)
                    },
                href = link.url,
            ) {
                ElevatedIconButton(
                    modifier = Modifier.size(36.px),
                    size = ButtonSize.MD,
                    borderRadius = DsBorderRadius(6.px),
                    onClick = {},
                    content = {
                        link.type.LinkIcon(
                            modifier = Modifier.id(socialIcon)
                        )
                    }
                )
            }
        }
    }
}
