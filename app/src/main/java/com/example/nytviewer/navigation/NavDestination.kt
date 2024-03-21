package com.example.nytviewer.navigation

import androidx.navigation.NamedNavArgument
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.createRoutePattern
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.typeOf

/**
 * Represents all the "destinations" available in the app that can be navigated to.
 * Virtually all the destination should map to real screens in the app.
 * The only exception is the "Back" destination, which represent the prior screen the user
 * has visited (if any).
 */
@OptIn(ExperimentalSerializationApi::class)
sealed class NavDestination : Destination {
    /**
     * The prior screen. The NavHost should not navigate "to" this destination.
     * It should pop the current screen, instead.
     */
    @Serializable
    data object Back : NavDestination()

    /**
     * The ArticleListView screen.
     */
    @Serializable
    data object ArticleList : NavDestination()

    /**
     * The ArticleDetailView screen.
     */
    @Serializable
    data class ArticleDetail(val articleId: String, val title: String) : NavDestination()

    companion object {
        /**
         * Convenience function that will return the baseRoute for a Given route.
         * Used to compare current destinations.
         */
        fun baseRoute(route: String): String =
            route.split(Regex("[?/]")).firstOrNull() ?: route
    }

    /**
     * Will return the route pattern for the destination.
     * Route patterns are used by the Host (and by Compose Navigation) to
     * determine which composable should be presented for each Destination.
     */
    @Transient
    val route: String
        get() = when (this) {
            Back -> createRoutePattern<Back>()
            ArticleList -> createRoutePattern<ArticleList>()
            is ArticleDetail -> createRoutePattern<ArticleDetail>()
            /**
             * This is necessary due to a quirk with how kotlinx.serialization wants to crawl the
             * class structure before it has finished initializing, even though it's been marked
             * @Transient and shouldn't be crawling it. Because of this, we must provide
             * a fallthrough route name when this happens (or the app crashes). So we use the
             * java class name as default. This does not affect normal usage.
             */
            else -> this.javaClass.toString()
        }

    /**
     * Returns the base of a route without argument parameters.
     * Used for comparisons in the navigation stack.
     */
    @Transient
    val baseRoute: String =
        NavDestination.baseRoute(route)
}