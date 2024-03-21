package com.example.nytviewer.ui.articleDetail.models

import android.content.Context
import java.net.URL

/**
 * All the action that can be taken by the ArticleDetailView, to be submitted to the
 * ArticleDetail View model.
 */
sealed class ArticleDetailActions {
    /// Should navigate to the prior screen
    data object BackPress : ArticleDetailActions()
    /// Should reload the article. This should only be available if an error occurs.
    data object ReloadArticle: ArticleDetailActions()
    /// If the user want to read the full article, they will need to view it in a browser.
    data class VisitArticle(val url: URL, val context: Context): ArticleDetailActions()
}