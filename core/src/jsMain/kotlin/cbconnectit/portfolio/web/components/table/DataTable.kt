package cbconnectit.portfolio.web.components.table

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cbconnectit.portfolio.web.styles.TableStyles
import com.materialkobweb.toColorScheme
import com.varabyte.kobweb.compose.css.BorderCollapse
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Filter
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.VerticalAlign
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.borderTop
import com.varabyte.kobweb.compose.css.functions.brightness
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderCollapse
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.filter
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.verticalAlign
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Tbody
import org.jetbrains.compose.web.dom.Td
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Th
import org.jetbrains.compose.web.dom.Thead
import org.jetbrains.compose.web.dom.Tr

val DataTableStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .borderRadius(TableStyles.borderRadius)
            .overflow(Overflow.Auto)
    }
    cssRule("> table") {
        Modifier
            .fillMaxWidth()
            .borderCollapse(BorderCollapse.Collapse)
    }
    cssRule("> table > thead > tr > th") {
        Modifier
            .padding(
                leftRight = TableStyles.headerPaddingLeftRight,
                topBottom = TableStyles.headerPaddingTopBottom
            )
            .fontWeight(FontWeight.SemiBold)
            .fontSize(14.px)
            .textAlign(TextAlign.Left)
            .color(colorMode.toColorScheme.onSurfaceVariant)
    }
    cssRule("> table > thead > tr > th[data-sorted='true']") {
        Modifier
            .color(colorMode.toColorScheme.primary)
    }
    cssRule("> table > thead > tr > th[data-wrap-content='true']") {
        Modifier
            .whiteSpace(WhiteSpace.NoWrap)
            .width(1.percent)
    }
    cssRule("> table > thead > tr > th[data-resizable='true']") {
        Modifier
            .resize(Resize.Horizontal)
            .overflow(Overflow.Hidden)
            .minWidth(TableStyles.minColumnWidth)
    }
    cssRule("> table > thead > tr > th[data-sortable='true']") {
        Modifier
            .cursor(Cursor.Pointer)
            .userSelect(UserSelect.None)
    }
    cssRule("> table > tbody > tr > td") {
        Modifier
            .padding(
                leftRight = TableStyles.cellPaddingLeftRight,
                topBottom = TableStyles.cellPaddingTopBottom
            )
            .fontSize(14.px)
    }
    cssRule("> table > tbody > tr > td[data-wrap-content='true']") {
        Modifier
            .whiteSpace(WhiteSpace.NoWrap)
            .width(1.percent)
    }
    cssRule("> table > tbody > tr[data-hoverable='true']") {
        Modifier
            .transition(
                Transition.of(
                    property = TransitionProperty.of("filter"),
                    duration = 120.ms,
                    timingFunction = TransitionTimingFunction.EaseInOut
                )
            )
    }
    cssRule("> table > tbody > tr[data-hoverable='true']:hover") {
        Modifier.filter(Filter.of(brightness(0.85)))
    }
}

private fun Boolean.asDataAttrValue(): String = toString()

/**
 * Generic reusable data table with sorting and optional row expansion
 *
 * @param T The type of items displayed in the table
 * @param items List of items to display
 * @param columns Column definitions
 * @param emptyMessage Message to show when list is empty
 * @param expandedRowKey Optional key for the currently expanded row (null if no expansion)
 * @param getRowKey Function to get unique key for an item (required for row expansion)
 * @param onRowClick Callback when a row is clicked (for expansion support)
 * @param expandedContent Composable to render in the expanded row (receives the item)
 */
