package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * All API return data in a common format. This represents that format.
 * The api-specific results are returned in the `results` property, as a
 * list of items, even if there's only one result.
 */
@JsonClass(generateAdapter = true)
data class APIResponse<T> (
    @Json(name = "status")
    val status: String = "",

    @Json(name = "copyright")
    val copyright: String = "",

    @Json(name = "num_results")
    val resultCount: Int = 0,

    @Json(name = "results")
    val results: List<T> = listOf(),
)