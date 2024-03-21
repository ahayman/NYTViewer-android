package com.example.nytviewer.navigation

import android.content.Intent
import android.net.Uri

/**
 * The interface necessary for a Navigation Graph to function properly.
 * Currently only includes a single method used to convert NavActions to the
 * destination info needed for the action.
 */
interface NavGraphInterface {
    /**
     * For a given [NavAction], this method should return the proper [DestinationInfo].
     * Can return `null`. If it does, no navigation will occur _within_ the app.
     */
    fun destinationInfoForAction(navAction: NavAction): DestinationInfo?
}

/**
 * Having a separate Navigation Graph separates the destination from the actions taken.
 * This allows the graph to dynamically determine dynamically determine the destination.
 */
class NavGraph : NavGraphInterface {
    override fun destinationInfoForAction(
        navAction: NavAction,
    ): DestinationInfo? = when (navAction) {
        NavAction.OnBack -> DestinationInfo(NavDestination.Back)
        NavAction.OnHome -> DestinationInfo(NavDestination.ArticleList, PopBehavior.PopToRoot)
        is NavAction.OnArticleSelect -> DestinationInfo(
            NavDestination.ArticleDetail(navAction.articleId, navAction.title),
            PopBehavior.PopIfSelf
        )
        is NavAction.OnUrlSelect -> {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(navAction.url.toString()))
            navAction.context.startActivity(intent)
            null
        }
    }
}