package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a specific instance of a media. Some media may have multiple. Ex: different
 * sizes of images, which are specified in the [height] and [width] properties.
 */
@JsonClass(generateAdapter = true)
data class MediaMetadata(
    @Json(name = "url")
    val url: String = "",

    @Json(name = "format")
    val format: String = "",

    @Json(name = "height")
    val height: Int = 0,

    @Json(name = "width")
    val width: Int = 0,
)
