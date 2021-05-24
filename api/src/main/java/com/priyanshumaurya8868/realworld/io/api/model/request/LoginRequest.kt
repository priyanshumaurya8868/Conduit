package com.priyanshumaurya8868.realworld.io.api.model.request


import com.priyanshumaurya8868.realworld.io.api.model.entites.LoginData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "user")
    val user: LoginData
)