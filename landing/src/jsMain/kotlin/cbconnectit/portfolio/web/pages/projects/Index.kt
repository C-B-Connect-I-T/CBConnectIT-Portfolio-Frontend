package cbconnectit.portfolio.web.pages.projects

//import com.varabyte.kobweb.silk.components.style.ComponentStyle
//import com.varabyte.kobweb.silk.components.style.hover
import androidx.compose.runtime.*
import cbconnectit.portfolio.web.components.*
import cbconnectit.portfolio.web.data.models.domain.Project
import cbconnectit.portfolio.web.data.models.domain.Tag
import cbconnectit.portfolio.web.data.repos.ProjectRepo
import cbconnectit.portfolio.web.data.repos.TagRepo
import cbconnectit.portfolio.web.pages.PageLayout
import cbconnectit.portfolio.web.utils.*
import com.materialdesignsystem.toColorScheme
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Checkbox
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaChevronDown
import com.varabyte.kobweb.silk.components.icons.fa.FaChevronUp
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.shapes.RectF
import com.varabyte.kobweb.silk.theme.shapes.clip
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@Page
@Composable
fun ProjectsPage() {
    val ctx = rememberPageContext()
    var menuOpened by remember { mutableStateOf(false) }
    val breakpoint = rememberBreakpoint()
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var tags by remember { mutableStateOf<List<Tag>>(emptyList()) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    val queryTagIds = ctx.route.params.filterKeys { it.startsWith(Identifiers.PathParams.Tag) }.values.toList()
    var filterTags by remember { mutableStateOf(queryTagIds) }

    window.onclick = {
        val targetIsNotDropDown = (it.target as? HTMLElement)?.id != Identifiers.ProjectsPage.dropBtn

        if (targetIsNotDropDown) {
            showDropDownMenu = false
        }
    }

    LaunchedEffect(Unit) {
        projects = ProjectRepo.getProjects(Config.baseUrl)

        tags = TagRepo.getTags(Config.baseUrl)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        PageLayout(
            Res.String.Projects,
            false,
            {
                menuOpened = true
            }
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleAndDropDown(breakpoint, showDropDownMenu, tags, filterTags, {
                    val tempFilterTags = filterTags.toMutableList()
                    if (tempFilterTags.contains(it.id)) {
                        tempFilterTags.remove(it.id)
                    } else {
                        tempFilterTags.add(it.id)
                    }
                    filterTags = tempFilterTags
                }) {
                    showDropDownMenu = it
                }

                ProjectsList(breakpoint, projects.filter { it.tags.any { tag -> if (filterTags.isNotEmpty()) tag.id in filterTags else true } })
            }
        }

//        * var showModal by remember { mutableStateOf(true) }
//        * if (showModal) {
//        *   Overlay(Modifier.onClick { showModal = false }) {
//            *     Dialog {
//            *        // ... your modal content here ...
//            *     }
//            *   }
//        * }

        BackToTopButton()

        if (menuOpened) {
            OverlowMenu { menuOpened = false }
        }
    }
}

