package com.example.nytviewer.repos.articles

import com.example.nytviewer.repos.articles.models.ArticleDetail
import com.example.nytviewer.repos.articles.models.ArticleRepoActions
import com.example.nytviewer.repos.articles.models.ArticleRepoState
import com.example.nytviewer.utils.StateReducer

/**
 * The Interface for the Articles Repository. Most functionality will be encapsulated
 * in the [ArticleRepoActions], which is handles by the repo to properly update it's state,
 * which is returned a FlowState.
 *
 * The only caveat to this is [getArticle], which will retrieve more details on a specific
 * article that has already been served by the `articles` in the repo state.
 */
interface ArticlesRepoInterface : StateReducer<ArticleRepoActions, ArticleRepoState> {
    suspend fun getArticle(uri: String): ArticleDetail?
}