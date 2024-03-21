package com.example.nytviewer.repos.articles.models

/**
 * Defines all the list types of article briefs that can be fetched.
 */
sealed class ListDef(val id: String, val title: String) {
    /// A list of the most popular shared articles
    data object PopularShared : ListDef("popularShared", "Popular Shared Articles")
    /// A list of the most popular emailed articles
    data object PopularEmailed : ListDef("popularEmailed", "Popular Emailed Articles")
    /// A List of the most popular Viewed articles
    data object PopularViewed : ListDef("popularViewed", "Popular Viewed Articles")
    /// A list of recent articles for a particular section. (Sections must be retrieved separately)
    data class Section(val section: ArticleSection): ListDef(section.id, section.label)

    val canLoadMore: Boolean get() = when (this) {
        PopularEmailed -> false
        PopularShared -> false
        PopularViewed -> false
        is Section -> true
    }
}