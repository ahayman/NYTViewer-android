package com.example.nytviewer.services.transport

import com.example.nytviewer.services.transport.models.Article
import com.example.nytviewer.services.transport.models.PeriodRequest
import com.example.nytviewer.services.transport.models.Section
import com.example.nytviewer.services.transport.utils.TransportResponse

interface TransportServiceInterface {
    suspend fun getSectionList(): TransportResponse<List<Section>>
    suspend fun getSectionArticles(section: String, limit: Int = 20, offset: Int = 0): TransportResponse<List<Article>>
    suspend fun getPopularEmailed(period: PeriodRequest): TransportResponse<List<Article>>
    suspend fun getPopularShared(period: PeriodRequest): TransportResponse<List<Article>>
    suspend fun getPopularViewed(period: PeriodRequest): TransportResponse<List<Article>>
}