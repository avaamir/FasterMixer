package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.models.requests.behraz.UpdateResponse
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.general.Event

class LoginActivityViewModel : ViewModel() {

    private var isCheckedForUpdatesRequestActive = false

    private var updateEvent: Event<Entity<UpdateResponse>> =
        Event(Entity(UpdateResponse.NoResponse, false, null))

    private val updateRequestEvent = MutableLiveData<Event<Unit>>()
    val checkUpdateResponse = Transformations.switchMap(updateRequestEvent) {
        isCheckedForUpdatesRequestActive = true
        RemoteRepo.checkUpdates().map {
            if (it?.isSucceed == false) {
                isCheckedForUpdatesRequestActive = false
            }

            if (it != null) {
                if (!updateEvent.peekContent().isSucceed) {
                    updateEvent = Event(it)
                }
            }
            updateEvent
        }
    }

    private val loginRequest = MutableLiveData<LoginRequest>()
    val loginResponse = Transformations.switchMap(loginRequest) { request ->
        RemoteRepo.login(request).map { entity ->
            //TODO if account isDemo (felan static code zade shode badan bayad az samte server dorost beshe) ->
            if (entity?.isSucceed == true) {
                FasterMixerApplication.isDemo = request.factoryCode == "100100"
            }
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