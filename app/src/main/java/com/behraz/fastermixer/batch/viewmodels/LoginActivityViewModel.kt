package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.general.Event

class LoginActivityViewModel : ViewModel() {

    private var isCheckedForUpdatesRequestActive = false

    private val updateRequestEvent = MutableLiveData<Event<Unit>>()
    val checkUpdateResponse = Transformations.switchMap(updateRequestEvent) {
        isCheckedForUpdatesRequestActive = true
        RemoteRepo.checkUpdates().map {
            isCheckedForUpdatesRequestActive = false
            it
        }
    }

    private val loginRequest = MutableLiveData<LoginRequest>()
    val loginResponse = Transformations.switchMap(loginRequest) {
        RemoteRepo.login(it).map { entity ->
            Event(entity)
        }
    }


    fun login(username: String, password: String, factoryCode: String) {
        loginRequest.value = LoginRequest(username, password, factoryCode)
    }

    fun tryAgain() {
        loginRequest.value = loginRequest.value
    }

    fun checkUpdates() {
        updateRequestEvent.value = Event(Unit)
    }

}