package com.example.nytviewer.ui.articleList.mocks

import com.example.nytviewer.repos.articles.models.ArticleBrief
import java.util.Date

fun createArticleBrief(idx: Int): ArticleBrief = ArticleBrief(
    uri = "$idx",
    date = Date(),
    title = "Title $idx",
    byline = "By $idx",
    imageUrl = null
)
