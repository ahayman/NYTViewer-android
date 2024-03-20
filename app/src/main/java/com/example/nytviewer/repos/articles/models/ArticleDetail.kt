package com.example.nytviewer.repos.articles.models

import java.net.URL

data class ArticleDetail(
    val uri: String,
    val url: URL,
    val title: String,
    val byline: String,
    var abstract: String,
    val imageUrl: URL?,
)