package com.example.nytviewer.services.transport.apis

import com.example.nytviewer.services.transport.models.APIResponse
import com.example.nytviewer.services.transport.models.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MostPopularAPIs {
    @GET("mostpopular/v2/emailed/{period}.json")
    suspend fun getPopularEmailed(@Path("period") period: Int): Response<APIResponse<Article>>

    @GET("mostpopular/v2/shared/{period}.json")
    suspend fun getPopularShared(@Path("period") period: Int): Response<APIResponse<Article>>

    @GET("mostpopular/v2/viewed/{period}.json")
    suspend fun getPopularViewed(@Path("period") period: Int): Response<APIResponse<Article>>
}