package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.now
import com.behraz.fastermixer.batch.utils.general.toJalali

class PompActivityViewModel : VehicleActivityViewModel() {

    @Volatile
    private var isGetCustomerRequestActive = false

    @Volatile
    private var isGetMixerRequestActive = false

    val shouldShowAllMixers = MutableLiveData(false)

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val allMixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getAllMixers().map { response ->
            sortMixerResponse(response)
        }
    }

    val requestMixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = false).map { response ->
            isGetMixerRequestActive = false
            sortMixerResponse(response)
        }
    }

    private fun sortMixerResponse(response: ApiResult<List<Mixer>>?): ApiResult<List<Mixer>>? {
        val sortedMixers = getUserLocationResponse.value?.let { pompLocation ->
            response?.entity?.let { mixers ->
                if (mixers.size == 1) {
                    mixers[0].state =
                        mixers[0].normalizeStateByDistance(CircleFence(pompLocation.location, 10.0))
                    mixers
                } else
                    mixers.sortedWith(
                        compareBy { mixer ->
                            mixer.location.distanceToAsDouble(pompLocation.location)
                                .also {
                                    mixer.state = mixer.normalizeStateByDistance(
                                        CircleFence(pompLocation.location, 10.0)
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

    private var selectedProjectId: String? = null
    private val getCustomerEvent =
        MutableLiveData(true) //if true reqServer else selectedProject Changed
    private var lastGetCustomerResponse: ApiResult<List<Customer>>? = null

    val customers = Transformations.switchMap(getCustomerEvent) { event ->
        println("debugx: getCustomerEvent called")
        if (event) {
            RemoteRepo.getCustomers().map { response ->
                println("debugx: getCustomerResponse came")
                isGetCustomerRequestActive = false

                if (response.isSucceed) {
                    val customers = response.entity!!
                    if (customers.isNotEmpty()) {
                        if (selectedProjectId == null) {
                            selectedProjectId = customers[0].id
                            customers[0].isSelected = true
                            lastGetCustomerResponse = response
                        } else {
                            val selectedProject =
                                customers.find { customer -> customer.id == selectedProjectId }
                            selectedProject?.isSelected = true
                        }
                    }
                }
                response
            }
        } else {
            lastGetCustomerResponse?.entity?.find { customer -> customer.id == selectedProjectId }?.isSelected =
                true
            MutableLiveData(lastGetCustomerResponse)
        }
    }

    override fun onTimerTick(user: User) {
        super.onTimerTick(user)
        refreshCustomers()
        refreshMixers()
        getUserLocation(user.equipmentId!!)
        getMission()
    }

    private fun refreshMixers() {
        if (!isGetMixerRequestActive) {
            isGetCustomerRequestActive = true
            getMixersEvent?.postValue(Event(Unit)) //chun timer dar kelas super call mishe inja hanuz init nashode khater hamin not null budan check mishe
        }
    }

    private fun refreshCustomers() {
        if (!isGetCustomerRequestActive) {
            isGetCustomerRequestActive = true
            getCustomerEvent?.postValue(true) //chun timer dar kelas super call mishe inja hanuz init nashode khater hamin not null budan check mishe
        }
    }

    fun selectProject(customer: Customer) {
        if (selectedProjectId != customer.id) {
            selectedProjectId = customer.id
            getCustomerEvent.postValue(false)
            /*todo geofence avaz shavad, masir yabi avaz shavad*/
            /*todo azesh beporse ke proje tamum nashode motmaeni*/
            /*todo refresh customer list for recoloring selectedProject*/
        }
    }

    fun insertMessage(message: String): Message = Message(
        id = now().time.toString(),
        senderName = "پروژه جدید",
        content = message,
        senderId = 0,
        eventName = "پروژه جدید",
        dateTime = now().toJalali().toString(),
        viewed = false,
        userId = UserConfigs.user.value?.id ?: 0
    ).let {
        MessageRepo.insert(it)
        return it
    }

}