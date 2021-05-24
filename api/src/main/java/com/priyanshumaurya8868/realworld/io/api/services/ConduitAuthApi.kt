package com.priyanshumaurya8868.realworld.io.api.services

import com.priyanshumaurya8868.realworld.io.api.model.request.CommentRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.PublishArticleRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.UpdateUserRequest
import com.priyanshumaurya8868.realworld.io.api.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ConduitAuthApi {
    @GET("user")
    suspend fun getUser(): Response<UserResponse>

    @PUT("user")
    suspend fun updateUser(
        @Body updateUserRequest: UpdateUserRequest
    ): Response<UserResponse>

    @GET("profiles/{username}")
    suspend fun getProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @POST("profiles/{username}/follow")
    suspend fun followProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @DELETE("profiles/{username}/follow")
    suspend fun unfollowProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @GET("articles/feed")
    suspend fun getFeedArticles(): Response<ArticlesResponse>

    @POST("articles/{slug}/favorite")
    suspend fun favoriteArticle(
        @Path("slug") slug: String
    ): Response<ArticleResponse>

    @DELETE("articles/{slug}/favorite")
    suspend fun unfavoriteArticle(
        @Path("slug") slug: String
    ): Response<ArticleResponse>

    @POST("articles")
    suspend fun publishArticle(
@Body publishArticleRequest: PublishArticleRequest
    ): Response<ArticleResponse>

    @GET("articles")
    suspend fun getArticles(
        @Query("author") author: String? = null,
        @Query("favorited") favorited: String? = null,
        @Query("tag") tag: String? = null
    ): Response<ArticlesResponse>

    @GET("articles/{slug}")
    suspend fun getArticleBySlug(
        @Path("slug") slug: String
    ): Response<ArticleResponse>

    @POST("articles/{slug}/comments")
    suspend fun postComment(
        @Path("slug") slug : String,
        @Body commentRequest: CommentRequest
    ): Response<CommentResponse>

    @GET("articles/{slug}/comments")
    suspend fun getComments(
        @Path("slug") slug: String
    ):Response<CommentsResponse>


}
