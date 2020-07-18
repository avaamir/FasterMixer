package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.*
import com.behraz.fastermixer.batch.models.Customer
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMixers
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.distanceTextNormalizer
import org.osmdroid.util.GeoPoint
import kotlin.concurrent.fixedRateTimer

class PompActivityViewModel : ViewModel() {

    val user get() = UserConfigs.user

    private var isGetMessageRequestActive = false
    private var isGetCustomerRequestActive = false
    private var isGetMixerRequestActive = false


    private var pompLocation: GeoPoint? = null //TODO implement this // get from GPS

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val mixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = false).map { response ->
            isGetMixerRequestActive = false
            val sortedMixers = pompLocation?.let { pompLocation ->
                response?.entity?.sortedWith(
                    compareBy { mixer ->
                        mixer.latLng.distanceToAsDouble(pompLocation).also { distance ->
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

    private val getCustomerEvent = MutableLiveData(Event(Unit))
    val customers = Transformations.switchMap(getCustomerEvent) {
        RemoteRepo.getCustomers().map { response ->
            isGetCustomerRequestActive = false
            response
        }
    }


    private val getMessageEvent = MutableLiveData<Event<Unit>>()
    val messages = Transformations.switchMap(getMessageEvent) {
        RemoteRepo.getMessages().map {
            isGetMessageRequestActive = false
            it
        }
    }

    private val logOutEvent = MutableLiveData<Event<Unit>>()
    val logoutResponse = Transformations.switchMap(logOutEvent) {
        RemoteRepo.logout()
    }

    private val timer = fixedRateTimer(period = 10000L) {
        refreshCustomers()
        refreshMixers()
        getMessages()
    }


    fun logout() {
        logOutEvent.value = Event(Unit)
    }

    fun refreshMixers() {
        if (!isGetMixerRequestActive) {
            isGetCustomerRequestActive = true
            getMixersEvent.postValue(Event(Unit))
        }
    }

    fun refreshCustomers() {
        if (!isGetCustomerRequestActive) {
            isGetCustomerRequestActive = true
            getCustomerEvent.postValue(Event(Unit))
        }
    }

    fun getMessages() {
        if (!isGetMessageRequestActive) {
            isGetMessageRequestActive = true
            getMessageEvent.postValue(Event(Unit))
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        timer.purge()
    }

}