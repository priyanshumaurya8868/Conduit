package com.priyanshumaurya8868.realworld.io.api.model.request


import com.priyanshumaurya8868.realworld.io.api.model.entites.UpdateUserData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateUserRequest(
    @Json(name = "user")
    val user: UpdateUserData
)