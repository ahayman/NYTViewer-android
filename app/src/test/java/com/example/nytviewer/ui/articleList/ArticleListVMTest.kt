package com.example.nytviewer.ui.articleList

import com.example.nytviewer.navigation.NavAction
import com.example.nytviewer.navigation.Navigator
import com.example.nytviewer.repos.articles.models.ArticleRepoActions
import com.example.nytviewer.repos.articles.models.ArticleSection
import com.example.nytviewer.repos.articles.models.ListDef
import com.example.nytviewer.ui.articleList.mocks.ArticlesRepoMock
import com.example.nytviewer.ui.articleList.mocks.RepoState
import com.example.nytviewer.ui.articleList.mocks.createArticleBrief
import com.example.nytviewer.ui.articleList.mocks.createBriefData
import com.example.nytviewer.ui.articleList.models.ArticleListVMActions
import com.example.nytviewer.ui.theme.ColorTheme
import com.example.nytviewer.ui.theme.MutableThemeProvider
import com.example.nytviewer.ui.theme.ThemeProviders
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


@OptIn(ExperimentalCoroutinesApi::class)
class ArticleListVMTest {
    private lateinit var viewModel: ArticleListVM

    private lateinit var articlesRepo: ArticlesRepoMock

    private val theme: MutableThemeProvider = ThemeProviders.inMemoryProvider()

    @Mock
    private lateinit var navigator: Navigator

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        articlesRepo = ArticlesRepoMock(RepoState)
        viewModel = ArticleListVM(theme = theme, navigator = navigator, articlesRepo = articlesRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test ViewModel responds to repo state changes`() = runBlocking {
        var vmState = viewModel.state.value

        /// Check initial state
        assertEquals(vmState.articles.count(), 0)
        assertEquals(vmState.sections.count(), 0)
        assertEquals(vmState.listDef.id, RepoState.listDef.id)

        /// Update state and recheck
        articlesRepo.state.update {
            it.copy(
                sections = listOf(ArticleSection("1", "1"), ArticleSection("2", "2")),
                articles = listOf(createArticleBrief(1), createArticleBrief(2), createArticleBrief(3)),
                listDef = ListDef.PopularShared
            )
        }

        vmState = viewModel.state.value
        assertEquals(vmState.articles.count(), 3)
        assertEquals(vmState.articles[0].uri, "1")
        assertEquals(vmState.articles[1].uri, "2")
        assertEquals(vmState.articles[2].uri, "3")
        assertEquals(vmState.sections.count(), 2)
        assertEquals(vmState.sections[0].id, "1")
        assertEquals(vmState.sections[1].id, "2")
        assertEquals(vmState.listDef.id, ListDef.PopularShared.id)
    }

    @Test
    fun `test ViewModel responds to repo error state change`() = runBlocking {
        var vmError = viewModel.error.value
        assertNull(vmError)

        articlesRepo.error.value = "Error"

        vmError = viewModel.error.value
        assertEquals(vmError, "Error")
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `test view model updates refresh`() = runBlocking {
        val refreshChanges = mutableListOf<Boolean>()
        GlobalScope.launch {
            viewModel.isRefreshing.collect{
                refreshChanges.add(it)
            }
        }
        viewModel.execute(ArticleListVMActions.RefreshArticles)
        assertEquals(refreshChanges.toList(), listOf(true, false))
    }

    @Test
    fun `test refresh articles Action`() = runBlocking {
        viewModel.execute(ArticleListVMActions.RefreshArticles)
        assertEquals(articlesRepo.lastExecute, ArticleRepoActions.RefreshArticles)
    }

    @Test
    fun `test section refresh action`() = runBlocking {
        viewModel.execute(ArticleListVMActions.RefreshSections)
        assertEquals(articlesRepo.lastExecute, ArticleRepoActions.RefreshSections)
    }

    @Test
    fun `test load more action`() = runBlocking {
        viewModel.execute(ArticleListVMActions.LoadMoreArticles)
        assertEquals(articlesRepo.lastExecute, ArticleRepoActions.LoadMoreArticles)
    }

    @Test
    fun `test select list action`() = runBlocking {
        viewModel.execute(ArticleListVMActions.SelectList(ListDef.PopularEmailed))
        assertEquals(articlesRepo.lastExecute, ArticleRepoActions.SelectList(ListDef.PopularEmailed))
    }

    @Test
    fun `test theme select action`() = runBlocking {
        viewModel.execute(ArticleListVMActions.SetTheme(ColorTheme.Dark))
        assertEquals(theme.colors.value, ColorTheme.Dark)
    }

    @Test
    fun `test article brief select action`() = runBlocking {
        val brief = createBriefData(1)
        viewModel.execute(ArticleListVMActions.SelectBrief(brief))
        verify(navigator, times(1)).handle(NavAction.OnArticleSelect(brief.uri, brief.title))
    }

    @Test
    fun `test view model triggers reload on scroll`() = runBlocking {
        val articles = (1..10).map { createArticleBrief(it) }
        articlesRepo.state.update {
            it.copy(
                sections = listOf(ArticleSection("1", "1"), ArticleSection("2", "2")),
                articles = articles,
                ///Must be a section: only section can batch load
                listDef = ListDef.Section(ArticleSection("1", "1"))
            )
        }

        /// If scroll index < 90% of article list, no action should be submitted to the repo to load a batch
        viewModel.execute(ArticleListVMActions.ScrollChange(0))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(1))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(2))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(3))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(4))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(5))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(6))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(7))
        assertNull(articlesRepo.lastExecute)
        viewModel.execute(ArticleListVMActions.ScrollChange(8))
        assertNull(articlesRepo.lastExecute)

        // If Scroll index < 90% of articles list, vm should attempt to load a new batch
        viewModel.execute(ArticleListVMActions.ScrollChange(9))
        assertEquals(articlesRepo.lastExecute, ArticleRepoActions.LoadMoreArticles)
    }
}