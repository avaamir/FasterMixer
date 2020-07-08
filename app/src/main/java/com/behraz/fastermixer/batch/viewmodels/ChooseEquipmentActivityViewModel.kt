package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.*
import com.behraz.fastermixer.batch.models.Equipment
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.models.requests.behraz.ChooseEquipmentRequest
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression

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
        }.exhaustiveAsExpression().map { response ->
            if (response?.isSucceed == true) {
                UserConfigs.updateUser(request.equipmentId)
            }
            response
        }
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
        getEquipmentEvent.value = Event(user.value!!.userType)
    }

    fun logout() {
        logOutEvent.value = Event(Unit)
    }


    fun chooseEquipment(equipmentId: String) {
        chooseEquipmentRequest.value = ChooseEquipmentRequest(equipmentId)
    }
}