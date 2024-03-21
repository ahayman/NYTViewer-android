package com.example.nytviewer.services.transport.apis

import com.example.nytviewer.services.transport.models.APIResponse
import com.example.nytviewer.services.transport.models.Article
import com.example.nytviewer.services.transport.models.Section
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * All the APIs available for retrieving content.
 * This includes stuff like the sections and content for those
 * sections.
 *
 * Note: Their content API includes a way to grab a specific Article. That's not included
 * because it doesn't provide any more data than the Article List apis provide.
 */
interface ContentAPIs {
    /**
     * Retrieve all the sections available from the API.
     */
    @GET("news/v3/content/section-list.json")
    suspend fun getSectionList(): Response<APIResponse<Section>>

    /**
     * Get articles for a specific section.
     * These are batched. Limits and offsets must be multiples of 20, or the
     * api will return an error.  Highest possible limit is 500.
     */
    @GET("news/v3/content/all/{section}.json")
    suspend fun getSectionArticles(
        @Path("section") section: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<APIResponse<Article>>
}