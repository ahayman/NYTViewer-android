package com.example.nytviewer.ui.articleList.models

import com.example.nytviewer.repos.articles.models.ListDef

data class ArticleVMState(
    val sections: List<SectionData>,
    val articles: List<BriefDisplayData>,
    val listDef: ListDef,
)

