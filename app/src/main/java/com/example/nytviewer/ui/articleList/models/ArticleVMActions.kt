package com.example.nytviewer.ui.articleList.models

import com.example.nytviewer.repos.articles.models.ListDef

sealed class ArticleVMActions {
    data object RefreshSections : ArticleVMActions()
    data object RefreshArticles : ArticleVMActions()
    data object LoadMoreArticles : ArticleVMActions()
    data class SelectList(val listDef: ListDef) : ArticleVMActions()
    data class SelectBrief(val data: BriefDisplayData) : ArticleVMActions()

    val isRefreshable: Boolean
        get() = when (this) {
            LoadMoreArticles -> true
            RefreshArticles -> true
            RefreshSections -> true
            is SelectBrief -> false
            is SelectList -> false
        }
}

