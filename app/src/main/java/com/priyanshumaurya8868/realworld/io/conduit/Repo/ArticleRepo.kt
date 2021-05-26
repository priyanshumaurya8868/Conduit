package com.priyanshumaurya8868.realworld.io.conduit.Repo

import com.priyanshumaurya8868.realworld.io.api.MyConduitClient
import com.priyanshumaurya8868.realworld.io.api.model.entites.Article
import com.priyanshumaurya8868.realworld.io.api.model.entites.Comment
import com.priyanshumaurya8868.realworld.io.api.model.request.CommentRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.PublishArticleRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.UpdateArticleRequest

object ArticleRepo {
    private val api = MyConduitClient.publicApi
    private val authApi = MyConduitClient.authApi
    suspend fun getGlobalFeeds() =
        if (MyConduitClient.authToken == null) api.getArticles() else authApi.getArticles()

    suspend fun getMyFeeds() = authApi.getFeedArticles()

    suspend fun publishArticle(publishedArticleReq: PublishArticleRequest) =
        authApi.publishArticle(publishedArticleReq)

    suspend fun favouriteArticle(slug: String) = authApi.favoriteArticle(slug)

    suspend fun unfavouriteArticle(slug: String) = authApi.unfavoriteArticle(slug)

    suspend fun getComments(slug: String) =
        if (MyConduitClient.authToken == null) api.getComments(slug) else authApi.getComments(slug)

    suspend fun postComments(slug: String, comment: Comment) {
        authApi.postComment(slug, CommentRequest(comment))
    }

    suspend fun getArticles(
        author: String? = null,
        favourited: String? = null,
        tag: String? = null
    ) = if (MyConduitClient.authToken == null) api.getArticles(
        author,
        favourited,
        tag
    ) else authApi.getArticles(author, favourited, tag)

    suspend fun updateArticle(slug: String, article: Article) =
        authApi.updateArticle(slug, UpdateArticleRequest(article))

    suspend fun deleteArticle(slug: String) =
        authApi.deleteArticle(slug)

    suspend fun deleteComment(slug: String,id  :Int) =
        authApi.deleteComment(slug, id)
}
