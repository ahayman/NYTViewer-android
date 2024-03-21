package com.example.nytviewer.ui.articleList.models

/**
 * All the data the UI needs to display an article brief as a list item.
 * The main difference between this and the ArticleBrief, is the date has
 * been formatted as a human readable string.
 */
data class BriefDisplayData(
    val uri: String,
    val dateDisplay: String,
    val title: String,
    val byline: String,
    val imageUrl: String?
)