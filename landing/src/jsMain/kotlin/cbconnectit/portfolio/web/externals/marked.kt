@file:JsModule("marked")
@file:JsNonModule
@file:JsQualifier("marked")

package cbconnectit.portfolio.web.externals

external fun use(vararg options: MarkedOptions)

external fun parse(markdown: String): String

external interface MarkedOptions {
    var renderer: TextRenderer?
}

external interface Renderer<T> {
    fun link(href: String?, title: String?, text: String): String
}

open external class TextRenderer : Renderer<String> {
    override fun link(href: String?, title: String?, text: String): String
}