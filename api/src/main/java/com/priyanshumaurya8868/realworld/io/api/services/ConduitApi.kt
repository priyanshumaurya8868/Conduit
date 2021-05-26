package com.priyanshumaurya8868.realworld.io.api.services

import com.priyanshumaurya8868.realworld.io.api.model.request.LoginRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.SignupUserRequest
import com.priyanshumaurya8868.realworld.io.api.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface   ConduitApi {

    @GET("articles")
    suspend fun getArticles(
        @Query("author") author: String? = null,
        @Query("favourited") favourited: String? = null,
        @Query("tag") tag: String? = null
    ): Response<ArticlesResponse>

    @POST("users")
    suspend fun signupUser(
        @Body userCreds: SignupUserRequest
    ): Response<UserResponse>

    @POST("users/login")
    suspend fun loginRequest(
        @Body userData: LoginRequest
    ): Response<UserResponse>

    @GET("articles/{slug}")
    suspend fun getArticleBySlug(
        @Path("slug") slug: String
    ): Response<ArticleResponse>

    @GET("tags")
    suspend fun getTags(): Response<TagsResponse>

    @GET("articles/{slug}/comments")
    suspend fun getComments(
        @Path("slug") slug: String
    ):Response<CommentsResponse>

    @GET("profiles/{username}")
    suspend fun getProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @PUT("articles/{slug}")
    suspend fun updateArticle(){

    }
}