package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.Customer
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.general.Event

class PompActivityViewModel : VehicleActivityViewModel() {

    @Volatile
    private var isGetCustomerRequestActive = false

    @Volatile
    private var isGetMixerRequestActive = false

    val shouldShowAllMixers = MutableLiveData<Boolean>(false)

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

    private fun sortMixerResponse(response: Entity<List<Mixer>>?): Entity<List<Mixer>>? {
        val sortedMixers = getUserLocationResponse.value?.let { pompLocation ->
            response?.entity?.let { mixers ->
                if (mixers.size == 1) {
                    if (mixers[0].state != "تخلیه") mixers[0].normalizeStateByDistance(pompLocation.circleFence)
                    mixers
                } else
                    mixers.sortedWith(
                        compareBy { mixer ->
                            mixer.location.distanceToAsDouble(pompLocation.circleFence.center)
                                .also {
                                    mixer.normalizeStateByDistance(
                                        pompLocation.circleFence
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
    private val getCustomerEvent = MutableLiveData(true) //if true reqServer else selectedProject Changed
    private var lastGetCustomerResponse: Entity<List<Customer>>? = null

    val customers = Transformations.switchMap(getCustomerEvent) { event ->
        println("debugx: getCustomerEvent called")
        if (event) {
            RemoteRepo.getCustomers().map { response ->
                println("debugx: getCustomerResponse came")
                isGetCustomerRequestActive = false
                response?.entity?.let {
                    if (selectedProjectId == null) {
                        selectedProjectId = it[0].id
                        it[0].isSelected = true
                        lastGetCustomerResponse = response
                    } else {
                        val selectedProject =
                            it.find { customer -> customer.id == selectedProjectId }
                        selectedProject?.isSelected = true
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
}