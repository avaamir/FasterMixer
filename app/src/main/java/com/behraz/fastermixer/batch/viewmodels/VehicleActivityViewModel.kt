package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.models.requests.behraz.GetVehicleLocationResponse
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class VehicleActivityViewModel : ParentViewModel() {

    @Volatile
    private var isGetUserLocationRequestActive = false

    @Volatile
    private var isGetMissionsRequestActive = false


    //TODO  get from GPS // curently it will receive from server from car GPS, In new Version Maybe Needed
    val userLocation = MutableLiveData<GetVehicleLocationResponse?>() //we did not use Transformation because it is not always have a observer , But We Always have to update it's value for sorting mixers List, the only observer is in map fragment


    val newMissionEvent = MutableLiveData<Event<Mission>>()
    val getMissionError = MutableLiveData<Event<String>>()
    private val getMissionEvent = MutableLiveData(Event(Unit))
    private val getMissionResponse = Transformations.switchMap(getMissionEvent) {
        RemoteRepo.getMixerMission().map {
            isGetMissionsRequestActive = false
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


    protected open fun getUserLocation(equipmentId: String) {
        if (!isGetUserLocationRequestActive) {
            isGetUserLocationRequestActive = true
            RemoteRepo.getEquipmentLocation(equipmentId) {
                isGetUserLocationRequestActive = false
                if (it != null) {
                    if (it.isSucceed) {
                        userLocation.value =
                            it.entity //age observer nadashte bashe set nemishe, age scenario avaz shod deghat kon, alan mapFragment Observersh hast
                    } else {
                        //TODO what should i do?
                    }
                }
            }
        }
    }

    protected open fun getMission() {
        if (!isGetMissionsRequestActive) {
            isGetMissionsRequestActive = true
            CoroutineScope(Dispatchers.Main).launch {
                getMissionEvent.value = Event(Unit)
            }
        }
    }

}