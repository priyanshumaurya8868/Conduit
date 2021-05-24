package com.priyanshumaurya8868.realworld.io.api.model.entites


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "body")
    val body: String, // You have to believe
    @Json(name = "description")
    val description: String, // Ever wonder how?
    @Json(name = "tagList")
    val tagList: List<String>?,
    @Json(name = "title")
    val title: String // How to train your dragon
)