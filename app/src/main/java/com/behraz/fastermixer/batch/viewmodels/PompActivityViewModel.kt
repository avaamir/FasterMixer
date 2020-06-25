package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Customer
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.utils.fastermixer.fakeCustomers
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMixers

class PompActivityViewModel : ViewModel() {
    private val _customer =  MutableLiveData<Customer>()
    val customer:LiveData<Customer> get() = _customer

    private val _currentMixer =  MutableLiveData<Mixer>()
    val currentMixer:LiveData<Mixer> get() = _currentMixer


    init {
        _customer.value = fakeCustomers()[0]  //todo ui test purpose
        _currentMixer.value = fakeMixers()[0]  //todo ui test purpose
    }

}