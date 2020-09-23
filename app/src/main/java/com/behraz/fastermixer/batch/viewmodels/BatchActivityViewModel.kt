package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.general.Event
import org.osmdroid.util.GeoPoint
import kotlin.concurrent.fixedRateTimer

class BatchActivityViewModel : ParentViewModel() {

    private var batchLocation: CircleFence? = null

    private val getMixersEvent = MutableLiveData(Event(Unit))
    val mixers = Transformations.switchMap(getMixersEvent) {
        RemoteRepo.getRequestMixers(batchNotPomp = true).map { response ->
            val sortedMixers = batchLocation?.let { batchLocation ->
                response?.entity?.let {
                    if (it.size == 1) {
                        it[0].normalizeStateByDistance(batchLocation)
                        it
                    } else {
                        it.sortedWith(
                            compareBy { mixer ->
                                mixer.latLng.distanceToAsDouble(batchLocation.center)
                                    .also { distance ->
                                        mixer.normalizeStateByDistance(
                                            distance,
                                            batchLocation.radius
                                        )
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
                batchLocation = it
            }
        }
    }

    fun refreshMixers() {
        getMixersEvent.postValue(Event(Unit))
    }

}