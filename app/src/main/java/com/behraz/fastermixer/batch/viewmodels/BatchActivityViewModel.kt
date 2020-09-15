package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.general.Event
import org.osmdroid.util.GeoPoint
import kotlin.concurrent.fixedRateTimer

class BatchActivityViewModel : ViewModel() {

    private var batchLocation: CircleFence? = null

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val mixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = true).map { response ->
            val sortedMixers = batchLocation?.let { batchLocation ->
                response?.entity?.let {
                    if (it.size == 1) {
                        it[0].normalizeStateByDistance(batchLocation)
                        it
                    } else {
                        it.sortedWith(
                            compareBy { mixer ->
                                mixer.latLng.distanceToAsDouble(batchLocation.center)
                                    .also { distance ->
                                        mixer.normalizeStateByDistance(
                                            distance,
                                            batchLocation.radius
                                        )
                                    }
                            }
                        )
                    }
                }
            }
            if (sortedMixers != null)
                response?.copy(entity = sortedMixers)
            else
                response
        }
    }


    private var isGetMessageRequestActive = false
    val messages = MessageRepo.allMessage
    val user get() = UserConfigs.user

    private val logOutEvent = MutableLiveData<Event<Unit>>()
    val logoutResponse = Transformations.switchMap(logOutEvent) {
        RemoteRepo.logout()
    }


    private val timer = fixedRateTimer(period = 10000L) {
        if (UserConfigs.isLoggedIn) { //TODO albate bazam momkene unauthorized bede chun shyad moghe check kardan login bashe ama bad if logout etefagh biofte
            refreshMixers()
            getMessages()
        }
    }

    init {
        UserConfigs.user.value!!.let { user -> //TODO get this location again if not receiver from server
            RemoteRepo.getBatchLocation(user.equipmentId!!) {
                batchLocation = it
            }
        }
    }


    private fun getMessages() {
        if (!isGetMessageRequestActive) {
            isGetMessageRequestActive = true
            RemoteRepo.getMessage {
                isGetMessageRequestActive = false
                if (it != null) { //halat hayee ke khata vojud darad mohem ast, data az MessageRepo khande mishavad
                    if (!it.isSucceed) {
                        //TODO ???
                    }
                } else {
                    //TODO ???
                }
            }
        }
    }

    fun refreshMixers() {
        getMixersEvent.postValue(Event(Unit))
    }


    fun logout() {
        logOutEvent.value = Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        timer.purge()
    }

}