package com.example.nytviewer.repos.articles

import com.example.nytviewer.repos.articles.models.ArticleBrief
import com.example.nytviewer.repos.articles.models.ArticleDetail
import com.example.nytviewer.repos.articles.models.ArticleSection
import com.example.nytviewer.repos.articles.models.ListDef
import com.example.nytviewer.utils.StateReducer

data class ArticleRepoState(
    val sections: List<ArticleSection>,
    val articles: List<ArticleBrief>,
    val listDef: ListDef,
)

sealed class ArticleRepoActions {
    data object RefreshSections : ArticleRepoActions()
    data object RefreshArticles : ArticleRepoActions()
    data object LoadMoreArticles : ArticleRepoActions()
    data class SelectList(val listDef: ListDef) : ArticleRepoActions()
}

interface ArticlesRepoInterface : StateReducer<ArticleRepoActions, ArticleRepoState> {
    suspend fun getArticle(uri: String): ArticleDetail?
}