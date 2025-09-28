package cbconnectit.portfolio.web.pages.projects

import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.SocialBar
import cbconnectit.portfolio.web.components.layouts.PageLayout
import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.utils.Constants
import cbconnectit.portfolio.web.utils.Identifiers
import cbconnectit.portfolio.web.utils.Res
import cbconnectit.portfolio.web.utils.markdownParagraph
import com.materialdesignsystem.components.Spacer
import com.materialdesignsystem.components.widgets.DsMultiSelect
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Page("/projects")
@Composable
fun ProjectsPage() {
    val ctx = rememberPageContext()
    val queryTagIds = ctx.route.params.filterKeys { it.startsWith(Identifiers.PathParams.Tag) }.values.toList()

    val viewModel = remember { ProjectsViewModel(queryTagIds) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
//            when (it) { }
        }
    }

    ProjectsPageContent(state, viewModel::sendIntent)
}

@Composable
private fun ProjectsPageContent(
    state: ProjectsContract.State,
    sendIntent: (ProjectsContract.Intent) -> Unit
) {
    PageLayout(
        title = Res.String.Projects,
        showMenuItems = false,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleAndDropDown(state.tags, state.filterTags, {
                sendIntent(ProjectsContract.Intent.UpdateFilterTags(it.id))
            })

            if (state.filteredProjects.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(90.percent)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    SpanText(
                        text = "No projects found for this filter.",
                        modifier = Modifier
                            .fontSize(32.px)
                            .textAlign(TextAlign.Center)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(68.px)
                ) {
                    state.filteredProjects.forEachIndexed { index, project ->
                        val leftAligned = index % 2 == 0

                        ProjectSection(project = project, leftAligned = leftAligned)
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleAndDropDown(
    tags: List<Tag>,
    filterTags: List<String>,
    toggleFilterTag: (tag: Tag) -> Unit
) {
    val breakpoint = rememberBreakpoint()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(Constants.SECTION_WIDTH.px)
            .padding(topBottom = 50.px, leftRight = 10.percent),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        H1(
            attrs = Modifier
                .fillMaxWidth()
                .toAttrs()
        ) {
            Text(Res.String.Projects)
        }

        DsMultiSelect(
            modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.SM) 60.percent else 100.percent),
            id = Identifiers.ProjectsPage.dropBtn,
            label = "",
            placeholder = "All Categories",
            items = tags.map { it.name },
            selectedItems = tags.filter { filterTags.contains(it.id) }.map { it.name }
        ) { selectedTag ->
            toggleFilterTag(tags.first { it.name == selectedTag })
        }
    }
}

@Composable
private fun ProjectSection(leftAligned: Boolean, project: Project) {
    val breakpoint = rememberBreakpoint()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .thenIf(!leftAligned) {
                Modifier.backgroundColor(ColorMode.current.toColorScheme.secondaryContainer)
                    .color(ColorMode.current.toColorScheme.onSecondaryContainer)
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(Constants.SECTION_WIDTH.px)
                .display(DisplayStyle.Flex)
                .flexDirection(if (breakpoint > Breakpoint.MD) FlexDirection.Row else FlexDirection.Column)
                .alignItems(AlignItems.Center)
                .padding(topBottom = 50.px, leftRight = 10.percent)
                .gap(100.px)
        ) {
            ProjectImageWithLinks(
                project = project,
                modifier = Modifier.order(if (breakpoint > Breakpoint.MD && leftAligned) 0 else 1)
            )

            Column(Modifier.fillMaxWidth()) {
                SpanText(
                    text = project.title,
                    modifier = Modifier
                        .fontSize(32.px)
                        .fontWeight(FontWeight.Bold)
                )

                P(
                    attrs = Modifier
                        .fontSize(18.px)
                        .toAttrs {
                            markdownParagraph(project.description)
                        }
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .display(DisplayStyle.Flex)
                        .flexDirection(FlexDirection.Row)
                        .flexWrap(FlexWrap.Wrap)
                        .gap(12.px)
                ) {
                    project.tags.forEach {
                        SpanText(
                            text = it.name,
                            modifier = Modifier
                                .padding(topBottom = 4.px, leftRight = 6.px)
                                .backgroundColor(if (leftAligned) ColorMode.current.toColorScheme.secondaryContainer else ColorMode.current.toColorScheme.inversePrimary)
                                .color(ColorMode.current.toColorScheme.onSecondaryContainer)
                                .fontSize(11.px)
                                .borderRadius(6.px)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectImageWithLinks(modifier: Modifier = Modifier, project: Project) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(modifier = Modifier.width(250.px), src = project.imageUrl, alt = "")

        Spacer(Modifier.height(36.px))

        SocialBar(true, links = project.links, itemGap = 20.px)
    }
}
