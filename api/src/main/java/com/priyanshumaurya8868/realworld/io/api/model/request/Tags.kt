package com.priyanshumaurya8868.realworld.io.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tags(
    @Json(name = "tags")
    val tags: List<String>
)