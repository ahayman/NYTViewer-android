package com.example.nytviewer.ui.articleList

import androidx.lifecycle.viewModelScope
import com.example.nytviewer.navigation.NavAction
import com.example.nytviewer.navigation.Navigator
import java.text.SimpleDateFormat
import com.example.nytviewer.repos.articles.ArticleRepoActions
import com.example.nytviewer.repos.articles.ArticleRepoState
import com.example.nytviewer.repos.articles.ArticlesRepoInterface
import com.example.nytviewer.repos.articles.models.ArticleBrief
import com.example.nytviewer.ui.articleList.models.ArticleVMActions
import com.example.nytviewer.ui.articleList.models.ArticleVMState
import com.example.nytviewer.ui.articleList.models.BriefDisplayData
import com.example.nytviewer.ui.articleList.models.SectionData
import com.example.nytviewer.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)

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

private fun mapRepoState(state: ArticleRepoState): ArticleVMState = ArticleVMState(
    sections = state.sections.map { SectionData(it.id, it.label) },
    articles = mapBriefs(state.articles),
    listDef = state.listDef
)

@HiltViewModel
class ArticleListVM @Inject constructor(
    private val navigator: Navigator,
    private val articlesRepo: ArticlesRepoInterface
) : BaseViewModel<ArticleVMActions, ArticleVMState>() {
    override val state: StateFlow<ArticleVMState> = articlesRepo.state
        .map { mapRepoState(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, mapRepoState(articlesRepo.state.value))

    override val error: StateFlow<String?> = articlesRepo.error
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _refreshing = MutableStateFlow(false)
    val isRefreshing = _refreshing.asStateFlow()

    override suspend fun execute(action: ArticleVMActions) {
        if (action.isRefreshable) {
            _refreshing.value = true
        }
        when (action) {
            ArticleVMActions.LoadMoreArticles -> articlesRepo.execute(ArticleRepoActions.LoadMoreArticles)
            ArticleVMActions.RefreshArticles -> articlesRepo.execute(ArticleRepoActions.RefreshArticles)
            ArticleVMActions.RefreshSections -> articlesRepo.execute(ArticleRepoActions.RefreshSections)
            is ArticleVMActions.SelectList -> articlesRepo.execute(ArticleRepoActions.SelectList(action.listDef))
            is ArticleVMActions.SelectBrief -> navigator.handle(NavAction.OnArticleSelect(action.data.uri, action.data.title))
        }
        if (action.isRefreshable) {
            _refreshing.value = false
        }
    }

    override fun submit(action: ArticleVMActions) {
        viewModelScope.launch {
            execute(action)
        }
    }

}