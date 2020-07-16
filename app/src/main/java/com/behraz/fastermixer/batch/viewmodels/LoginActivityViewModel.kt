package com.behraz.fastermixer.batch.viewmodels

import android.util.EventLog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.general.Event

class LoginActivityViewModel: ViewModel() {

    private val loginRequest = MutableLiveData<LoginRequest>()
    val loginResponse = Transformations.switchMap(loginRequest) {
       RemoteRepo.login(it).map { entity ->
           Event(entity)
       }
    }


    fun login(username: String, password: String) {
        loginRequest.value = LoginRequest(username, password)
    }

    fun tryAgain() {
        loginRequest.value = loginRequest.value
    }

}