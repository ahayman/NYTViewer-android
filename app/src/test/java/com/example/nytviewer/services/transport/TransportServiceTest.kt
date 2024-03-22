package com.example.nytviewer.services.transport

import com.example.nytviewer.services.transport.apis.NYTimesAPI
import com.example.nytviewer.services.transport.mocks.createArticle
import com.example.nytviewer.services.transport.mocks.createSection
import com.example.nytviewer.services.transport.models.APIResponse
import com.example.nytviewer.services.transport.models.Article
import com.example.nytviewer.services.transport.models.PeriodRequest
import com.example.nytviewer.services.transport.models.Section
import com.example.nytviewer.services.transport.utils.TransportResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class TransportServiceTests {

    @Mock
    private lateinit var api: NYTimesAPI

    private lateinit var transport: TransportService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        transport = TransportService(api)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getSectionList success`() = runBlocking {
        val sections = listOf(createSection(1), createSection(2))
        val apiResponse: Response<APIResponse<Section>> = Response.success(
            APIResponse(
                status = "success",
                results = sections
            )
        )
        Mockito.`when`(api.getSectionList()).thenReturn(apiResponse)
        when (val response = transport.getSectionList()) {
            is TransportResponse.Error -> throw Error("Should not receive an Error")
            is TransportResponse.Success -> {
                assertEquals(response.data, sections)
            }
        }
    }

    @Test
    fun `test getSectionList failure`() = runBlocking {
        val error = "Not Found Error"
        val apiResponse: Response<APIResponse<Section>> = Response.error(400, error.toResponseBody())
        Mockito.`when`(api.getSectionList()).thenReturn(apiResponse)
        when (val response = transport.getSectionList()) {
            is TransportResponse.Error -> {
                assertEquals(response.code, 400)
                assertEquals(response.message, error)
            }
            is TransportResponse.Success -> throw Error("Should not be success")
        }
    }

    @Test
    fun `test getSectionArticles success`() = runBlocking {
        val articles = listOf(createArticle(1), createArticle(2), createArticle(3))
        val sectionName = "Section"
        val apiResponse: Response<APIResponse<Article>> = Response.success(
            APIResponse(
                status = "success",
                results = articles
            )
        )
        Mockito.`when`(api.getSectionArticles(sectionName, 20, 0)).thenReturn(apiResponse)
        when (val response = transport.getSectionArticles(sectionName, 20, 0)) {
            is TransportResponse.Error -> throw Error("Should not receive an Error")
            is TransportResponse.Success -> {
                assertEquals(response.data, articles)
            }
        }
    }

    @Test
    fun `test getSectionArticles failure`() = runBlocking {
        val sectionName = "Section"
        val error = "Not Found Error"
        val apiResponse: Response<APIResponse<Article>> = Response.error(400, error.toResponseBody())
        Mockito.`when`(api.getSectionArticles(sectionName, 20, 0)).thenReturn(apiResponse)
        when (val response = transport.getSectionArticles(sectionName, 20, 0)) {
            is TransportResponse.Error -> {
                assertEquals(response.code, 400)
                assertEquals(response.message, error)
            }
            is TransportResponse.Success -> throw Error("Should not be success")
        }
    }

    @Test
    fun `test getPopularEmailed success`() = runBlocking {
        val articles = listOf(createArticle(1), createArticle(2), createArticle(3))
        val apiResponse: Response<APIResponse<Article>> = Response.success(
            APIResponse(
                status = "success",
                results = articles
            )
        )
        Mockito.`when`(api.getPopularEmailed(1)).thenReturn(apiResponse)
        when (val response = transport.getPopularEmailed(PeriodRequest.Day)) {
            is TransportResponse.Error -> throw Error("Should not receive an Error")
            is TransportResponse.Success -> {
                assertEquals(response.data, articles)
            }
        }
    }

    @Test
    fun `test getPopularEmailed failure`() = runBlocking {
        val error = "Not Found Error"
        val apiResponse: Response<APIResponse<Article>> = Response.error(400, error.toResponseBody())
        Mockito.`when`(api.getPopularEmailed(1)).thenReturn(apiResponse)
        when (val response = transport.getPopularEmailed(PeriodRequest.Day)) {
            is TransportResponse.Error -> {
                assertEquals(response.code, 400)
                assertEquals(response.message, error)
            }
            is TransportResponse.Success -> throw Error("Should not be success")
        }
    }

    @Test
    fun `test getPopularShared success`() = runBlocking {
        val articles = listOf(createArticle(1), createArticle(2), createArticle(3))
        val apiResponse: Response<APIResponse<Article>> = Response.success(
            APIResponse(
                status = "success",
                results = articles
            )
        )
        Mockito.`when`(api.getPopularShared(1)).thenReturn(apiResponse)
        when (val response = transport.getPopularShared(PeriodRequest.Day)) {
            is TransportResponse.Error -> throw Error("Should not receive an Error")
            is TransportResponse.Success -> {
                assertEquals(response.data, articles)
            }
        }
    }

    @Test
    fun `test getPopularShared failure`() = runBlocking {
        val error = "Not Found Error"
        val apiResponse: Response<APIResponse<Article>> = Response.error(400, error.toResponseBody())
        Mockito.`when`(api.getPopularShared(1)).thenReturn(apiResponse)
        when (val response = transport.getPopularShared(PeriodRequest.Day)) {
            is TransportResponse.Error -> {
                assertEquals(response.code, 400)
                assertEquals(response.message, error)
            }
            is TransportResponse.Success -> throw Error("Should not be success")
        }
    }

    @Test
    fun `test getPopularViewed success`() = runBlocking {
        val articles = listOf(createArticle(1), createArticle(2), createArticle(3))
        val apiResponse: Response<APIResponse<Article>> = Response.success(
            APIResponse(
                status = "success",
                results = articles
            )
        )
        Mockito.`when`(api.getPopularViewed(1)).thenReturn(apiResponse)
        when (val response = transport.getPopularViewed(PeriodRequest.Day)) {
            is TransportResponse.Error -> throw Error("Should not receive an Error")
            is TransportResponse.Success -> {
                assertEquals(response.data, articles)
            }
        }
    }

    @Test
    fun `test getPopularViewed failure`() = runBlocking {
        val error = "Not Found Error"
        val apiResponse: Response<APIResponse<Article>> = Response.error(400, error.toResponseBody())
        Mockito.`when`(api.getPopularViewed(1)).thenReturn(apiResponse)
        when (val response = transport.getPopularViewed(PeriodRequest.Day)) {
            is TransportResponse.Error -> {
                assertEquals(response.code, 400)
                assertEquals(response.message, error)
            }
            is TransportResponse.Success -> throw Error("Should not be success")
        }
    }
}