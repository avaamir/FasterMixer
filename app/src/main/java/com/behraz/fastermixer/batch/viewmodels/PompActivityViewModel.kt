package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Customer
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.fastermixer.fakeCustomers
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMixers
import com.behraz.fastermixer.batch.utils.general.Event

class PompActivityViewModel : ViewModel() {

    val user get() = UserConfigs.user

    private val logOutEvent = MutableLiveData<Event<Unit>>()
    val logoutResponse = Transformations.switchMap(logOutEvent) {
        RemoteRepo.logout()
    }

    private val _customer =  MutableLiveData<Customer>()
    val customer:LiveData<Customer> get() = _customer

    private val _currentMixer =  MutableLiveData<Mixer>()
    val currentMixer:LiveData<Mixer> get() = _currentMixer


    init {
        _customer.value = fakeCustomers()[0]  //todo ui test purpose
        _currentMixer.value = fakeMixers()[0]  //todo ui test purpose
    }


    fun logout() {
        logOutEvent.value = Event(Unit)
    }


}