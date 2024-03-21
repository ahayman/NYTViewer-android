package com.example.nytviewer.ui.articleList.models

import com.example.nytviewer.repos.articles.models.ListDef

/**
 * The state the ArticleListVM exposes to the View.
 * Includes articles, the current list selection, and
 * all the available sections for selection.
 */
data class ArticleListVMState(
    val sections: List<SectionData>,
    val articles: List<BriefDisplayData>,
    val listDef: ListDef,
)

