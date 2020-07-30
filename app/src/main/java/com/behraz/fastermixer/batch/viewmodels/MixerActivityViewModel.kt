package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MixerActivityViewModel: ViewModel() {

    val user get() = UserConfigs.user

    private var isGetMessageRequestActive = false

    val mixerLocation = MutableLiveData<CircleFence?>()

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
            getMessages()
            getMixerLocation(user.equipmentId!!) //get location from Car GPS
            println("timer: fuck")
        }
    }

    private fun getMixerLocation(equipmentId: String) {
        RemoteRepo.getEquipmentLocation(equipmentId) {
            if (it != null) {
                if (it.isSucceed) {
                    mixerLocation.value =
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