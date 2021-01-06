package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Plan
import com.behraz.fastermixer.batch.respository.RemoteRepo

class ServiceFragmentViewModel : ViewModel() {

    private val getServiceEvent = MutableLiveData<Int>()
    val activeServices = Transformations.switchMap(getServiceEvent) { reqId ->
        RemoteRepo.getActiveServices(reqId)
    }

    var plan: Plan? = null
        set(value) {
            field = value
            getServices()
        }


    fun getServices() {
        getServiceEvent.value = plan!!.id
    }
}