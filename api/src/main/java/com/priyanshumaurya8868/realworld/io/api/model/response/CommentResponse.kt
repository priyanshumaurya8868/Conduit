package com.priyanshumaurya8868.realworld.io.api.model.response


import com.priyanshumaurya8868.realworld.io.api.model.entites.GetComment
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentResponse(
    @Json(name = "comment")
    val comments: GetComment
)