package com.example.nytviewer.ui.articleDetail.models

/**
 * The only state needed is the ArticleDetailData, which the VM should load and return
 * to the view. This will only be `null` if an error has occurred.
 */
data class ArticleDetailVMState(
    val data: ArticleDetailData?
)