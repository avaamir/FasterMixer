package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event

class BatchActivityViewModel : ParentViewModel() {

    private var batchFenceLocation: Fence? = null

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val mixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = true).map { response ->
            val sortedMixers = batchFenceLocation?.let { fence ->
                response?.entity?.let {
                    if (it.size == 1) {
                        it[0].normalizeStateByDistance(fence)
                        it
                    } else {
                        it.sortedWith(
                            compareBy { mixer ->
                                mixer.location.distanceToAsDouble(fence.center)
                                    .also {
                                        mixer.normalizeStateByDistance(fence)
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

    override fun onTimerTick() {
        refreshMixers()
    }

    init {
        UserConfigs.user.value!!.let { user -> //TODO get this location again if not receiver from server
            RemoteRepo.getBatchLocation(user.equipmentId!!) {
                batchFenceLocation =
                    it
            }
        }
    }

    fun refreshMixers() {
        getMixersEvent.postValue(Event(Unit))
    }

}