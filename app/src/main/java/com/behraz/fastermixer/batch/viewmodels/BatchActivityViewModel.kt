package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.models.normalizeStateByDistance
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.log

class BatchActivityViewModel : ParentViewModel() {

    private var batchFenceLocation: Fence? = null

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val mixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = true).map { response ->
            log("getMixerResponse:$response, Fence:${batchFenceLocation}")
            val sortedMixers = batchFenceLocation?.let { fence ->
                response?.entity?.let {
                    if (it.size == 1) {
                        it[0].state = it[0].normalizeStateByDistance(fence)
                        it
                    } else {
                        it.sortedWith(
                            compareBy { mixer ->
                                mixer.location.distanceToAsDouble(fence.center)
                                    .also {
                                        mixer.state = mixer.normalizeStateByDistance(fence)
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

    override fun onTimerTick(user: User) {
        refreshMixers()
        if (batchFenceLocation == null) {
            getBatchFenceLocation(user)
        }
    }

    private fun getBatchFenceLocation(user: User) {
        RemoteRepo.getBatchLocation(user.equipmentId!!) {
            log("getBatchFenceLocation:$it")
            batchFenceLocation = it
        }
    }

    fun refreshMixers() {
        getMixersEvent?.postValue(Event(Unit)) //chun timer dar kelas super call mishe inja hanuz init nashode khater hamin not null budan check mishe
    }

}