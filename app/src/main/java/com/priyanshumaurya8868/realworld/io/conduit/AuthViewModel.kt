package com.priyanshumaurya8868.realworld.io.conduit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.realworld.io.api.model.entites.User
import com.priyanshumaurya8868.realworld.io.conduit.Repo.UserRepo
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private var oldUser : User? = null
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun login(email: String, password: String) = viewModelScope.launch {
        UserRepo.userLogin(email, password)?.let {
            _user.postValue(it)
        }
    }

    fun signup(username: String, email: String, password: String) = viewModelScope.launch {
        UserRepo.userSignin(username, email, password)?.let {
            _user.postValue(it)
        }
    }

    fun autoSignin(token: String) = viewModelScope.launch {
        UserRepo.getCurrentUserProfile(token)?.let {
            _user.postValue(it)
        }
    }


    fun updateUser(
        image: String?,
        username: String?,
        bio: String?,
        email: String?,
        password: String?
    ) = viewModelScope.launch {
        UserRepo.updateUser(image, username, bio, email, password)?.let { newUser->
            if(oldUser.toString() != newUser.toString()){
                oldUser = newUser
                _user.postValue(oldUser)
            }
        }
    }

    fun logOut() {
        _user.postValue(null)
    }


}