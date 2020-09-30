package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class MixerActivityViewModel : ParentViewModel() {

    @Volatile
    private var isGetMixerLocationRequestActive = false

    @Volatile
    private var isGetMixerMissionsRequestActive = false

    val mixerLocation = MutableLiveData<CircleFence?>()

    val newMissionEvent = MutableLiveData<Event<Mission>>()
    val getMissionError = MutableLiveData<Event<String>>()
    private val getMixerMissionEvent = MutableLiveData(Event(Unit))
    private val getMissionResponse = Transformations.switchMap(getMixerMissionEvent) {
        RemoteRepo.getMixerMission().map {
            isGetMixerMissionsRequestActive = false
            it
        }
    }

    init {
        getMissionResponse.observeForever {
            println("debux:(MixerActivityViewModel-getMissionResponseForeverObserver) getMissionResponse =====================================")
            println("debux: `newMission` Come from server")
            if (it != null) {
                if (it.isSucceed) {
                    //Check if serverMission is a new Mission or Already submitted
                    val serverMission = it.entity
                    println("debux: `newMissionContent` -> ${it.entity}")
                    if (serverMission != null) { //NewMission
                        val currentMission = newMissionEvent.value?.peekContent()
                        if (serverMission.missionId != currentMission?.missionId) { //serverMission is a NewMission
                            println("debux: `newMissionEvent happened ")
                            newMissionEvent.value = Event(serverMission)
                        } else {
                            println("debux: `newMissionId` and lastMissionId are same")
                        }
                    } else { //NoMission
                        if (newMissionEvent.value?.peekContent() !== Mission.NoMission)
                            newMissionEvent.value = Event(Mission.NoMission)
                    }
                } else {
                    getMissionError.value = Event(it.message)  //TODO?
                }
            } else {
                //TODO check this code , request again to map.ir
                println("debux: `newMission` SERVER ERROR ")
                getMissionError.value = Event(Constants.SERVER_ERROR) //TODO?
            }
        }
    }


    override fun onTimerTick() {
        user.value?.let { user ->  //TODO albate bazam momkene unauthorized bede chun shyad moghe check kardan login bashe ama bad if logout etefagh biofte, AMA jelo exception ro migire
            getMixerLocation(user.equipmentId!!) //get location from Car GPS
            getMixerMission()
        }
    }


    private fun getMixerLocation(equipmentId: String) {
        if (!isGetMixerLocationRequestActive) {
            isGetMixerLocationRequestActive = true
            RemoteRepo.getEquipmentLocation(equipmentId) {
                isGetMixerLocationRequestActive = false
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
            isGetMixerMissionsRequestActive = true
            CoroutineScope(Main).launch {
                getMixerMissionEvent.value = Event(Unit)
            }
        }
    }

}