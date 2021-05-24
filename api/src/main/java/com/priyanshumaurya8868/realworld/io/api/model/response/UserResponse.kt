package com.priyanshumaurya8868.realworld.io.api.model.response


import com.priyanshumaurya8868.realworld.io.api.model.entites.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "user")
    val user: User
)