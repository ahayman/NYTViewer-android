package com.example.nytviewer.navigation

import androidx.navigation.NamedNavArgument
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.createRoutePattern
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

sealed class NavDestination : Destination {
    @Serializable data object Back : NavDestination()
    @Serializable data object ArticleList : NavDestination()
    @Serializable data class  ArticleDetail(val articleId: String) : NavDestination()

    companion object {
        fun baseRoute(route: String): String =
            route.split(Regex("[?/]")).firstOrNull() ?: route
    }

    @OptIn(ExperimentalSerializationApi::class)
    val route: String = when (this) {
        Back -> createRoutePattern<Back>()
        ArticleList -> createRoutePattern<ArticleList>()
        is ArticleDetail -> createRoutePattern<ArticleDetail>()
    }

    /**
     * Returns the base of a route without argument parameters.
     * Used for comparisons in the navigation stack.
     */
    val baseRoute: String =
        route.split(Regex("[?/]")).firstOrNull() ?: route

    fun hydratedRoute(namedNavArguments: List<NamedNavArgument>): String {
        var hydratedRoute = route
        namedNavArguments.forEach {
            hydratedRoute = hydratedRoute.replace(it.name, it.argument.defaultValue.toString())
        }
        return hydratedRoute
    }
}