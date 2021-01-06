package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Plan
import com.behraz.fastermixer.batch.respository.RemoteRepo


class ServiceHistoryFragmentViewModel: ViewModel() {
    private var vehicleId: Int = 0
    var plan: Plan? = null
        private set

    private val getServiceEvent = MutableLiveData<Pair<Int, Plan>>()
    val serviceHistory = Transformations.switchMap(getServiceEvent) {
        val vehicleId = it.first
        val reqId = it.second.id
        RemoteRepo.getServiceHistory(vehicleId, reqId)
    }


    fun setData(vehicleId: Int, plan: Plan) {
        this.vehicleId = vehicleId
        this.plan = plan
        getServices()
    }

    fun getServices() {
        getServiceEvent.value = Pair(vehicleId, plan!!)
    }




}