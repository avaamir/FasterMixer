package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Plan
import com.behraz.fastermixer.batch.respository.RemoteRepo
import java.util.*
import kotlin.concurrent.fixedRateTimer

class ServiceFragmentViewModel : ViewModel() {

    private val getServiceEvent = MutableLiveData<Int>()
    val activeServices = Transformations.switchMap(getServiceEvent) { reqId ->
        RemoteRepo.getActiveServices(reqId)
    }


    private var timer: Timer? = null

    var plan: Plan? = null
        set(value) {
            field = value
            if (timer == null) {
                timer = fixedRateTimer(period = 20000L) {
                    getServices()
                }
            }
        }


    fun getServices() {
        getServiceEvent.postValue( plan!!.id)
    }
}