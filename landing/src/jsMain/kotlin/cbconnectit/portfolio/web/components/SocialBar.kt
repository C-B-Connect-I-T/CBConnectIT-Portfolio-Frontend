package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.data.models.domain.Link
import cbconnectit.portfolio.web.data.models.enums.LinkIcon
import cbconnectit.portfolio.web.utils.Identifiers.SocialBar.socialIcon
import com.materialdesignsystem.components.widgets.DsBorderRadius
import com.materialdesignsystem.components.widgets.ElevatedIconButton
import com.materialdesignsystem.constants.AttributeValue
import com.materialdesignsystem.constants.Attributes
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.navigation.Anchor
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import org.jetbrains.compose.web.css.*

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
        SocialLinks(links)
    }
}

@Composable
private fun SocialLinks(
    links: List<Link> = emptyList()
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
