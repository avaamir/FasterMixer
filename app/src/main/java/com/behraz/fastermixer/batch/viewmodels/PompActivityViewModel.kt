package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PompActivityViewModel : ParentViewModel() {

    @Volatile
    private var isGetPompMissionsRequestActive = false

    var shouldShowAllMixers = MutableLiveData<Boolean>(false)

    private var isGetCustomerRequestActive = false
    private var isGetMixerRequestActive = false


    //we did not use Transformation because it is not always have a observer , But We Always have to update it's value for sorting mixers List, the only observer is in map fragment
    val pompAreaInfo =
        MutableLiveData<CircleFence?>(null) //TODO implement this // get from GPS // curently it will receive from server from car GPS, In new Version Maybe Needed

    private val getPompEvent = MutableLiveData(Event(Unit))

    val allMixers = Transformations.switchMap(getPompEvent) {
        RemoteRepo.getAllMixers().map { response ->
            sortMixerResponse(response)
        }
    }

    val requestMixers = Transformations.switchMap(getPompEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = false).map { response ->
            isGetMixerRequestActive = false
            sortMixerResponse(response)
        }
    }

    private fun sortMixerResponse(response: Entity<List<Mixer>>?): Entity<List<Mixer>>? {
        val sortedMixers = pompAreaInfo.value?.let { pompLocation ->
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
        return if (sortedMixers != null)
            response?.copy(entity = sortedMixers)
        else
            response
    }


    val newMissionEvent = MutableLiveData<Event<Mission>>()
    val getMissionError = MutableLiveData<Event<String>>()
    private val getMixerMissionEvent = MutableLiveData(Event(Unit))
    private val getMissionResponse = Transformations.switchMap(getMixerMissionEvent) {
        RemoteRepo.getPompMission()
    }

    init {
        getMissionResponse.observeForever {
            println("debux: `newMission` Come")
            if (it != null) {
                if (it.isSucceed) {
                    //Check if serverMission is a new Mission or Already submitted
                    val serverMission = it.entity
                    if (serverMission != null) { //NewMission
                        val currentMission = newMissionEvent.value?.peekContent()
                        if (serverMission.missionId != currentMission?.missionId) { //serverMission is a NewMission
                            newMissionEvent.value = Event(serverMission)
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
                getMissionError.value = Event(Constants.SERVER_ERROR) //TODO?
            }
        }
    }

    private val getCustomerEvent = MutableLiveData(Event(Unit))
    val customers = Transformations.switchMap(getCustomerEvent) {
        RemoteRepo.getCustomers().map { response ->
            isGetCustomerRequestActive = false
            response
        }
    }


    override fun onTimerTick() {
        user.value?.let { user ->  //TODO albate bazam momkene unauthorized bede chun shyad moghe check kardan login bashe ama bad if logout etefagh biofte, AMA jelo exception ro migire
            refreshCustomers()
            refreshMixers()
            getPompLocation(user.equipmentId!!)
            getPompMission()
        }
    }

    private fun getPompMission() {
        if (!isGetPompMissionsRequestActive) {
            CoroutineScope(Dispatchers.Main).launch {
                getMixerMissionEvent.value = Event(Unit)
            }
        }
    }

    private fun getPompLocation(equipmentId: String) {
        RemoteRepo.getEquipmentLocation(equipmentId) {
            if (it != null) {
                if (it.isSucceed) {
                    //TODO az server vase pomp circle nemiyad khater hamin man mahduda ro ruye 100 gozashtam
                    pompAreaInfo.value =
                        it.entity!!.location.copy(radius = 100.0) //age observer nadashte bashe set nemishe, age scenario avaz shod deghat kon, alan mapFragment Observersh hast
                } else {
                    //TODO what should i do?
                }
            }
        }
    }

    fun refreshMixers() {
        if (!isGetMixerRequestActive) {
            isGetCustomerRequestActive = true
            getPompEvent.postValue(Event(Unit))
        }
    }

    fun refreshCustomers() {
        if (!isGetCustomerRequestActive) {
            isGetCustomerRequestActive = true
            getCustomerEvent.postValue(Event(Unit))
        }
    }
}