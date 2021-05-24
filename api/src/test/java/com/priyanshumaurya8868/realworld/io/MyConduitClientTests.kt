package com.priyanshumaurya8868.realworld.io

import com.priyanshumaurya8868.realworld.io.api.MyConduitClient
import com.priyanshumaurya8868.realworld.io.api.model.entites.Article
import com.priyanshumaurya8868.realworld.io.api.model.entites.UserCreds
import com.priyanshumaurya8868.realworld.io.api.model.request.PublishArticleRequest
import com.priyanshumaurya8868.realworld.io.api.model.request.SignupUserRequest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random

class MyConduitClientTests {

    @Test
    fun `POST users - create user`() {
        val userCreds = UserCreds(
            email = "testemail${Random.nextInt(999, 9999)}@test.com",
            password = "pass${Random.nextInt(9999, 999999)}",
            username = "new_rand_user_${Random.nextInt(99, 999)}"
        )
        runBlocking {
            val resp = conduitClient.publicApi.signupUser(SignupUserRequest(userCreds))
            assertEquals(userCreds.username, resp.body()?.user?.username)
        }
    }


    private val conduitClient = MyConduitClient

    @Test
    fun `GET Articles`() {
        runBlocking {
            val articles = conduitClient.publicApi.getArticles()
            assertNotNull(articles.body()?.getArticles)
        }
    }

    @Test
    fun `GET Articles by Author`() {
        runBlocking {
            val articles = conduitClient.publicApi.getArticles(author = "444")
            assertNotNull(articles.body()?.getArticles)
        }
    }

    @Test
    fun `GET Articles by tag`() {
        runBlocking {
            val articles = conduitClient.publicApi.getArticles(tag = "dragon")
            assertNotNull(articles.body()?.getArticles)
        }
    }

    @Test
    fun uploadArticle(){

        val article = Article(
            title = "t1",
            body = "b1",
            description = "d1", tagList = null
        )
        runBlocking {
           val  article = conduitClient.authApi.
           publishArticle(PublishArticleRequest(article)).body()
          assertNotNull( article)
        }
    }
    @Test
    fun `test unfavourite article`(){
        runBlocking{
          val article =  MyConduitClient.authApi.unfavoriteArticle("abc-i8opti")
            assertNotNull(article)
        }
    }

    @Test
    fun `test favourite article`(){
        runBlocking{
            val article =  MyConduitClient.authApi.favoriteArticle("abc-i8opti")
            assertNotNull(article)
        }
    }

}