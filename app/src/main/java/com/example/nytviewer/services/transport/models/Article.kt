package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "url")
    var url: String = "",

    @Json(name = "uri")
    var uri: String = "",

    @Json(name = "subsection")
    var subsection: String = "",

    @Json(name = "section")
    var section: String = "",

    @Json(name = "title")
    var title: String = "",

    @Json(name = "byline")
    var byline: String = "",

    @Json(name = "abstract")
    var abstract: String = "",

    @Json(name = "media")
    val media: List<MediaItem>? = null,

    @Json(name = "multimedia")
    val multiMedia: List<MultiMedia>? = null
)
