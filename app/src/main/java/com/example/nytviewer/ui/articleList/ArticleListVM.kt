package com.example.nytviewer.ui.articleList

import androidx.lifecycle.viewModelScope
import com.example.nytviewer.navigation.NavAction
import com.example.nytviewer.navigation.Navigator
import java.text.SimpleDateFormat
import com.example.nytviewer.repos.articles.models.ArticleRepoActions
import com.example.nytviewer.repos.articles.models.ArticleRepoState
import com.example.nytviewer.repos.articles.ArticlesRepoInterface
import com.example.nytviewer.repos.articles.models.ArticleBrief
import com.example.nytviewer.ui.articleList.models.ArticleListVMActions
import com.example.nytviewer.ui.articleList.models.ArticleListVMState
import com.example.nytviewer.ui.articleList.models.BriefDisplayData
import com.example.nytviewer.ui.articleList.models.SectionData
import com.example.nytviewer.ui.common.BaseViewModel
import com.example.nytviewer.ui.theme.MutableThemeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)

/**
 * Convenience function that maps a list of Article Briefs to the view model data
 * the view needs to display it.
 */
private fun mapBriefs(briefs: List<ArticleBrief>): List<BriefDisplayData> =
    briefs.map {
        BriefDisplayData(
            uri = it.uri,
            dateDisplay = dateFormatter.format(it.date),
            title = it.title,
            byline = it.byline,
            imageUrl = it.imageUrl
        )
    }

/**
 * A convenience function that maps the entire repo state into the VM state.
 */
private fun mapRepoState(state: ArticleRepoState): ArticleListVMState = ArticleListVMState(
    sections = state.sections.map { SectionData(it.id, it.label) },
    articles = mapBriefs(state.articles),
    listDef = state.listDef
)

/**
 * The primary View Model for the ArticleListView. Manages all the state the view needs
 * to function.
 *
 * This View Model is a State Reducer, and so all actions that can be taken should be listed
 * in the actions and submitted to the view model as a state reducer.
 */
@HiltViewModel
class ArticleListVM @Inject constructor(
    private val theme: MutableThemeProvider,
    private val navigator: Navigator,
    private val articlesRepo: ArticlesRepoInterface
) : BaseViewModel<ArticleListVMActions, ArticleListVMState>() {
    /**
     * Private state to keep track of whether we've request a new batch of articles
     * for a section list. This keeps us from making duplicate requests to the repo.
     */
    private var moreLoaded = false

    /**
     * Whether a request is currently pending.
     * UI should subscribe and display a loading indicator.
     */
    private val _refreshing = MutableStateFlow(false)
    val isRefreshing = _refreshing.asStateFlow()

    /**
     * Make the theme available to the View so it can properly change the
     * theme when the user wants to.
     * Note: The VM cannot do this because it requires a composable function to
     * determine if the current them is in dark mode or not.
     */
    val colorTheme = theme.colors
        .stateIn(viewModelScope, SharingStarted.Eagerly, theme.colors.value)

    override val state: StateFlow<ArticleListVMState> = articlesRepo.state
        .map { mapRepoState(it) }
        .onEach { moreLoaded = false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, mapRepoState(articlesRepo.state.value))

    override val error: StateFlow<String?> = articlesRepo.error
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    override suspend fun execute(action: ArticleListVMActions) {
        if (_refreshing.value) return
        if (action.isRefreshable) {
            _refreshing.value = true
        }
        when (action) {
            ArticleListVMActions.LoadMoreArticles -> articlesRepo.execute(ArticleRepoActions.LoadMoreArticles)
            ArticleListVMActions.RefreshArticles -> articlesRepo.execute(ArticleRepoActions.RefreshArticles)
            ArticleListVMActions.RefreshSections -> articlesRepo.execute(ArticleRepoActions.RefreshSections)
            is ArticleListVMActions.SetTheme -> theme.setColors(action.theme)
            is ArticleListVMActions.ScrollChange -> handleScroll(action.lastVisibleIndex)
            is ArticleListVMActions.SelectList -> articlesRepo.execute(ArticleRepoActions.SelectList(action.listDef))
            is ArticleListVMActions.SelectBrief -> navigator.handle(NavAction.OnArticleSelect(action.data.uri, action.data.title))
        }
        _refreshing.value = false
    }

    override fun submit(action: ArticleListVMActions) {
        viewModelScope.launch {
            execute(action)
        }
    }

    /**
     * When the scroll change, the UI should update the VM. This will check the last visible
     * index and, if it's far enough in to the list (>= 90%), will trigger a new batch to load.
     * This only works for lists that support batching.
     */
    private fun handleScroll(lastVisibleIndex: Int) {
        val articles = state.value.articles
        if (articles.isEmpty() || !state.value.listDef.canLoadMore) return
        val scrolled = lastVisibleIndex / articles.count().toDouble()
        if (scrolled >= 0.9 && !moreLoaded) {
            moreLoaded = true
            submit(ArticleListVMActions.LoadMoreArticles)
        }
    }
}