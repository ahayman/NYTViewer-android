package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A Media item contains its type (ex: image, video) along with all the variants
 * available in [MediaMetadata].
 */
@JsonClass(generateAdapter = true)
data class MediaItem(
    @Json(name = "type")
    val type: String = "",

    @Json(name = "subtype")
    val subtype: String = "",

    @Json(name = "caption")
    val caption: String = "",

    @Json(name = "media-metadata")
    val metadata: List<MediaMetadata>
)
