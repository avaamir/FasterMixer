package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.app.LocationCompassProvider
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.models.requests.behraz.GetVehicleLocationResponse
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.log
import com.behraz.fastermixer.batch.utils.general.now
import com.behraz.fastermixer.batch.utils.general.toJalali
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

abstract class VehicleActivityViewModel : ParentViewModel() {

    private val _isDamaged = MutableLiveData<Boolean>()
    val isDamaged: LiveData<Boolean> = _isDamaged

    var isServerLocationProvider: Boolean = false
        set(value) {
            if (value) {
                getUserLocationResponse.value = lastServerLocationResponse
            }
            field = value
        }

    @Volatile
    private var isGetUserLocationRequestActive = false

    @Volatile
    private var isGetMissionsRequestActive = false


    //TODO  get from GPS // curently it will receive from server from car GPS, In new Version Maybe Needed
    val getUserLocationResponse =
        MutableLiveData<GetVehicleLocationResponse?>() //we did not use Transformation because it is not always have a observer , But We Always have to update it's value for sorting mixers, the only observer is in map fragment

    private val getCurrentWeatherEvent = MutableLiveData<GeoPoint>()
    val currentWeather = Transformations.switchMap(getCurrentWeatherEvent) {
        RemoteRepo.getCurrentWeatherByCoordinates(it)
    }

    val newMissionEvent = MutableLiveData<Event<Mission>>()
    val getMissionError = MutableLiveData<Event<String>>()
    private val getMissionEvent = MutableLiveData(Event(Unit))
    private val getMissionResponse = Transformations.switchMap(getMissionEvent) {
        RemoteRepo.getMission(
            when (this) {
                is MixerActivityViewModel -> false
                is PompActivityViewModel -> true
                else -> throw IllegalStateException("this type not yet defined here")
            }
        ).map {
            isGetMissionsRequestActive = false
            it
        }
    }


    init {
        getMissionResponse.observeForever {
            if (it.isSucceed) {
                //Check if serverMission is a new Mission or Already submitted
                val serverMission = it.entity
                if (serverMission != null) { //NewMission
                    val currentMission = newMissionEvent.value?.peekContent()
                    if (serverMission.missionId != currentMission?.missionId) { //serverMission is a NewMission
                        newMissionEvent.value = Event(serverMission)
                    } else {
                        log("`newMissionId` and lastMissionId are same")
                        if(serverMission != currentMission) {
                            //TODO age content ha yeki nabud mission feli update behshe, msln benvise address avaz shod ya ..??
                        }
                    }
                } else { //NoMission
                    if (newMissionEvent.value?.peekContent() !== Mission.NoMission)
                        newMissionEvent.value = Event(Mission.NoMission)
                }
            } else {
                getMissionError.value = Event(it.message)
            }
        }

        LocationCompassProvider.location.observeForever { location ->
            if (!isServerLocationProvider) {
                //TODO age location va lastLocation kheli fasele dasht dg animate nashe va mostaghim bere un noghte
                getUserLocationResponse.value =
                    GetVehicleLocationResponse.create(location, _isDamaged.value ?: false)
            }
        }

    }

    private var lastServerLocationResponse: GetVehicleLocationResponse? = null

    protected open fun getUserLocation(equipmentId: Int) {
        if (!isGetUserLocationRequestActive) {
            isGetUserLocationRequestActive = true
            RemoteRepo.getEquipmentLocation(equipmentId) {
                isGetUserLocationRequestActive = false
                if (it != null) {
                    if (it.isSucceed) {
                        _isDamaged.value = it.entity?.isDamaged ?: false
                        lastServerLocationResponse = it.entity
                        if (isServerLocationProvider) {
                            getUserLocationResponse.value =
                                it.entity//age observer nadashte bashe set nemishe, age scenario avaz shod deghat kon, alan mapFragment Observersh hast
                        }
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


    fun getCurrentWeather() {
        getCurrentWeatherEvent.value = getUserLocationResponse.value!!.location
    }

    fun insertMission(mission: Mission): Message {
        val message = Message(
            id = mission.missionId,
            senderName = "ماموریت جدید",
            content = mission.summery,
            senderId = 0,
            eventName = "ماموریت جدید",
            dateTime = (mission.startMissionTime?.toJalali() ?: now().toJalali()).toString(),
            viewed = false,
            userId = UserConfigs.user.value?.id ?: 0
        )
        MessageRepo.insert(message)
        return message
    }
}
