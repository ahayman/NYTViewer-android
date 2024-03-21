package com.example.nytviewer.services.transport

import com.example.nytviewer.services.transport.apis.NYTimesAPI
import com.example.nytviewer.services.transport.models.Article
import com.example.nytviewer.services.transport.models.PeriodRequest
import com.example.nytviewer.services.transport.models.Section
import com.example.nytviewer.services.transport.utils.TransportResponse
import com.example.nytviewer.services.transport.utils.getAPIResults

/**
 * A Concrete implementation of the TransportServiceInterface.
 * Requires a retrofit NYTimes API to make the required calls.
 */
class TransportService(private val api: NYTimesAPI): TransportServiceInterface {
    override suspend fun getSectionList(): TransportResponse<List<Section>> {
        return try {
            api.getSectionList().getAPIResults()
        } catch (e: Exception) {
            TransportResponse.Error(null, e.message ?: "Unknown error")
        }
    }

    override suspend fun getSectionArticles(
        section: String,
        limit: Int,
        offset: Int
    ): TransportResponse<List<Article>> {
        return try {
            api.getSectionArticles(section, limit, offset).getAPIResults()
        } catch (e: Exception) {
            TransportResponse.Error(null, e.message ?: "Unknown error")
        }
    }

    override suspend fun getPopularEmailed(period: PeriodRequest): TransportResponse<List<Article>> {
        return try {
            api.getPopularEmailed(period.period).getAPIResults()
        } catch (e: Exception) {
            TransportResponse.Error(null, e.message ?: "Unknown error")
        }
    }

    override suspend fun getPopularShared(period: PeriodRequest): TransportResponse<List<Article>> {
        return try {
            api.getPopularShared(period.period).getAPIResults()
        } catch (e: Exception) {
            TransportResponse.Error(null, e.message ?: "Unknown error")
        }
    }

    override suspend fun getPopularViewed(period: PeriodRequest): TransportResponse<List<Article>> {
        return try {
            api.getPopularViewed(period.period).getAPIResults()
        } catch (e: Exception) {
            TransportResponse.Error(null, e.message ?: "Unknown error")
        }
    }

}