package com.example.nytviewer.repos.articles

import com.example.nytviewer.repos.articles.models.ArticleBrief
import com.example.nytviewer.repos.articles.models.ArticleDetail
import com.example.nytviewer.repos.articles.models.ArticleRepoActions
import com.example.nytviewer.repos.articles.models.ArticleRepoState
import com.example.nytviewer.repos.articles.models.ArticleSection
import com.example.nytviewer.repos.articles.models.ListDef
import com.example.nytviewer.services.transport.TransportServiceInterface
import com.example.nytviewer.services.transport.models.Article
import com.example.nytviewer.services.transport.models.PeriodRequest
import com.example.nytviewer.services.transport.utils.TransportResponse
import com.example.nytviewer.utils.CacheExpiry
import com.example.nytviewer.utils.CachedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

/**
 * Convenience function that searches through an article to find the
 * smallest image it can find (probably a thumbnail).
 */
private fun smallImageIn(article: Article): String? =
    article.media?.firstOrNull { it.type == "image" }?.let { media ->
        media.metadata.minByOrNull { it.height * it.width }?.url
    } ?: article.multiMedia?.filter { it.type == "image" }?.minByOrNull { it.width * it.height }?.url

/**
 * Convenience function that searches through an article to find the
 * largest image it can find.
 */
private fun largeImageIn(article: Article): String? =
    article.media?.firstOrNull { it.type == "image" }?.let { media ->
        media.metadata.maxByOrNull { it.height * it.width }?.url
    } ?: article.multiMedia?.filter { it.type == "image" }?.maxByOrNull { it.width * it.height }?.url

/**
 * An extension to [List<Article>] data retrieved from the Transport service,
 * that maps it to a [List<ArticleBrief>] to be returned by the repo.
 * This is mostly here to made the the conversion cleaner in the repo code.
 */
fun List<Article>.briefs(): List<ArticleBrief> = this.map {
    ArticleBrief(
        uri = it.uri,
        date = it.publishedDate,
        title = it.title,
        byline = it.byline,
        imageUrl = smallImageIn(it)
    )
}

/**
 * Concrete implementation of the ArticlesRepoInterface.
 * Requires a TransportService to retrieve data from the apis.
 */
