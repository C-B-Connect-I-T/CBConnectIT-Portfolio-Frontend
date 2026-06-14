package cbconnectit.portfolio.web.components.table

import androidx.compose.runtime.Composable

/**
 * Column definition for generic data tables
 *
 * @param T The type of items displayed in the table
 * @param heading The column header text
 * @param resizable Whether the column can be resized by the user
 * @param wrapContent Whether the column should only be as wide as its content (true) or take available space (false)
 * @param comparator Optional comparator for sorting (null = not sortable)
 * @param cellContent Composable to render the cell content for an item
 */
data class TableColumnDef<T>(
    val heading: String,
    val resizable: Boolean = false,
    val wrapContent: Boolean = false,
    val comparator: Comparator<T>? = null,
    val cellContent: @Composable (T) -> Unit
)
