package com.priyanshumaurya8868.realworld.io.conduit.ui.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.realworld.io.api.model.entites.Article
import com.priyanshumaurya8868.realworld.io.api.model.entites.Comment
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetArticle
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetComment
import com.priyanshumaurya8868.realworld.io.api.model.request.PublishArticleRequest
import com.priyanshumaurya8868.realworld.io.conduit.Repo.ArticleRepo
import com.priyanshumaurya8868.realworld.io.conduit.Repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val _article = MutableLiveData<GetArticle>()
    val getArticle: LiveData<GetArticle> = _article

    private val _cmnts = MutableLiveData<List<GetComment>?>()
    val cmnts: LiveData<List<GetComment>?> = _cmnts


    fun getArticleBySlug(slug: String) = viewModelScope.async(Dispatchers.IO) {
        val response = UserRepo.getArticleBySlug(slug)
        _article.postValue(response.body()?.getArticle)
    }

   suspend fun publishedArticle(article : Article) = ArticleRepo.publishArticle(PublishArticleRequest(article)).code()

    fun getComments(slug: String)= viewModelScope.launch(Dispatchers.IO) {
        val  response = ArticleRepo.getComments(slug)
        _cmnts.postValue(response.body()?.getComments)
        Log.d("comment" ,"getComments called ${response.body()?.getComments?.size}")
    }

    fun postComment(slug: String, comment: Comment) = viewModelScope.launch(Dispatchers.IO) {
        ArticleRepo.postComments(slug,comment)
        getComments(slug)
        //TODO :  added received response in the existing list instead of calling getComents(slug)
    }

    fun favouriteArticle(slug: String) = viewModelScope.launch {
        Log.d("FEED","favourite called")
        ArticleRepo.favouriteArticle(slug)
    }
    fun unfavouriteArticle(slug: String) = viewModelScope.launch {
        ArticleRepo.unfavouriteArticle(slug)
        Log.d("FEED","unfavourite called")
    }

    fun updateArticle(oldArticleSlug: String,newArticle: Article ) = viewModelScope.launch {
        val  response =ArticleRepo.updateArticle( oldArticleSlug,newArticle)
        if (response.isSuccessful)
        _article.postValue(response.body()?.getArticle)
    }

    fun deleteArticle(slug: String) =viewModelScope.launch {
        ArticleRepo.deleteArticle(slug)
    }

    fun deleteComment(slug: String,id: Int) = viewModelScope.launch {
        ArticleRepo.deleteComment(slug, id)
    }

}