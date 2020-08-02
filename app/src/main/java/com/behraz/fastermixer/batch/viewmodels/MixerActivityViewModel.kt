package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMessages
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.launchApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class MixerActivityViewModel : ViewModel() {

    val user get() = UserConfigs.user

    @Volatile
    private var isGetMessageRequestActive = false

    @Volatile
    private var isGetMixerLocationRequestActive = false

    @Volatile
    private var isGetMixerMissionsRequestActive = false

    val mixerLocation = MutableLiveData<CircleFence?>()


    private val getMixerMissionEvent = MutableLiveData(Event(Unit))
    val mixerMission = Transformations.switchMap(getMixerMissionEvent) {
        isGetMessageRequestActive = true
        RemoteRepo.getMixerMission().map {
            isGetMixerMissionsRequestActive = false
            it
        }
    }


    private val getMessageEvent = MutableLiveData<Event<Unit>>()
    val messages = Transformations.switchMap(getMessageEvent) {
        RemoteRepo.getMessages().map {
            isGetMessageRequestActive = false
            it?.entity?.let { _messages ->
                if (_messages.isNotEmpty()) {
                    newMessage.value = _messages[0]
                    //TODO  RemoteRepo.seenMessage(_messages[0].id)
                }
            }
            it
        }
    }

    val newMessage = MutableLiveData<Message>()

    private val logOutEvent = MutableLiveData<Event<Unit>>()
    val logoutResponse = Transformations.switchMap(logOutEvent) {
        RemoteRepo.logout()
    }

    private val timer = fixedRateTimer(period = 10000L) {
        user.value?.let { user ->  //TODO albate bazam momkene unauthorized bede chun shyad moghe check kardan login bashe ama bad if logout etefagh biofte, AMA jelo exception ro migire
            getMessages()
            getMixerLocation(user.equipmentId!!) //get location from Car GPS
            getMixerMission()
        }
    }

    private fun getMixerLocation(equipmentId: String) {
        if (!isGetMixerLocationRequestActive) {
            isGetMessageRequestActive = true
            RemoteRepo.getEquipmentLocation(equipmentId) {
                isGetMessageRequestActive = false
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
    }

    private fun getMixerMission() {
        if (!isGetMixerMissionsRequestActive) {
            CoroutineScope(Main).launch {
                getMixerMissionEvent.value = Event(Unit)
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