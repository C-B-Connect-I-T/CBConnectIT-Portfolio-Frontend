@file:Suppress("LongParameterList")

package cbconnectit.portfolio.web.components

import androidx.compose.runtime.Composable
import cbconnectit.portfolio.web.utils.withAlpha
import com.materialkobweb.components.widgets.DsMaterialSymbols
import com.materialkobweb.components.widgets.DsSpinner
import com.materialkobweb.components.widgets.DsVideo
import com.materialkobweb.components.widgets.SpinnerSize
import com.materialkobweb.extensions.fileChooser
import com.materialkobweb.styles.MaterialColorVars
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignContent
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.w3c.files.File

private const val END_NEGATIVE_PADDING = (-12)
private const val TOP_NEGATIVE_PADDING = (-24)

@Composable
fun VideoChooser(
    modifier: Modifier = Modifier,
    id: String,
    label: String,
    videoUrl: String? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    centerText: String = "",
    isLoading: Boolean,
    onFileSelected: (File) -> Unit,
    onDeleteClicked: (() -> Unit)? = null
) {
    FileChooserWithPreview(
        modifier = modifier,
        id = id,
        label = label,
        accept = "video/*",
        fileUrl = videoUrl,
        horizontalAlignment = horizontalAlignment,
        centerText = centerText,
        isLoading = isLoading,
        previewContent = {
            DsVideo(
                videoUrl = videoUrl ?: "",
                modifier = Modifier
                    .fillMaxSize()
                    .borderRadius(14.px)
                    .objectFit(ObjectFit.Contain),
                controls = true
            )
        },
        onFileSelected = onFileSelected,
        onDeleteClicked = onDeleteClicked
    )
}

@Composable
fun ImageChooser(
    modifier: Modifier = Modifier,
    id: String,
    label: String,
    imageUrl: String? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    previewRoundedCorners: Boolean = false,
    centerText: String = "",
    isLoading: Boolean,
    onFileSelected: (File) -> Unit,
    onDeleteClicked: (() -> Unit)? = null
) {
    FileChooserWithPreview(
        modifier = modifier,
        id = id,
        label = label,
        accept = "image/*",
        fileUrl = imageUrl,
        horizontalAlignment = horizontalAlignment,
        centerText = centerText,
        isLoading = isLoading,
        previewContent = {
            Image(
                src = imageUrl ?: "",
                modifier = Modifier
                    .fillMaxSize()
                    .objectFit(ObjectFit.Contain)
                    .thenIf(previewRoundedCorners) {
                        Modifier.borderRadius(14.px)
                    }
            )
        },
        onFileSelected = onFileSelected,
        onDeleteClicked = onDeleteClicked
    )
}

@Composable
fun FileChooserWithPreview(
    modifier: Modifier = Modifier,
    id: String,
    label: String,
    accept: String,
    horizontalAlignment: Alignment.Horizontal,
    fileUrl: String? = null,
    centerText: String = "",
    isLoading: Boolean = false,
    previewContent: @Composable () -> Unit,
    onFileSelected: (File) -> Unit,
    onDeleteClicked: (() -> Unit)? = null
) {

    fun fileChooser() = if (!isLoading) document.fileChooser(
        accept = accept,
        multiple = false,
        onFileSelected = { onFileSelected(it.first()) }
    ) else Unit

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = horizontalAlignment
    ) {
        Label(
            attrs = Modifier
                .margin(bottom = 0.5.cssRem)
                .cursor(Cursor.Pointer)
                .onClick { fileChooser() }
                .toAttrs()
        ) {
            Text(value = label)
        }

        Box(
            modifier = modifier
                .id(id)
                .border(2.px, LineStyle.Dashed, MaterialColorVars.Outline.withAlpha(0.5f))
                .borderRadius(14.px)
                .thenIf(fileUrl != null) {
                    Modifier.padding(12.px)
                }
                .position(Position.Relative),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    DsSpinner(size = SpinnerSize.Large)
                }

                return@Box
            }

            if (fileUrl == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.px)
                        .onClick { fileChooser() }
                        .cursor(Cursor.Pointer),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.px, Alignment.CenterVertically)
                ) {
                    when {
                        accept.startsWith("image") -> DsMaterialSymbols(
                            modifier = Modifier.height(16.px),
                            icon = "add_a_photo"
                        )

                        accept.startsWith("video") -> DsMaterialSymbols(
                            modifier = Modifier.height(16.px),
                            icon = "videocam"
                        )

                        else -> Unit // Handle other file types if needed
                    }

                    SpanText(
                        text = centerText,
                        modifier = Modifier
                            .textAlign(TextAlign.Center)
                            .fontSize(FontSize.Medium)
                    )
                }
            }

            if (fileUrl != null) {
                // TODO [TicketNumber]: When the dimensions are different than 16:9, the image will stretch the parent's box as well..
                previewContent()

                Row(
                    modifier = Modifier
                        .position(Position.Absolute)
                        .top(TOP_NEGATIVE_PADDING.px)
                        .right(END_NEGATIVE_PADDING.px),
                    horizontalArrangement = Arrangement.spacedBy(4.px, Alignment.CenterHorizontally),
                ) {
                    if (onDeleteClicked != null) {
                        DsMaterialSymbols(
                            modifier = Modifier
                                .size(40.px)
                                .borderRadius(100.percent)
                                .cursor(Cursor.Pointer)
                                .background(MaterialColorVars.Error.value())
                                .color(MaterialColorVars.OnError.value())
                                .onClick { onDeleteClicked() }
                                .textAlign(TextAlign.Center)
                                .alignContent(AlignContent.Center),
                            icon = "delete"
                        )
                    }

                    DsMaterialSymbols(
                        modifier = Modifier
                            .size(40.px)
                            .borderRadius(100.percent)
                            .border(2.px, LineStyle.Solid, MaterialColorVars.Outline.withAlpha(0.5f))
                            .cursor(Cursor.Pointer)
                            .background(MaterialColorVars.Background.value())
                            .onClick { fileChooser() }
                            .textAlign(TextAlign.Center)
                            .alignContent(AlignContent.Center),
                        icon = "edit"
                    )
                }
            }
        }
    }
}
