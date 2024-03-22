package com.example.nytviewer.services.transport.mocks

import com.example.nytviewer.services.transport.models.Article
import java.util.Date

fun createArticle(idx: Int) = Article(
    url = "http://$idx.com",
    uri = "$idx",
    publishedDate = Date(),
    subsection = "",
    section = "",
    title = "Title $idx",
    byline = "By $idx",
    abstract = "A $idx article",
    media = null,
    multiMedia = null,
)