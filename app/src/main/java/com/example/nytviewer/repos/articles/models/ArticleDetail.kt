package com.example.nytviewer.repos.articles.models

import java.net.URL
import java.util.Date

data class ArticleDetail(
    val uri: String,
    val url: URL,
    val date: Date,
    val title: String,
    val byline: String,
    var abstract: String,
    val imageUrl: String?,
)