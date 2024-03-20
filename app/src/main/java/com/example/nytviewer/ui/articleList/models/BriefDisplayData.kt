package com.example.nytviewer.ui.articleList.models

import java.net.URL

data class BriefDisplayData(
    val uri: String,
    val dateDisplay: String,
    val title: String,
    val byline: String,
    val imageUrl: String?
)