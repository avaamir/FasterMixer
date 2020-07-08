package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.utils.fastermixer.fakeAdminEquipments
import com.behraz.fastermixer.batch.utils.general.DoubleTrigger
import com.behraz.fastermixer.batch.utils.general.Event

class EquipmentsFragmentViewModel : ViewModel() {
    private val getEquipmentsEvent = MutableLiveData(Event(Unit))
    private val sortEquipmentsEvent = MutableLiveData<List<AdminEquipment>>()
    val equipments =
        Transformations.switchMap(DoubleTrigger(getEquipmentsEvent, sortEquipmentsEvent)) {
            if (it.first?.getEventIfNotHandled() != null) {
                MutableLiveData(fakeAdminEquipments())
            } else {
                MutableLiveData(it.second!!)
            }
        }


    fun refreshEquipments() {
        getEquipmentsEvent.value = Event(Unit)
    }

    fun sortEquipments(byState: Boolean) { //or byType
        equipments.value?.let { _equipments ->
            sortEquipmentsEvent.value =
                _equipments.sortedWith(if (byState) compareBy { it.state } else compareBy { it.type })
        }
    }

}