package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Contact

class ContactActivityViewModel : ViewModel() {
    var allContacts: ArrayList<Contact>? = null
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    fun filterContactsByOrganization(organization: String) {
        _contacts.value = if (organization != "همه") {
            allContacts!!.filter { contact ->
                contact.company == organization
            }
        } else {
            allContacts
        }
    }

    fun selectAll(shouldSelect: Boolean) {
        contacts.value?.forEach {
            it.isChecked = shouldSelect
        }
        _contacts.value = contacts.value
    }

}