package com.example.nytviewer.repos.articles.models

import java.util.Date

data class ArticleBrief(
    val uri: String,
    val date: Date,
    val title: String,
    val byline: String,
    val imageUrl: String?
)
