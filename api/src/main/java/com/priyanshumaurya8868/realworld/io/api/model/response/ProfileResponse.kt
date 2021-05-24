package com.priyanshumaurya8868.realworld.io.api.model.response


import com.priyanshumaurya8868.realworld.io.api.model.entites.Profile
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    @Json(name = "profile")
    val profile: Profile
)