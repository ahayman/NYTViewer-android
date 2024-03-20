package com.example.nytviewer.navigation

import androidx.navigation.NamedNavArgument
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.createRoutePattern
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.typeOf

@OptIn(ExperimentalSerializationApi::class)
sealed class NavDestination : Destination {
    @Serializable
    data object Back : NavDestination()
    @Serializable
    data object ArticleList : NavDestination()
    @Serializable
    data class  ArticleDetail(val articleId: String, val title: String) : NavDestination()

    companion object {
        fun baseRoute(route: String): String =
            route.split(Regex("[?/]")).firstOrNull() ?: route
    }

    @Transient
    val route: String
        get() = when (this) {
            Back -> createRoutePattern<Back>()
            ArticleList -> createRoutePattern<ArticleList>()
            is ArticleDetail -> createRoutePattern<ArticleDetail>()
            else -> this.javaClass.toString()
        }

    /**
     * Returns the base of a route without argument parameters.
     * Used for comparisons in the navigation stack.
     */
    @Transient
    val baseRoute: String =
        route.split(Regex("[?/]")).firstOrNull() ?: route
}