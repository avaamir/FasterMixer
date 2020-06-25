package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Customer
import com.behraz.fastermixer.batch.utils.fastermixer.fakeCustomers

class CustomerListFragmentViewModel: ViewModel() {
    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> get() = _customers


    init {
        _customers.value =
            fakeCustomers()  //TODO ui test purpose
    }
}