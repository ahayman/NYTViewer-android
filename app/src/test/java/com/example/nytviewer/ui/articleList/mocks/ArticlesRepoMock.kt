package com.example.nytviewer.ui.articleList.mocks

import com.example.nytviewer.repos.articles.ArticlesRepoInterface
import com.example.nytviewer.repos.articles.models.ArticleDetail
import com.example.nytviewer.repos.articles.models.ArticleRepoActions
import com.example.nytviewer.repos.articles.models.ArticleRepoState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class ArticlesRepoMock(initialState: ArticleRepoState) : ArticlesRepoInterface {
    var mockArticleDetail: ArticleDetail? = null
    var lastGetArticleUri: String? = null
    var lastSubmit: ArticleRepoActions? = null
    var lastExecute: ArticleRepoActions? = null

    override suspend fun getArticle(uri: String): ArticleDetail? {
        lastGetArticleUri = uri
        return mockArticleDetail
    }

    override val state = MutableStateFlow(initialState)
    override val error = MutableStateFlow<String?>(null)

    override fun submit(action: ArticleRepoActions) {
        lastSubmit = action
    }

    override suspend fun execute(action: ArticleRepoActions) {
        lastExecute = action
        //Note: In order for StateFlows to emit properly, the function must have some suspension
        delay(10)
    }
}