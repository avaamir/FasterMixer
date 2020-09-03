package com.behraz.fastermixer.batch.respository.persistance.contactdb

import android.content.Context
import androidx.lifecycle.LiveData
import com.behraz.fastermixer.batch.models.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object ContactRepo {
    private lateinit var job: Job
    private lateinit var context: Context

    private val ContactDao: ContactDao by lazy {
        ContactDatabase.getInstance(context).getDao()
    }


    fun setContext(context: Context) {
        ContactRepo.context = context.applicationContext
    }

    val allContacts: LiveData<List<Contact>> by lazy {
        ContactDao.allContacts
    }

    fun insert(items: List<Contact>) {
        if (!ContactRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            ContactDao.insertAll(items)
        }
    }


    fun search(keyword: String, company: String = "%"): LiveData<List<Contact>> {
        if (!ContactRepo::job.isInitialized || !job.isActive)
            job = Job()
        return ContactDao.searchAndFilterByCompany("%$keyword%", company)
    }

    fun filterByCompany(filter: String): LiveData<List<Contact>> {
        if (!ContactRepo::job.isInitialized || !job.isActive)
            job = Job()
        return ContactDao.filterByCompany(filter)
    }

    fun insert(item: Contact) {
        if (!ContactRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            ContactDao.insert(item)
        }
    }

    fun update(item: Contact) {
        if (!ContactRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            ContactDao.update(item)
        }
    }

    fun delete(item: Contact) {
        if (!ContactRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            ContactDao.delete(item)
        }
    }

    fun deleteAllContacts() {
        if (!ContactRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            ContactDao.deleteAllContacts()
        }
    }

    fun cancelJobs() {
        if (ContactRepo::job.isInitialized && job.isActive)
            job.cancel()
    }
}