class ArticlesRepo @Inject constructor(
    private val transport: TransportServiceInterface,
) : ArticlesRepoInterface {
    /// When possible, api and data processing will occur in an IO scope.
    private val ioScope = CoroutineScope(Dispatchers.IO)
    /// A state data will be returned/emitted in the main scope.
    private val mainScope = CoroutineScope(Dispatchers.Main)
    /// A cache of articles for each ListDef.id, so we don't unnecessarily hit the api.
    private val articleCache: MutableMap<String, CachedItem<List<Article>>> = mutableMapOf()
    /// Maps to Articles via the [Article.uri] property.
    private val articleMap: MutableMap<String, Article> = mutableMapOf()

    /// Required Error state for the State Reducer. Generally, it's used for API errors.
    override val error = MutableStateFlow<String?>(null)
    /// Required State for the reducer. This is the main state returned by the repo.
    override val state = MutableStateFlow(
        ArticleRepoState(
            sections = listOf(),
            articles = listOf(),
            listDef = ListDef.PopularViewed
        )
    )

    // When the repo starts up, preload the articles and section list
    init {
        submit(ArticleRepoActions.RefreshArticles)
        submit(ArticleRepoActions.RefreshSections)
    }

    override fun submit(action: ArticleRepoActions) {
        ioScope.launch { execute(action) }
    }

    override suspend fun execute(action: ArticleRepoActions) {
        mainScope.launch { error.value = null  }
        when (action) {
            ArticleRepoActions.LoadMoreArticles -> loadMoreArticles()
            ArticleRepoActions.RefreshArticles -> refreshCurrentArticles(state.value.listDef)
            ArticleRepoActions.RefreshSections -> refreshSectionList()
            is ArticleRepoActions.SelectList -> selectList(action.listDef)
        }
    }

    override suspend fun getArticle(uri: String): ArticleDetail? =
        articleMap[uri]?.let {
            ArticleDetail(
                uri = it.uri,
                url = URL(it.url),
                date = it.publishedDate,
                title = it.title,
                byline = it.byline,
                abstract = it.abstract,
                imageUrl = largeImageIn(it)
            )
        }

    /**
     * ============= Private functions =============
     */

    /// Simple function to add articles the article map.
    private fun applyArticlesToMap(articles: List<Article>) =
        articles.forEach { articleMap[it.uri] = it }

    /// Will retrieve articles from the Transport Service and return them
    private suspend fun getRefreshedArticles(listDef: ListDef): TransportResponse<List<Article>> =
        when (listDef) {
            ListDef.PopularEmailed -> transport.getPopularEmailed(PeriodRequest.Month)
            ListDef.PopularShared -> transport.getPopularShared(PeriodRequest.Month)
            ListDef.PopularViewed -> transport.getPopularViewed(PeriodRequest.Month)
            is ListDef.Section -> transport.getSectionArticles(listDef.section.id, 100)
        }

    /**
     * This retrieves current article for the given list, replaces the cache with them, and
     * updates the state with the new articles.
     */
    private suspend fun refreshCurrentArticles(listDef: ListDef) {
        when(val response = getRefreshedArticles(state.value.listDef)) {
            is TransportResponse.Success -> {
                val briefs = response.data.briefs()
                applyArticlesToMap(response.data)
                articleCache[listDef.id] = CachedItem(response.data, CacheExpiry.Hours(1))
                mainScope.launch {
                    state.update { it.copy(articles = briefs)  }
                }
            }
            is TransportResponse.Error -> {
                mainScope.launch {
                    error.value = response.errorMessage
                }
            }
        }
    }

    /**
     * Loads additional articles, updating the cache and state.
     * This only works for section articles. Popular articles have
     * no api to load additional data.
     */
    private suspend fun loadMoreArticles() {
        val listDef = state.value.listDef
        val articleResponse = when (listDef) {
            ListDef.PopularEmailed -> return
            ListDef.PopularShared -> return
            ListDef.PopularViewed -> return
            is ListDef.Section -> {
                val count = state.value.articles.count()
                //We grab batches of 100. The our count is not a multiple of 100, then we've reached the end
                if (count % 100 != 0) {
                    return
                } else {
                    transport.getSectionArticles(listDef.id, 100, count)
                }
            }
        }
        when (articleResponse) {
            is TransportResponse.Success -> {
                val articles = articleCache[listDef.id]
                    ?.let { it.item + articleResponse.data }
                    ?: articleResponse.data
                articleCache[listDef.id] = CachedItem(articles, CacheExpiry.Hours(1))
                val briefs = articles.briefs()
                mainScope.launch {
                    state.update { it.copy(articles = briefs) }
                }
            }
            is TransportResponse.Error -> {
                mainScope.launch {
                    error.value = articleResponse.errorMessage
                }
            }
        }
    }

    /**
     * Selecting a list will update the cache if that list has expired,
     * and update the state with the new list's articles along with the
     * requested listDef.
     */
    private suspend fun selectList(listDef: ListDef) {
        val cache = articleCache[listDef.id]
        if (cache != null && !cache.isExpired) {
            val briefs = cache.item.briefs()
            mainScope.launch {
                state.update {
                    it.copy(
                        articles = briefs,
                        listDef = listDef,
                    )
                }
            }
            return
        }
        when(val response = getRefreshedArticles(listDef)) {
            is TransportResponse.Success -> {
                val briefs = response.data.briefs()
                applyArticlesToMap(response.data)
                articleCache[listDef.id] = CachedItem(response.data, CacheExpiry.Hours(1))
                mainScope.launch {
                    state.update {
                        it.copy(
                            articles = briefs,
                            listDef = listDef,
                        )
                    }
                }
            }
            is TransportResponse.Error -> {
                mainScope.launch {
                    error.value = response.errorMessage
                }
            }
        }

    }

    /**
     * Refreshes the section list. This is normally not needed, as Sections
     * don't change very often.
     */
    private suspend fun refreshSectionList() {
        when (val response = transport.getSectionList()) {
            is TransportResponse.Success -> {
                val sections = response.data.map { ArticleSection(it.section, it.title) }
                mainScope.launch {
                    state.update { it.copy(sections = sections) }
                }
            }
            is TransportResponse.Error -> {
                mainScope.launch {
                    error.value = response.errorMessage
                }
            }
        }
    }
}