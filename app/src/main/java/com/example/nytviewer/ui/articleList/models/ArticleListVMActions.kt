package com.example.nytviewer.ui.articleList.models

import com.example.nytviewer.repos.articles.models.ListDef
import com.example.nytviewer.ui.theme.ColorTheme

/**
 * All of the actions that can be taken on the ArticleList View Model.
 */
sealed class ArticleListVMActions {
    /// Will attempt to refresh the sections returned in the state.
    data object RefreshSections : ArticleListVMActions()
    /// Will attempt to refresh the current list of articles.
    data object RefreshArticles : ArticleListVMActions()
    /// Will attempt to load more articles for the current list.
    data object LoadMoreArticles : ArticleListVMActions()
    /// Change the theme of the app.
    data class SetTheme(val theme: ColorTheme) : ArticleListVMActions()
    /// Should be called when the scroll position changes. Used to load more articles for sections.
    data class ScrollChange(val lastVisibleIndex: Int) : ArticleListVMActions()
    /// Change the list of articles that are being displayed.
    data class SelectList(val listDef: ListDef) : ArticleListVMActions()
    /// Select a brief. Will result in a navigation to the article details.
    data class SelectBrief(val data: BriefDisplayData) : ArticleListVMActions()

    /**
     * Some actions result in a delayed change. For those actions (like refreshing articles),
     * the VM should present a "refreshing" state so the UI can display a loading indicator.
     */
    val isRefreshable: Boolean
        get() = when (this) {
            LoadMoreArticles -> true
            RefreshArticles -> true
            RefreshSections -> true
            is SetTheme -> false
            is SelectList -> true
            is SelectBrief -> false
            is ScrollChange -> false
        }
}

