package com.example.nytviewer.ui.articleDetail.models

import java.net.URL

/**
 * All the data needed to display and interact with an Article.
 */
data class ArticleDetailData (
    val uri: String,
    val url: URL,
    val dateDisplay: String,
    val title: String,
    val byline: String,
    var abstract: String,
    val imageUrl: String?,
)