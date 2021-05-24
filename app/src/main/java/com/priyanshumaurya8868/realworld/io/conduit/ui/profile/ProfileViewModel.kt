package com.priyanshumaurya8868.realworld.io.conduit.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.realworld.io.api.MyConduitClient
import com.priyanshumaurya8868.realworld.io.api.model.entites.GetArticle
import com.priyanshumaurya8868.realworld.io.api.model.entites.Profile
import com.priyanshumaurya8868.realworld.io.api.model.entites.User
import com.priyanshumaurya8868.realworld.io.conduit.Repo.ArticleRepo
import com.priyanshumaurya8868.realworld.io.conduit.Repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel(){
   private val _myProfile = MutableLiveData<User?>()
           val myProfile : LiveData<User?> = _myProfile
    init {
        getMyProfile()
    }
    private val  _user = MutableLiveData<Profile>()
    val user : LiveData<Profile> = _user

    private val  _authorArticles = MutableLiveData<List<GetArticle>>()
    val authorArticles : LiveData<List<GetArticle>> = _authorArticles

    private val  _favArticles = MutableLiveData<List<GetArticle>>()
    val favArticles : LiveData<List<GetArticle>> = _favArticles

    fun  getUser(username : String) = viewModelScope.launch {
       val response =  UserRepo.getProfile(username)
        _user.postValue(response.body()?.profile)
    }
    fun getMyArticles(
        author: String? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        val  response = ArticleRepo.getArticles(author = author)
        _authorArticles.postValue(response.body()?.getArticles)

    }
    fun getFavArticles(author: String? = null)=viewModelScope.launch(Dispatchers.IO){
        val  response2 = ArticleRepo.getArticles(favourited = author)
        _favArticles.postValue(response2.body()?.getArticles)
    }
    fun followProfile(username : String) = viewModelScope.launch {
        UserRepo.followProfile(username)
        Log.d("Profile", "follow req sent")
    }
    fun unfollowProfile(username : String) = viewModelScope.launch {
        UserRepo.unFollowProfile(username)
        Log.d("Profile", "unfollow req sent")
    }


    fun getMyProfile() = viewModelScope.launch {
            val response = UserRepo.getMyProfile()
            _myProfile.postValue(response.body()?.user)
    }
   fun removeProfile() {
       _myProfile.postValue(null)
   }

}