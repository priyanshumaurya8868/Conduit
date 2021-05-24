package com.priyanshumaurya8868.realworld.io.api.model.entites


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "email")
    val email: String,
    @Json(name = "image")
    val image: String?,
    @Json(name = "token")
    val token: String,
    @Json(name = "username")
    val username: String
)