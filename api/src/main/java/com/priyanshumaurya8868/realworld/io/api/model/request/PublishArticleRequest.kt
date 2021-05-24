package com.priyanshumaurya8868.realworld.io.api.model.request

import com.priyanshumaurya8868.realworld.io.api.model.entites.Article
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PublishArticleRequest(
    @Json(name = "article")
    val article: Article?
)