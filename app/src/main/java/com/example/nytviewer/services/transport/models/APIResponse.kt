package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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