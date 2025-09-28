package cbconnectit.portfolio.web.utils

import cbconnectit.portfolio.web.externals.parse
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import org.jetbrains.compose.web.attributes.AttrsScope
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
