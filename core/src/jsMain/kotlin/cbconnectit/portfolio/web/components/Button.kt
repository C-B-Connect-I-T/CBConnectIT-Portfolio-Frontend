package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import com.materialkobweb.components.widgets.DsBorderRadius
import com.materialkobweb.components.widgets.FilledButton
import com.materialkobweb.components.widgets.FilledButtonStyle
import com.materialkobweb.components.widgets.OutlinedButton
import com.materialkobweb.components.widgets.OutlinedButtonStyle
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.graphics.lightened
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.silk.components.forms.ButtonKind
import com.varabyte.kobweb.silk.components.forms.ButtonSize
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.attributes.ButtonType

@Composable
fun DestructiveOutlinedButton(
    modifier: Modifier = Modifier,
    id: String? = null,
    borderRadius: DsBorderRadius? = null,
    variant: CssStyleVariant<ButtonKind> = OutlinedButtonStyle,
    type: ButtonType = ButtonType.Button,
    size: ButtonSize = ButtonSize.MD,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val colorScheme = ColorMode.current.toColorScheme

    OutlinedButton(
        modifier = modifier
            .setVariable(ButtonVars.BackgroundDefaultColor, Colors.Transparent)
            .setVariable(ButtonVars.Color, colorScheme.error)
            .setVariable(ButtonVars.BackgroundHoverColor, colorScheme.error.lightened(0.7f))
            .setVariable(ButtonVars.BackgroundFocusColor, colorScheme.error.lightened(0.7f))
            .setVariable(ButtonVars.BackgroundPressedColor, colorScheme.error.lightened(0.5f)),
        borderRadius = borderRadius,
        id = id,
        variant = variant,
        type = type,
        size = size,
        enabled = enabled,
        onClick = onClick,
        content = content
    )
}

@Composable
fun DestructiveFilledButton(
    modifier: Modifier = Modifier,
    id: String? = null,
    borderRadius: DsBorderRadius? = null,
    variant: CssStyleVariant<ButtonKind> = FilledButtonStyle,
    size: ButtonSize = ButtonSize.MD,
    type: ButtonType = ButtonType.Button,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val colorScheme = ColorMode.current.toColorScheme

    FilledButton(
        modifier = modifier
            .setVariable(ButtonVars.BackgroundDefaultColor, colorScheme.error)
            .setVariable(ButtonVars.Color, colorScheme.onError)
            .setVariable(ButtonVars.BackgroundHoverColor, colorScheme.error.lightened(0.1f))
            .setVariable(ButtonVars.BackgroundFocusColor, colorScheme.error.lightened(0.1f))
            .setVariable(ButtonVars.BackgroundPressedColor, colorScheme.error.lightened(0.3f)),
        borderRadius = borderRadius,
        id = id,
        variant = variant,
        type = type,
        size = size,
        enabled = enabled,
        onClick = onClick,
        content = content
    )
}