@Composable
fun <T> DataTable(
    items: List<T>,
    columns: List<TableColumnDef<T>>,
    emptyMessage: String = "Geen items gevonden.",
    expandedRowKey: String? = null,
    getRowKey: ((T) -> String)? = null,
    onRowClick: ((T) -> Unit)? = null,
    expandedContent: (@Composable (T) -> Unit)? = null
) {
    // Validate: expansion features require getRowKey
    require(expandedRowKey == null || getRowKey != null) {
        "getRowKey must be provided when expandedRowKey is used for row expansion"
    }
    require(expandedContent == null || getRowKey != null) {
        "getRowKey must be provided when expandedContent is used for row expansion"
    }

    val colorScheme = ColorMode.current.toColorScheme

    var sortColumnIndex by remember { mutableStateOf<Int?>(null) }
    var sortAscending by remember { mutableStateOf(true) }

    val sortedItems = remember(items, columns, sortColumnIndex, sortAscending) {
        val col = sortColumnIndex
        if (col == null || col >= columns.size) {
            items
        } else {
            val comparator = columns[col].comparator ?: return@remember items
            if (sortAscending) items.sortedWith(comparator) else items.sortedWith(comparator.reversed())
        }
    }

    Box(modifier = DataTableStyle.toModifier()) {
        Table(
            attrs = Modifier
                .backgroundColor(colorScheme.surfaceContainer)
                .toAttrs()
        ) {
            Thead {
                Tr(attrs = Modifier.backgroundColor(colorScheme.surfaceVariant).toAttrs()) {
                    columns.forEachIndexed { index, col ->
                        val isSortable = col.comparator != null
                        val isSorted = sortColumnIndex == index
                        val indicator = when {
                            !isSortable -> ""
                            isSorted && sortAscending -> " ↑"
                            isSorted -> " ↓"
                            else -> ""
                        }
                        Th(
                            attrs = Modifier
                                .thenIf(isSortable) {
                                    Modifier
                                        .onClick {
                                            when {
                                                // Not sorted yet -> sort ascending
                                                !isSorted -> {
                                                    sortColumnIndex = index
                                                    sortAscending = true
                                                }
                                                // Currently ascending -> sort descending
                                                sortAscending -> sortAscending = false
                                                // Currently descending -> reset to original order
                                                else -> sortColumnIndex = null
                                            }
                                        }
                                }
                                .toAttrs {
                                    attr("data-wrap-content", col.wrapContent.asDataAttrValue())
                                    attr("data-resizable", col.resizable.asDataAttrValue())
                                    attr("data-sortable", isSortable.asDataAttrValue())
                                    attr("data-sorted", isSorted.asDataAttrValue())
                                }
                        ) { Text(col.heading + indicator) }
                    }
                }
            }
            Tbody {
                if (items.isEmpty()) {
                    // Show single cell spanning all columns
                    Tr(
                        attrs = Modifier.backgroundColor(colorScheme.surfaceContainer)
                            .styleModifier {
                                borderTop(TableStyles.borderWidth, LineStyle.Solid, colorScheme.outlineVariant)
                            }
                            .toAttrs()
                    ) {
                        Td(
                            attrs = Modifier
                                .padding(
                                    leftRight = TableStyles.cellPaddingLeftRight,
                                    topBottom = 20.px
                                )
                                .fontSize(14.px)
                                .height(TableStyles.emptyStateHeight)
                                .textAlign(TextAlign.Center)
                                .verticalAlign(VerticalAlign.Middle)
                                .color(colorScheme.onSurfaceVariant)
                                .toAttrs {
                                    attr("colspan", columns.size.toString())
                                }
                        ) {
                            Text(emptyMessage)
                        }
                    }
                } else {
                    sortedItems.forEachIndexed { index, item ->
                        val rowKey = getRowKey?.invoke(item)
                        val isExpanded = expandedRowKey != null && rowKey == expandedRowKey

                        // Main row
                        Tr(
                            attrs = Modifier.backgroundColor(
                                if (isExpanded) colorScheme.primaryContainer
                                else if (index % 2 == 0) colorScheme.surfaceContainer
                                else colorScheme.surfaceContainerHigh
                            )
                                .styleModifier {
                                    borderTop(TableStyles.borderWidth, LineStyle.Solid, colorScheme.outlineVariant)
                                }
                                .thenIf(onRowClick != null) {
                                    Modifier
                                        .cursor(Cursor.Pointer)
                                        .onClick { onRowClick?.invoke(item) }
                                }
                                .toAttrs {
                                    attr("data-hoverable", (onRowClick != null).asDataAttrValue())
                                }
                        ) {
                            columns.forEach { col ->
                                Td(
                                    attrs = Modifier
                                        .color(
                                            if (isExpanded) colorScheme.onPrimaryContainer
                                            else colorScheme.onSurfaceVariant
                                        )
                                        .toAttrs {
                                            attr("data-wrap-content", col.wrapContent.asDataAttrValue())
                                        }
                                ) {
                                    col.cellContent(item)
                                }
                            }
                        }

                        // Expanded row (if enabled)
                        if (isExpanded && expandedContent != null) {
                            Tr(
                                attrs = Modifier.backgroundColor(colorScheme.surfaceContainerLow)
                                    .toAttrs()
                            ) {
                                Td(
                                    attrs = Modifier
                                        .padding(TableStyles.expandedContentPadding)
                                        .toAttrs {
                                            attr("colspan", columns.size.toString())
                                        }
                                ) {
                                    expandedContent(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
