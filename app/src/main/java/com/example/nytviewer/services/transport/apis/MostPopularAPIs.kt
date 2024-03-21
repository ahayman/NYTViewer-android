package com.example.nytviewer.services.transport.apis

import com.example.nytviewer.services.transport.models.APIResponse
import com.example.nytviewer.services.transport.models.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * These APIs return a list of articles for NYTimes most popular content.
 * Generally, they only return the top 20 articles. More article cannot be batched.
 * Could return less if a smaller period is specified.
 *
 * All APIs have a "period", which must be one of:
 *  - 1 day
 *  - 7 days
 *  - 30 days
 *
 *  See: [PeriodRequest]
 */
interface MostPopularAPIs {
    /// Get most popular emailed articles
    @GET("mostpopular/v2/emailed/{period}.json")
    suspend fun getPopularEmailed(@Path("period") period: Int): Response<APIResponse<Article>>

    /// Get most popular shared articles
    @GET("mostpopular/v2/shared/{period}.json")
    suspend fun getPopularShared(@Path("period") period: Int): Response<APIResponse<Article>>

    /// Get most popular viewed articles
    @GET("mostpopular/v2/viewed/{period}.json")
    suspend fun getPopularViewed(@Path("period") period: Int): Response<APIResponse<Article>>
}