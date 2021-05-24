package com.priyanshumaurya8868.realworld.io.conduit.ui.feeds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetArticle
import com.priyanshumaurya8868.realworld.io.conduit.Repo.ArticleRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {
    private val _feed = MutableLiveData<List<GetArticle>>()
    val feed: LiveData<List<GetArticle>> = _feed

    fun fetchGlobalFeed() = viewModelScope.launch(Dispatchers.IO) {

        ArticleRepo.getGlobalFeeds().body()?.let {
            _feed.postValue(it.getArticles)
            Log.d("FEED", "article count" + it.articlesCount)
        }
    }

    fun fetchMyFeed() = viewModelScope.launch(Dispatchers.IO) {
        ArticleRepo.getMyFeeds().body()?.let {
            _feed.postValue(it.getArticles)
            Log.d("FEED", "My feed article count" + it.articlesCount)
        }
    }

    fun favouriteArticle(slug: String) = viewModelScope.launch {
        Log.d("FEED","favourite called")
        ArticleRepo.favouriteArticle(slug)
    }
    fun unfavouriteArticle(slug: String) = viewModelScope.launch {
        ArticleRepo.unfavouriteArticle(slug)
        Log.d("FEED","unfavourite called")
    }


}//TODO : implement wrapper Class for loaders
