package com.behraz.fastermixer.batch.viewmodels

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.*
import com.behraz.fastermixer.batch.models.Contact
import com.behraz.fastermixer.batch.respository.persistance.contactdb.ContactRepo
import com.behraz.fastermixer.batch.utils.general.DoubleTrigger
import com.behraz.fastermixer.batch.utils.general.Event
import kotlinx.coroutines.selects.select

class ContactActivityViewModel(application: Application) : AndroidViewModel(application) {
    //var allContacts: ArrayList<Contact>? = null
    init {
        ContactRepo.setContext(application.applicationContext)
    }


    private val companyFilter = MutableLiveData("همه")
    private val selectAll = MutableLiveData<Event<Unit>>()

    private var keyword: String? = null
        set(value) {
            field = if (value == "")
                null
            else
                value
        }


    val organizations = Transformations.map(ContactRepo.allContacts) { contacts ->
        contacts.map { it.company }.distinct()
    }


    private val isCheckedMap = HashMap<String, Boolean>()

    val contacts =
        Transformations.switchMap(DoubleTrigger(companyFilter, ContactRepo.allContacts)) {
            val company = it.first!!
            if (company == "همه") {
                ContactRepo.search(keyword = keyword ?: "%")
            } else {
                ContactRepo.search(keyword = keyword ?: "%", company = company)
            }.map { contacts ->
                if (isCheckedMap.size != 0) {
                    var hitCount = 0 //for optimization purpose
                    contacts.forEach { contact ->
                        if (isCheckedMap[contact.mobileNumber] == true) {
                            contact.isChecked = true
                            hitCount++
                            if (hitCount == isCheckedMap.size)
                                return@forEach
                        }
                    }
                }
                contacts
            }
        }

    /*fun filterContactsByOrganization(organization: String) {
        _contacts.value = if (organization != "همه") {
            allContacts.value!!.filter { contact ->
                contact.company == organization
            }
        } else {
            allContacts.value
        }
    }*/

    fun searchAndFilter(keyword: String, organization: String = companyFilter.value!!) {
        this.keyword = keyword
        companyFilter.value = organization
    }

    fun filterOrganization(organization: String) {
        companyFilter.value = organization
    }

    fun allContacts() {
        keyword = null
        companyFilter.value = "همه"
    }

    fun insertContact(contact: Contact) {
        ContactRepo.insert(contact)
    }

    fun insertContact(contacts: List<Contact>) {
        ContactRepo.insert(contacts)
    }

    fun selectAll(shouldSelect: Boolean) {
        //TODO implement this
        contacts.value?.forEach {
            it.isChecked = shouldSelect
            println("debug: ${it.displayName}")
        }
        selectAll.value = Event(Unit)
    }

    fun selectContact(contact: Contact) {
        if (contact.isChecked)
            isCheckedMap[contact.mobileNumber] = contact.isChecked
        else
            isCheckedMap.remove(contact.mobileNumber)
    }


}