package com.example.nytviewer.repos.articles.models

/**
 * Articles can belong to a section (ex: US Politics, Automotive, Books, etc).
 * The class represents those sections.
 */
data class ArticleSection(
    /// Unique ID
    val id: String,
    /// Human readable label
    val label: String,
)
