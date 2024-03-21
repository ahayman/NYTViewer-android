package com.example.nytviewer.repos.articles.models

data class ArticleRepoState(
    val sections: List<ArticleSection>,
    val articles: List<ArticleBrief>,
    val listDef: ListDef,
)