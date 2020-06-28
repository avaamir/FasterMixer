package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.ChooseBatchRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event

class ChooseBatchActivityViewModel : ViewModel() {

    val user get() = UserConfigs.user

    private val chooseBatchRequest = MutableLiveData<ChooseBatchRequest>()
    val chooseBatchResponse = Transformations.switchMap(chooseBatchRequest) {
        RemoteRepo.chooseBatch(it)
    }

    private val getBatchEvent = MutableLiveData<Event<Unit>>()
    val batches = Transformations.switchMap(getBatchEvent) {
        RemoteRepo.getBatches()
    }

    fun getBatches() {
        getBatchEvent.value = Event(Unit)
    }

    fun logout() {
        UserConfigs.logout()
    }


    fun chooseBatch(batchId: String) {
        this.chooseBatchRequest.value = ChooseBatchRequest(batchId)
    }
}