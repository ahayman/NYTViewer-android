package com.example.nytviewer.navigation

import android.content.Context
import java.net.URL

/**
 * NavAction defines actions taken within the app that may result in a navigation change.
 * The Actions are _not_ directly related to the Navigation Destinations (actual screens).
 * This separates the NavAction taken and the Destination screen. It is up to the NavGraph
 * to determine which Destination is appropriate for any specific action.
 *
 * NavActions should be named to indicate the actual action taken
 * instead of the intended destination.
 */
sealed class NavAction {
    /// When a back button is pressed.
    data object OnBack : NavAction()
    /// When a home button is pressed.
    data object OnHome : NavAction()
    /// When an article is selected.
    data class OnArticleSelect(val articleId: String, val title: String) : NavAction();
    /// When a URL is selected.
    data class OnUrlSelect(val url: URL, val context: Context) : NavAction()
}