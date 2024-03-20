package com.example.nytviewer.repos.articles.models

import java.net.URL

data class ArticleBrief(
    val uri: String,
    val title: String,
    val byline: String,
    val imageUrl: URL?
)
