package com.example.nytviewer.ui.articleDetail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.nytviewer.navigation.NavAction
import com.kiwi.navigationcompose.typed.decodeArguments
import com.example.nytviewer.navigation.NavDestination
import com.example.nytviewer.navigation.Navigator
import com.example.nytviewer.repos.articles.ArticlesRepoInterface
import com.example.nytviewer.ui.articleDetail.models.ArticleDetailActions
import com.example.nytviewer.ui.articleDetail.models.ArticleDetailData
import com.example.nytviewer.ui.articleDetail.models.ArticleDetailVMState
import com.example.nytviewer.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * When creating an Article Detail Data object, this is used to format the date
 * into a human readable string.
 */
private val dateFormatter = SimpleDateFormat("MMM dd, yyyy hh:mma", Locale.US)

/**
 * The View Model responsible for handling the data and actions for the ArticleDetailView.
 */
@OptIn(ExperimentalSerializationApi::class)
@HiltViewModel
class ArticleDetailVM @Inject constructor(
    stateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val articlesRepo: ArticlesRepoInterface
) : BaseViewModel<ArticleDetailActions, ArticleDetailVMState>() {
    /**
     * When routing to this screen, the Article ID should have been passed into the navigation
     * as an argument. This retrieves that argument.
     */
    private val navArgs: NavDestination.ArticleDetail = stateHandle.decodeArguments<NavDestination.ArticleDetail>()

    override val state = MutableStateFlow(ArticleDetailVMState(data = null))
    override val error = MutableStateFlow<String?>(null)

    override suspend fun execute(action: ArticleDetailActions) {
        when (action) {
            ArticleDetailActions.BackPress -> navigator.handle(NavAction.OnBack)
            ArticleDetailActions.ReloadArticle -> reloadArticle()
            is ArticleDetailActions.VisitArticle -> visitURL(action.url, action.context)
        }
    }

    override fun submit(action: ArticleDetailActions) {
        viewModelScope.launch {
            execute(action)
        }
    }

    /// Load the article as soon as the View Model inits.
    init {
        submit(ArticleDetailActions.ReloadArticle)
    }

    /**
     * Logic to reload the article. This should only be necessary when the view model first loads
     * or if there's an error and the user retries to load the article.
     */
    private suspend fun reloadArticle() {
        error.value = null
        val data = articlesRepo.getArticle(navArgs.articleId)
        if (data == null) {
            error.value = "Unable to load Article"
        } else {
            state.update {
                it.copy(
                    data = ArticleDetailData(
                        uri = data.uri,
                        url = data.url,
                        dateDisplay = dateFormatter.format(data.date),
                        title = data.title,
                        byline = data.byline,
                        abstract = data.abstract,
                        imageUrl = data.imageUrl
                    )
                )
            }
        }
    }

    /// We route the request to the navigator and let handle the URL.
    private fun visitURL(url: URL, context: Context) {
        navigator.handle(NavAction.OnUrlSelect(url, context))
    }
}

