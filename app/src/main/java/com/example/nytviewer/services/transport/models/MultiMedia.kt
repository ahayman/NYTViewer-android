package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MultiMedia(
    @Json(name = "url")
    val url: String = "",

    @Json(name = "format")
    val format: String = "",

    @Json(name = "type")
    val type: String = "",

    @Json(name = "subtype")
    val subtype: String = "",

    @Json(name = "caption")
    val caption: String = "",

    @Json(name = "height")
    val height: Int = 0,

    @Json(name = "width")
    val width: Int = 0,
)
