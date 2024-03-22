package com.example.nytviewer.ui.articleList.mocks

import com.example.nytviewer.repos.articles.models.ArticleRepoState
import com.example.nytviewer.repos.articles.models.ListDef

val RepoState = ArticleRepoState(
    sections = listOf(),
    articles = listOf(),
    listDef = ListDef.PopularViewed,
)
