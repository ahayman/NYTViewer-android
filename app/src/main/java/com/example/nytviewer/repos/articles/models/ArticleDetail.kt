package com.example.nytviewer.repos.articles.models

import java.net.URL
import java.util.Date

/**
 * All the information needed to display an article with all its details.
 * Mostly, this is the same as the brief, but provides a larger image, the
 * article url, and an abstract to describe the article's contents.
 */
data class ArticleDetail(
    /// Unique Id
    val uri: String,
    /// URL source for the article.
    val url: URL,
    /// Publish date
    val date: Date,
    /// Article title
    val title: String,
    /// Who wrote the article
    val byline: String,
    /// Description of the article.
    var abstract: String,
    /// Large image
    val imageUrl: String?,
)