@Composable
fun TitleAndDropDown(
    breakpoint: Breakpoint,
    showDropDownMenu: Boolean,
    tags: List<Tag>,
    filterTags: List<String>,
    toggleFilterTag: (tag: Tag) -> Unit,
    toggleDropDownMenu: (Boolean) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .maxWidth(Constants.SECTION_WIDTH.px),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(topBottom = 50.px, leftRight = 10.percent),
            horizontalAlignment = Alignment.Start
        ) {
            H1(
                attrs = Modifier
                    .fillMaxWidth()
                    .toAttrs()
            ) {
                Text(Res.String.Projects)
            }

            Spacer(Modifier.height(8.px))

            Box(
                modifier = Modifier
                    .fillMaxWidth(if (breakpoint >= Breakpoint.SM) 60.percent else 100.percent)
                    .position(Position.Relative)
                    .display(DisplayStyle.InlineBlock),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = ProjectTagLinkStyle.toModifier()
                        .id(Identifiers.ProjectsPage.dropBtn)
                        .padding(leftRight = 16.px, topBottom = 10.px)
                        .borderRadius(50.px)
                        .border(2.px, LineStyle.Solid, ColorMode.current.toColorScheme.surfaceVariant)
                        .cursor(Cursor.Pointer)
                        .onClick {
                            toggleDropDownMenu(!showDropDownMenu)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val format = if (filterTags.isEmpty()) {
                        Res.String.All
                    } else {
                        tags.filter { filterTags.contains(it.id) }.joinToString(", ") { it.name }
                    }

                    Span(
                        attrs = Modifier
                            .id(Identifiers.ProjectsPage.dropBtn)
                            .maxLines(1)
                            .margin(top = 0.px, bottom = 0.px, left = 0.px, right = 24.px)
                            .userSelect(UserSelect.None)
                            .cursor(Cursor.Pointer)
                            .toAttrs()
                    ) {
                        Text(Res.String.FilterCategories.format(format))
                    }

                    if (showDropDownMenu) {
                        FaChevronUp()
                    } else {
                        FaChevronDown()
                    }
                }

                Box(
                    modifier = Modifier
                        .display(if (showDropDownMenu) DisplayStyle.Flex else DisplayStyle.None)
                        .margin(top = 8.px)
                        .position(Position.Absolute)
                        .backgroundColor(ColorMode.current.toColorScheme.surfaceVariant)
                        .borderRadius(16.px)
                        .color(ColorMode.current.toColorScheme.onSurfaceVariant)
                        .boxShadow(0.px, 8.px, 16.px, 0.px, Colors.Black.copyf(alpha = 0.2f))
                        .zIndex(1)
                ) {
                    SimpleGrid(
                        numColumns = numColumns(1, 2, 3),
                        modifier = Modifier
                            .clip(RectF(16.px))
                    ) {
                        tags.forEach { tag ->
                            Link(
                                "",
                                modifier = ProjectTagLinkStyle.toModifier()
                                    .minWidth(175.px)
                                    .padding(topBottom = 12.px, leftRight = 16.px)
                                    .textDecorationLine(TextDecorationLine.None)
                                    .onClick {
                                        toggleFilterTag(tag)
                                    }
                            ) {
                                Checkbox(
                                    modifier = Modifier
                                        .fontSize(12.px),
                                    checked = filterTags.contains(tag.id),
                                    onCheckedChange = {
                                        toggleFilterTag(tag)
                                    }
                                ) {
                                    Text(tag.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val ProjectTagLinkStyle = CssStyle {
    base {
        Modifier
            .transition(Transition.of(property = "background", duration = 200.ms))
    }

    hover {
        Modifier.backgroundColor(colorMode.toColorScheme.onSurfaceVariant.toRgb().copyf(alpha = 0.2f))
    }
}

@Composable
private fun ProjectsList(breakpoint: Breakpoint, projects: List<Project>) {
    if (projects.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(leftRight = 10.percent),
            contentAlignment = Alignment.Center
        ) {
            Span(
                Modifier
                    .fontSize(32.px)
                    .textAlign(TextAlign.Center)
                    .toAttrs()
            ) { Text("No projects found for this filter.") }
        }
    }

    projects.forEachIndexed { index, project ->
        val leftAligned = index % 2 == 0

        if (index != 0) {
            Spacer(Modifier.height(68.px))
        }

        Box(
            Modifier
                .fillMaxWidth()
                .thenIf(leftAligned.not()) {
                    Modifier.backgroundColor(ColorMode.current.toColorScheme.secondaryContainer)
                        .color(ColorMode.current.toColorScheme.onSecondaryContainer)
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .maxWidth(Constants.SECTION_WIDTH.px),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .display(DisplayStyle.Flex)
                        .flexDirection(if (breakpoint > Breakpoint.MD) FlexDirection.Row else FlexDirection.Column)
                        .alignItems(AlignItems.Center)
                        .padding(topBottom = 50.px, leftRight = 10.percent)
                ) {
                    if (leftAligned && breakpoint > Breakpoint.MD) {
                        ProjectImageWithLinks(breakpoint, project)

                        Spacer(Modifier.width(100.px))
                    }

                    Column(
                        Modifier.fillMaxWidth()
                    ) {
                        P(
                            attrs = Modifier
                                .fontSize(32.px)
                                .fontWeight(FontWeight.Bold)
                                .fontFamily(Constants.FONT_FAMILY)
                                .toAttrs()
                        ) {
                            Text(project.title)
                        }

                        P(
                            attrs = Modifier
                                .fontSize(18.px)
                                .fontFamily(Constants.FONT_FAMILY)
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
                                P(
                                    Modifier
                                        .padding(topBottom = 4.px, leftRight = 6.px)
                                        .margin(topBottom = 0.px)
                                        .backgroundColor(if (leftAligned) ColorMode.current.toColorScheme.secondaryContainer else ColorMode.current.toColorScheme.inversePrimary)
                                        .color(ColorMode.current.toColorScheme.onSecondaryContainer)
                                        .fontSize(11.px)
                                        .borderRadius(6.px)
                                        .toAttrs()
                                ) { Text(it.name) }
                            }
                        }
                    }

                    if (leftAligned.not() || breakpoint <= Breakpoint.MD) {
                        Spacer(
                            Modifier
                                .width(100.px)
                                .thenIf(breakpoint <= Breakpoint.MD) {
                                    Modifier.height(100.px)
                                })

                        ProjectImageWithLinks(breakpoint, project)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectImageWithLinks(breakpoint: Breakpoint, project: Project) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(modifier = Modifier.width(250.px), src = project.imageUrl, alt = "")

        Spacer(Modifier.height(36.px))

        SocialBar(true, links = project.links, itemGap = 20.px, socialLinkSize = SocialLinkSize.LG)
    }
}