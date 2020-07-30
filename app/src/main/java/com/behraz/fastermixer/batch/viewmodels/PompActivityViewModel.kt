package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.*
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event
import kotlin.concurrent.fixedRateTimer

class PompActivityViewModel : ViewModel() {


    val user get() = UserConfigs.user

    private var isGetMessageRequestActive = false
    private var isGetCustomerRequestActive = false
    private var isGetMixerRequestActive = false


    val pompArea = MutableLiveData<CircleFence?>(null) //TODO implement this // get from GPS // curently it will receive from server from car GPS, In new Version Maybe Needed

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val mixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = false).map { response ->
            isGetMixerRequestActive = false
            val sortedMixers = pompArea.value?.let { pompLocation ->
                response?.entity?.let {
                    if (it.size == 1) {
                        if (it[0].state != "تخلیه") it[0].normalizeStateByDistance(pompLocation)
                        it
                    } else
                        it.sortedWith(
                            compareBy { mixer ->
                                mixer.latLng.distanceToAsDouble(pompLocation.center)
                                    .also { distance ->
                                        mixer.normalizeStateByDistance(
                                            distance,
                                            pompLocation.radius
                                        )
                                    }
                            }
                        )
                }
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
        user.value?.let { user ->  //TODO albate bazam momkene unauthorized bede chun shyad moghe check kardan login bashe ama bad if logout etefagh biofte, AMA jelo exception ro migire
            refreshCustomers()
            refreshMixers()
            getMessages()
            getPompLocation(user.equipmentId!!)
        }
    }


    private fun getPompLocation(equipmentId: String) {
        RemoteRepo.getPompLocation(equipmentId) {
            if (it != null) {
                if (it.isSucceed) {
                    pompArea.value =
                        it.entity!!.location //age observer nadashte bashe set nemishe, age scenario avaz shod deghat kon, alan mapFragment Observersh hast
                } else {
                    //TODO what should i do?
                }
            }
        }
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