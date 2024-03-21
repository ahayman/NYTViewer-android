package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

/**
 * The article data returned by the apis.
 * Note: this represents both popular and section articles. For the most part, they're the same.
 * However, there's one important difference: media. Section and Popular articles use different
 * media formats. So both are included on the type as optional parameters.
 */
@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "url")
    var url: String = "",

    @Json(name = "uri")
    var uri: String = "",

    @Json(name = "published_date")
    var publishedDate: Date = Date(),

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
