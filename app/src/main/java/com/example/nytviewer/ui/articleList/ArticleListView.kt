package com.example.nytviewer.ui.articleList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.nytviewer.repos.articles.models.ArticleSection
import com.example.nytviewer.repos.articles.models.ListDef
import com.example.nytviewer.ui.articleList.models.ArticleListVMActions
import com.example.nytviewer.ui.articleList.models.BriefDisplayData
import com.example.nytviewer.ui.common.ErrorView
import com.example.nytviewer.ui.common.NavBar
import com.example.nytviewer.ui.theme.ColorTheme
import kotlinx.coroutines.launch

/**
 * The main Article List view. Displays a list of articles, allowing the user to scroll and
 * select and article for more details. Lists can be switched using a Menu Drawer. Also, the
 * theme can be switched as well.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleListView(viewModel: ArticleListVM = hiltViewModel()) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state by viewModel.state.collectAsState()
    val error by viewModel.error.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.submit(ArticleListVMActions.RefreshArticles) }
    )
    val listState = rememberLazyListState()
    val lastListIndex by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index } }
    val colorTheme by viewModel.colorTheme.collectAsState()
    val isDarkTheme = colorTheme.isDarkTheme()

    /**
     * Updates the VM with the current scroll information when the scroll has changed.
     */
    LaunchedEffect(lastListIndex) {
        lastListIndex?.let {
            viewModel.submit(ArticleListVMActions.ScrollChange(it))
        }
    }

    NavBar(
        title = state.listDef.title,
        backIcon = Icons.Filled.Menu,
        backAction = {
            scope.launch {
                drawerState.apply { if (isClosed) open() else close() }
            }
        },
        rightBarContent = { ThemeSwitcher {
            viewModel.submit(ArticleListVMActions.SetTheme(if (isDarkTheme) ColorTheme.Light else ColorTheme.Dark))
        } }
    ) {
        MenuDrawer(viewModel = viewModel, drawerState = drawerState) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .pullRefresh(pullRefreshState),
            ) {
                Column {
                    error?.let {
                        ErrorView(error = it) {
                            viewModel.submit(ArticleListVMActions.RefreshArticles)
                        }
                    }
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        items(state.articles) {
                            BriefView(
                                brief = it,
                                onSelect = {
                                    viewModel.submit(ArticleListVMActions.SelectBrief(it))
                                }
                            )
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

/**
 * A simple Icon view that displays the "Theme Switcher" icon.
 */
@Composable
private fun ThemeSwitcher(onPress: () -> Unit) = with(MaterialTheme) {
    IconButton(onClick = onPress) {
        Icon(
            Icons.Outlined.ColorLens,
            tint = colorScheme.primary,
            contentDescription = "Theme Switcher"
        )
    }
}

/**
 * The Menu Drawer definition that allows the user to switch between different
 * article lists.
 */
@Composable
private fun MenuDrawer(viewModel: ArticleListVM, drawerState: DrawerState, content: @Composable () -> Unit) {
    val state by viewModel.state.collectAsState()
    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.scrim,
        drawerContent = {
            ModalDrawerSheet {
                Divider()
                Text(
                    text = "Popular",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                ListDrawerItem(
                    listDef = ListDef.PopularViewed,
                    selected = state.listDef,
                    viewModel = viewModel,
                    state = drawerState
                )
                ListDrawerItem(
                    listDef = ListDef.PopularShared,
                    selected = state.listDef,
                    viewModel = viewModel,
                    state = drawerState
                )
                ListDrawerItem(
                    listDef = ListDef.PopularEmailed,
                    selected = state.listDef,
                    viewModel = viewModel,
                    state = drawerState
                )
                Divider()
                Text(
                    text = "Sections",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    items(state.sections) { section ->
                        section.let { ArticleSection(it.id, it.label) }.apply {
                            ListDrawerItem(
                                listDef = ListDef.Section(this),
                                selected = state.listDef,
                                viewModel = viewModel,
                                state = drawerState
                            )
                        }
                    }
                }
            }
        }
    ) {
        content()
    }
}

/**
 * A Drawer Item should include the ListDef it represents, the current selected (so selected lists
 * can be highlighted) the view model (to submit a list change) and the drawer state (so we can close
 * the drawer when a selection is made).
 */
@Composable
private fun ListDrawerItem(listDef: ListDef, selected: ListDef, viewModel: ArticleListVM, state: DrawerState) = with(MaterialTheme) {
    val scope = rememberCoroutineScope()
    NavigationDrawerItem(
        label = {
            Text(
                text = listDef.title,
                color = colorScheme.onSurface
            )
        },
        selected = listDef.id == selected.id,
        onClick = {
            viewModel.submit(ArticleListVMActions.SelectList(listDef))
            scope.launch {
                state.close()
            }
        }
    )
}

/**
 * This is the Article Brief list item view. Displays the title, date, byline and
 * image (if any) in a Row.
 */
@Composable
private fun BriefView(brief: BriefDisplayData, onSelect: () -> Unit) = with(MaterialTheme) {
    Surface(
        modifier = Modifier
            .clickable { onSelect() }
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(16.dp)),
        color = colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = brief.title,
                    style = typography.labelMedium,
                    color = colorScheme.onSurface
                )
                Text(
                    text = brief.dateDisplay,
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
                brief.byline.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        style = typography.bodySmall,
                        color = colorScheme.onSurface
                    )
                }
            }
            brief.imageUrl?.let {
                Box(modifier = Modifier.padding(5.dp)) {
                    AsyncImage(
                        model = it,
                        contentDescription = brief.title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .width(100.dp)
                    )
                }
            }
        }
    }
}