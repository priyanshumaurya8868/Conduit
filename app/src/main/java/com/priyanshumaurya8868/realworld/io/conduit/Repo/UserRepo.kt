package com.priyanshumaurya8868.realworld.io.conduit.Repo

import com.priyanshumaurya8868.realworld.io.api.MyConduitClient
import com.priyanshumaurya8868.realworld.io.api.model.entites.LoginData
import com.priyanshumaurya8868.realworld.io.api.model.entites.UpdateUserData
import com.priyanshumaurya8868.realworld.io.api.model.entites.User
import com.priyanshumaurya8868.realworld.io.api.model.entites.UserCreds
import com.priyanshumaurya8868.realworld.io.api.model.request.LoginRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.SignupUserRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.UpdateUserRequest

object UserRepo {
    private val api = MyConduitClient.publicApi
   private val authApi = MyConduitClient.authApi
    suspend fun userLogin(
        email: String,
        password: String
    ): User? {
        val response = api.loginRequest(LoginRequest(LoginData(email, password)))
        MyConduitClient.authToken = response.body()?.user?.token
        return response.body()?.user
    }

    suspend fun userSignin(
        username: String,
        email: String,
        password: String
    ): User? {
        val response = api.signupUser(SignupUserRequest(UserCreds(username, email, password)))
        MyConduitClient.authToken = response.body()?.user?.token
        return response.body()?.user
    }

    suspend fun updateUser(
        image: String?,
        username: String?,
        bio: String?,
        email: String?,
        password: String?
    ): User? {
        val response = authApi.updateUser(
            UpdateUserRequest(
                UpdateUserData(
                    image,
                    username,
                    bio,
                    email,
                    password
                )
            )
        )
        return response.body()?.user
    }

    suspend fun getArticleBySlug(slug: String) = if(MyConduitClient.authToken == null) api.getArticleBySlug(slug) else authApi.getArticleBySlug(slug)
    suspend fun getCurrentUserProfile(token: String): User? {
        MyConduitClient.authToken = token
        return authApi.getUser().body()?.user
    }

    suspend fun getProfile(username: String) = if(MyConduitClient.authToken == null) api.getProfile(username) else authApi.getProfile(username)

    suspend fun getMyProfile() = authApi.getUser()

    suspend fun followProfile(username :String)  = authApi.followProfile(username)

    suspend fun unFollowProfile(username: String) = authApi.unfollowProfile(username)

}