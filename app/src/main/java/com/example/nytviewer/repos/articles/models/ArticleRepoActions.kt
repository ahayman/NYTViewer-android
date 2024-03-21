package com.example.nytviewer.repos.articles.models

sealed class ArticleRepoActions {
    data object RefreshSections : ArticleRepoActions()
    data object RefreshArticles : ArticleRepoActions()
    data object LoadMoreArticles : ArticleRepoActions()
    data class SelectList(val listDef: ListDef) : ArticleRepoActions()
}