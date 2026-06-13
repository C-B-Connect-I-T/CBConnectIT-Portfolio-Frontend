package cbconnectit.portfolio.web.utils

import cbconnectit.portfolio.web.externals.parse
import com.varabyte.kobweb.browser.uri.decodeURIComponent
import com.varabyte.kobweb.browser.uri.encodeURIComponent
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.w3c.dom.HTMLParagraphElement

fun Modifier.maxLines(max: Number) = this
    .display(DisplayStyle("-webkit-box"))
    .overflow(Overflow.Hidden)
    .attrsModifier {
        style {
            property("-webkit-line-clamp", "$max")
            property("-webkit-box-orient", "vertical")
        }
    }

fun AttrsScope<HTMLParagraphElement>.markdownParagraph(
    text: String,
    breaks: Boolean = false,
    vararg classes: String,
) {
    if (classes.isNotEmpty()) classes(*classes)

    val textToParse = if (breaks) text.replace("\n", "<br>") else text

    this.prop({ htmlParagraphElement: HTMLParagraphElement, s: String -> htmlParagraphElement.innerHTML = s }, parse(textToParse))
}

/**
 * Creates a string from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
 * There is also the possibility to use the index of the item, if needed.
 *
 * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
 * elements will be appended, followed by the [truncated] string (which defaults to "...").
 */
fun <T> Iterable<T>.joinToStringIndexed(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((Int, T) -> CharSequence)? = null
): String {
    return joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
}

/**
 * Appends the string from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
 * There is also the possibility to use the index of the item, if needed.
 *
 * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
 * elements will be appended, followed by the [truncated] string (which defaults to "...").
 */
fun <T, A : Appendable> Iterable<T>.joinTo(
    buffer: A,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((Int, T) -> CharSequence)? = null
): A {
    buffer.append(prefix)
    var count = 0
    for (element in this) {
        if (++count > 1) buffer.append(separator)
        if (limit < 0 || count <= limit) {
            buffer.appendElement(element, count, transform)
        } else break
    }
    if (limit in 0..<count) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}

internal fun <T> Appendable.appendElement(element: T, count: Int, transform: ((Int, T) -> CharSequence)?) {
    when {
        transform != null -> append(transform(count, element))
        element is CharSequence? -> append(element)
        element is Char -> append(element)
        else -> append(element.toString())
    }
}

fun String.format(vararg args: Any?): String {
    val pattern = Regex("\\{\\{|\\}\\}|\\{(\\d+)\\}")
    return pattern.replace(this) { result ->
        when (val match = result.value) {
            "{{" -> "{"
            "}}" -> "}"
            else -> args[match.substring(1, match.length - 1).toInt()].toString()
        }
    }
}

/**
 * Applies an alpha (opacity) value to a CSS color variable using color-mix.
 * @param alpha The opacity level (0.0 = fully transparent, 1.0 = fully opaque)
 * @return A CSSColorValue with the alpha applied
 */
fun StyleVariable.PropertyValue<CSSColorValue>.withAlpha(alpha: Float): CSSColorValue {
    require(alpha in 0f..1f)

    val percentage = ((1 - alpha) * 100).toInt()
    val colorMix = "color-mix(in srgb, ${this.value()}, transparent $percentage%)"
    return Color(colorMix)
}

fun logoImage(colorMode: ColorMode) = when (colorMode) {
    ColorMode.DARK -> Res.Image.logoDark
    ColorMode.LIGHT -> Res.Image.logo
}

/**
 * Safely encodes a URL for use as a query parameter using base64 encoding.
 * This is more reliable than URL encoding for complex URLs with multiple query parameters.
 * Uses encodeURIComponent to handle non-Latin1 characters (e.g., Dutch special characters like ë or é)
 * before base64 encoding to prevent DOMException.
 */
fun encodeReturnUrl(url: String): String {
    return try {
        val uriEncoded = encodeURIComponent(url)
        window.btoa(uriEncoded)
    } catch (e: Exception) {
        Logger.error("Functions", "Failed to encode return URL: $url", e)
        ""
    }
}

/**
 * Decodes a base64-encoded URL from a query parameter.
 * Uses decodeURIComponent after base64 decoding to properly restore non-Latin1 characters.
 */
fun decodeReturnUrl(encodedUrl: String): String? {
    return try {
        val base64Decoded = window.atob(encodedUrl)
        decodeURIComponent(base64Decoded)
    } catch (e: Exception) {
        Logger.error("Functions", "Failed to decode return URL: $encodedUrl", e)
        null
    }
}
