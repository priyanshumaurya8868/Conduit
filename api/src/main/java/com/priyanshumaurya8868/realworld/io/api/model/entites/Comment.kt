package com.priyanshumaurya8868.realworld.io.api.model.entites


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "body")
    val body: String? // His name was my name too.
)