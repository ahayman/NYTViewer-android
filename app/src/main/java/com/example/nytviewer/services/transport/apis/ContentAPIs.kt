package com.example.nytviewer.services.transport.apis

import com.example.nytviewer.services.transport.models.APIResponse
import com.example.nytviewer.services.transport.models.Article
import com.example.nytviewer.services.transport.models.Section
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentAPIs {
    @GET("news/v3/content/section-list.json")
    suspend fun getSectionList(): Response<APIResponse<Section>>

    @GET("svc/news/v3/content/all/{section}.json")
    suspend fun getSectionArticles(
        @Path("section") section: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<APIResponse<Article>>
}