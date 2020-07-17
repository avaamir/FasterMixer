package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Equipment
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.models.requests.behraz.ChooseEquipmentRequest
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression
import java.lang.IllegalStateException

class ChooseEquipmentActivityViewModel : ViewModel() {

    private val logOutEvent = MutableLiveData<Event<Unit>>()
    val logoutResponse = Transformations.switchMap(logOutEvent) {
        RemoteRepo.logout()
    }


    val user get() = UserConfigs.user

    private val chooseEquipmentRequest = MutableLiveData<ChooseEquipmentRequest>()
    val chooseEquipmentResponse = Transformations.switchMap(chooseEquipmentRequest) { request ->
        when (UserConfigs.user.value!!.userType) {
            UserType.Pomp -> RemoteRepo.choosePomp(request)
            UserType.Mixer -> TODO()
            UserType.Batch -> RemoteRepo.chooseBatch(request)
        }.exhaustiveAsExpression()
    }

    private val getEquipmentEvent = MutableLiveData<Event<UserType>>()
    val equipments: LiveData<Entity<List<Equipment>>?> =
        Transformations.switchMap(getEquipmentEvent) {
            when (it.peekContent()) {
                UserType.Batch -> RemoteRepo.getBatches()
                UserType.Pomp -> RemoteRepo.getPomps()
                UserType.Mixer -> TODO("getMixers")
            }.exhaustiveAsExpression() as LiveData<Entity<List<Equipment>>?>
        }

    fun getEquipments() {
        user.value?.let {
            getEquipmentEvent.value = Event(it.userType)
        }
    }

    fun logout() {
        logOutEvent.value = Event(Unit)
    }


    fun chooseEquipment(equipmentId: String? = null, retryLastRequest: Boolean? = null) {
        if (retryLastRequest == true)
            chooseEquipmentRequest.value.let { if (it != null) chooseEquipmentRequest.value = it else throw IllegalStateException("not requested before") }
        else
            chooseEquipmentRequest.value = ChooseEquipmentRequest(equipmentId!!)
    }
}