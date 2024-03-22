package com.example.nytviewer.ui.articleList.mocks

import com.example.nytviewer.ui.articleList.models.BriefDisplayData

fun createBriefData(idx: Int): BriefDisplayData = BriefDisplayData(
    uri = "$idx",
    dateDisplay = "June $idx, 2024",
    title = "Title $idx",
    byline = "By $idx",
    imageUrl = null
)