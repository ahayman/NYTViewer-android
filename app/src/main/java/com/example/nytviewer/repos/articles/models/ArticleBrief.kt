package com.example.nytviewer.repos.articles.models

import java.util.Date

/**
 * The ArticleBrief is the minimal information needed to display the article
 * information in a List.
 */
data class ArticleBrief(
    /// The unique identifier
    val uri: String,
    /// The publish date
    val date: Date,
    /// The article title
    val title: String,
    /// Who wrote the article
    val byline: String,
    /// A small, thumbnail image
    val imageUrl: String?
)
