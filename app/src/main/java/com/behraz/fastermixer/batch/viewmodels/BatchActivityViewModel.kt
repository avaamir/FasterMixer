package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event
import org.osmdroid.util.GeoPoint
import kotlin.concurrent.fixedRateTimer

class BatchActivityViewModel : ViewModel() {

    private var batchLocation: GeoPoint? = null //TODO implement this // get from server

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val mixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getBatchMixers().map { response ->
            val sortedMixers = batchLocation?.let { batchLocation ->
                response?.entity?.sortedWith(
                    compareBy { mixer ->
                        mixer.latLng.distanceToAsDouble(batchLocation).also { distance ->
                            mixer.state = distanceTextNormalizer(distance)
                        }
                    }
                )
            }
            if (sortedMixers != null)
                response?.copy(entity = sortedMixers)
            else
                response
        }
    }


    private var isGetMessageRequestActive = false
    private val getMessageEvent = MutableLiveData<Event<Unit>>()
    val messages = Transformations.switchMap(getMessageEvent) {
        RemoteRepo.getMessages().map {
            isGetMessageRequestActive = false
            it
        }
    }
    val user get() = UserConfigs.user

    private val logOutEvent = MutableLiveData<Event<Unit>>()
    val logoutResponse = Transformations.switchMap(logOutEvent) {
        RemoteRepo.logout()
    }


    private val timer = fixedRateTimer(period = 10000L) {
        getMixersEvent.postValue(Event(Unit))
        getMessageEvent.postValue(Event(Unit))
    }

    init {
        println("debug:" + UserConfigs.user.value)
        RemoteRepo.getEquipmentLocation(UserConfigs.user.value!!.equipmentId!!) {
            batchLocation = it
        }
    }


    fun getMessages() {
        if (!isGetMessageRequestActive) {
            isGetMessageRequestActive = true
            getMessageEvent.postValue(Event(Unit))
        }
    }

    fun refreshMixers() {
        getMixersEvent.value = Event(Unit)
    }


    fun logout() {
        logOutEvent.value = Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        timer.purge()
    }




    fun distanceTextNormalizer(meterDistance: Double): String {
        val temp = meterDistance.toInt() / 100
        return when {
            meterDistance < 20 -> "به بچ رسید"
            temp == 0 || temp / 10 == 0 -> "${meterDistance.toInt()} متر" //meter
            temp % 10 == 0 -> "${temp / 10} کیلومتر" //km
            else -> "${temp / 10}.${temp % 10} کیلومتر" //km
        }.let {
            if (it.contains("رسید"))
                it
            else
                "$it مانده"
        }
    }

}