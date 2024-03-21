package com.example.nytviewer.services.transport

import com.example.nytviewer.services.transport.models.Article
import com.example.nytviewer.services.transport.models.PeriodRequest
import com.example.nytviewer.services.transport.models.Section
import com.example.nytviewer.services.transport.utils.TransportResponse

/**
 * The Transport Service is responsible for using a network api interface to retrieve data
 * from the internet.
 */
interface TransportServiceInterface {
    /// Retrieve list of article sections sections. These can be used to get section articles.
    suspend fun getSectionList(): TransportResponse<List<Section>>

    /**
     * Get a batch of articles for a section. It must be a section returned by [getSectionList].
     * Batches must be in an interval of 20 up to at most 500. Offsets also much be in an interval
     * of 20.
     */
    suspend fun getSectionArticles(section: String, limit: Int = 20, offset: Int = 0): TransportResponse<List<Article>>

    /**
     * Get the most popular emailed articles over the request period.
     */
    suspend fun getPopularEmailed(period: PeriodRequest): TransportResponse<List<Article>>

    /**
     * Get the most popular shared articles over the request period.
     */
    suspend fun getPopularShared(period: PeriodRequest): TransportResponse<List<Article>>

    /**
     * Get the most popular viewed articles over the request period.
     */
    suspend fun getPopularViewed(period: PeriodRequest): TransportResponse<List<Article>>
}