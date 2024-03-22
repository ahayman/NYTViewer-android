package com.example.nytviewer.repos.articles

import com.example.nytviewer.repos.articles.models.ArticleBrief
import com.example.nytviewer.repos.articles.models.ArticleRepoActions
import com.example.nytviewer.repos.articles.models.ArticleSection
import com.example.nytviewer.repos.articles.models.ListDef
import com.example.nytviewer.services.transport.TransportService
import com.example.nytviewer.services.transport.mocks.createArticle
import com.example.nytviewer.services.transport.mocks.createSection
import com.example.nytviewer.services.transport.models.PeriodRequest
import com.example.nytviewer.services.transport.utils.TransportResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesRepoTest {

    @Mock
    private lateinit var transportService: TransportService

    @Mock
    private lateinit var articlesRepo: ArticlesRepo

    @OptIn(DelicateCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        articlesRepo = ArticlesRepo(transportService)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial load`() = runBlocking {
        assertEquals(articlesRepo.state.value.sections.count(), 0)
        assertEquals(articlesRepo.state.value.articles.count(), 0)
        assertEquals(articlesRepo.state.value.listDef.id, ListDef.PopularViewed.id)
    }

    @Test
    fun `test initial error`() = runBlocking {
        assertNull(articlesRepo.error.value)
    }

    @Test
    fun `test getArticle success`() = runBlocking {
        //Grab some articles
        val refreshedArticles = listOf(createArticle(10), createArticle(11))
        Mockito.`when`(transportService.getPopularViewed(PeriodRequest.Month))
            .thenReturn(TransportResponse.Success(refreshedArticles))
        articlesRepo.execute(ArticleRepoActions.RefreshArticles)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)

        var article = articlesRepo.getArticle(refreshedArticles[0].uri)
        assertNotNull(article)
        assertEquals(article!!.uri, refreshedArticles[0].uri)

        article = articlesRepo.getArticle(refreshedArticles[1].uri)
        assertNotNull(article)
        assertEquals(article!!.uri, refreshedArticles[1].uri)
    }

    @Test
    fun `test getArticle failure`() = runBlocking {
        val article = articlesRepo.getArticle("invalid_uri")
        assertNull(article)
    }

    @Test
    fun `test RefreshSections action success`() = runBlocking {
        val refreshedSections = listOf(createSection(10), createSection(11), createSection(12))
        Mockito.`when`(transportService.getSectionList())
            .thenReturn(TransportResponse.Success(refreshedSections))
        articlesRepo.execute(ArticleRepoActions.RefreshSections)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value.sections, refreshedSections.map { ArticleSection(it.section, it.title)})
    }

    @Test
    fun `test action failure emits error`() = runBlocking {
        val error = "Not Found"
        val articles = articlesRepo.state.value.articles
        Mockito.`when`(transportService.getSectionList())
            .thenReturn(TransportResponse.Error(400, error))
        articlesRepo.execute(ArticleRepoActions.RefreshSections)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value.articles, articles)
        assertEquals(articlesRepo.error.value, "(400) $error")
    }

    @Test
    fun `test RefreshArticles action success`() = runBlocking {
        val refreshedArticles = listOf(createArticle(10), createArticle(11), createArticle(12), createArticle(13))
        Mockito.`when`(transportService.getPopularViewed(PeriodRequest.Month))
            .thenReturn(TransportResponse.Success(refreshedArticles))
        articlesRepo.execute(ArticleRepoActions.RefreshArticles)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value.articles, refreshedArticles.briefs())
    }

    @Test
    fun `test LoadMoreArticles action fails when not a SectionList`() = runBlocking {
        val state = articlesRepo.state.value
        articlesRepo.execute(ArticleRepoActions.LoadMoreArticles)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value, state)
    }

    @Test
    fun `test SelectList(section) action loads new articles`() = runBlocking {
        val sectionArticles = (0..<100).map { createArticle(it) }
        val section = "section"
        val listDef = ListDef.Section(ArticleSection(section, section))
        Mockito.`when`(transportService.getSectionArticles(listDef.section.id, 100, 0))
            .thenReturn(TransportResponse.Success(sectionArticles))
        articlesRepo.execute(ArticleRepoActions.SelectList(listDef))
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value.articles, sectionArticles.briefs())
    }

    @Test
    fun `test SelectList to prior list loads cached articles`() = runBlocking {
        val refreshedArticles = listOf(createArticle(10), createArticle(11), createArticle(12), createArticle(13))
        Mockito.`when`(transportService.getPopularViewed(PeriodRequest.Month))
            .thenReturn(TransportResponse.Success(refreshedArticles))
        articlesRepo.execute(ArticleRepoActions.RefreshArticles)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        /// Note: Invocations include the init load, which is why we expect 2 here
        verify(transportService, times(2)).getPopularViewed(PeriodRequest.Month)
        assertEquals(articlesRepo.state.value.articles, refreshedArticles.briefs())

        val newArticles = (0..<100).map { createArticle(it) }
        val listDef = ListDef.PopularShared
        Mockito.`when`(transportService.getPopularShared(PeriodRequest.Month))
            .thenReturn(TransportResponse.Success(newArticles))
        articlesRepo.execute(ArticleRepoActions.SelectList(listDef))
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value.articles, newArticles.briefs())

        Mockito.`when`(transportService.getPopularViewed(PeriodRequest.Month))
            .thenReturn(TransportResponse.Success(listOf()))
        articlesRepo.execute(ArticleRepoActions.SelectList(ListDef.PopularViewed))
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        verify(transportService, times(2)).getPopularViewed(PeriodRequest.Month)
        assertEquals(articlesRepo.state.value.articles, refreshedArticles.briefs())
    }

    @Test
    fun `test LoadMoreArticles action success`() = runBlocking {
        val sectionArticles = (0..<100).map { createArticle(it) }
        val section = "section"
        val listDef = ListDef.Section(ArticleSection(section, section))
        Mockito.`when`(transportService.getSectionArticles(listDef.section.id, 100, 0))
            .thenReturn(TransportResponse.Success(sectionArticles))
        articlesRepo.execute(ArticleRepoActions.SelectList(listDef))
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value.articles, sectionArticles.briefs())

        val newArticles = (100..<130).map { createArticle(it) }
        Mockito.`when`(transportService.getSectionArticles(section, 100, 100))
            .thenReturn(TransportResponse.Success(newArticles))
        articlesRepo.execute(ArticleRepoActions.LoadMoreArticles)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)

        val allArticles = (sectionArticles + newArticles).briefs()
        assertEquals(articlesRepo.state.value.articles, allArticles)
    }

    @Test
    fun `test LoadMoreArticles action no-op when list not multiple of 100`() = runBlocking {
        val sectionArticles = (0..<50).map { createArticle(it) }
        val section = "section"
        val listDef = ListDef.Section(ArticleSection(section, section))
        Mockito.`when`(transportService.getSectionArticles(listDef.section.id, 100, 0))
            .thenReturn(TransportResponse.Success(sectionArticles))
        articlesRepo.execute(ArticleRepoActions.SelectList(listDef))
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)
        assertEquals(articlesRepo.state.value.articles, sectionArticles.briefs())

        val newArticles = (100..<130).map { createArticle(it) }
        Mockito.`when`(transportService.getSectionArticles(section, 100, 100))
            .thenReturn(TransportResponse.Success(newArticles))
        articlesRepo.execute(ArticleRepoActions.LoadMoreArticles)
        /// Delay is not optional: results are emitted from a different scope, so we need to wait briefly
        delay(100)

        assertEquals(articlesRepo.state.value.articles, sectionArticles.briefs())
    }
}