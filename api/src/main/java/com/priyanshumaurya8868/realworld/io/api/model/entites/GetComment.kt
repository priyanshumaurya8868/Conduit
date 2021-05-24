package com.priyanshumaurya8868.realworld.io.api.model.entites


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetComment(
    @Json(name = "author")
    val author: Profile,
    @Json(name = "body")
    val body: String,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "updatedAt")
    val updatedAt: String
)