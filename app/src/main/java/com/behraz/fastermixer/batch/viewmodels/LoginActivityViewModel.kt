package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class LoginActivityViewModel: ViewModel() {

    private val loginRequest = MutableLiveData<LoginRequest>()
    val loginResponse = Transformations.switchMap(loginRequest) {
        RemoteRepo.login(it)
    }


    fun login(username: String, password: String) {
        loginRequest.value = LoginRequest(username, password)
    }

    fun tryAgain() {
        loginRequest.value = loginRequest.value
    }

}