package com.example.nytviewer.navigation

sealed class NavAction {
    data object OnBack : NavAction()
    data object OnHome : NavAction()
    data class OnArticleSelect(val articleId: String) : NavAction();
}