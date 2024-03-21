package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A section represents articles sections/categories like: Politics, Automotive, Books, etc.
 * The Section object include [section] (basically the ID usable in other apis) and
 * [title], which is a human readable label for the section.
 */
@JsonClass(generateAdapter = true)
data class Section(
    @Json(name = "section")
    val section: String = "",

    @Json(name = "display_name")
    val title: String = "",
)

