package com.priyanshumaurya8868.realworld.io.api.model.request


import com.priyanshumaurya8868.realworld.io.api.model.entites.Comment
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentRequest(
    @Json(name = "comment")
    val comment: Comment?
)