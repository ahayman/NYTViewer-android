package com.example.nytviewer.services.transport.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Section(
    @Json(name = "section")
    val section: String = "",

    @Json(name = "display_name")
    val title: String = "",
)

