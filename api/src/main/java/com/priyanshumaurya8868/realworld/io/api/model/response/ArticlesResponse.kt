package com.priyanshumaurya8868.realworld.io.api.model.response


import com.priyanshumaurya8868.realworld.io.api.model.entites.GetArticle
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticlesResponse(
    @Json(name = "articles")
    val getArticles: List<GetArticle>,
    @Json(name = "articlesCount")
    val articlesCount: Int
)