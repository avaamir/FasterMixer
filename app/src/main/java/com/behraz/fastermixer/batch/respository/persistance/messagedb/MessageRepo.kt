package com.behraz.fastermixer.batch.respository.persistance.messagedb

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.respository.UserConfigs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object MessageRepo {
    private lateinit var job: Job
    private lateinit var context: Context

    private val MessageDao: MessageDao by lazy {
        MessageDatabase.getInstance(context).getDao()
    }


    fun setContext(context: Context) {
        MessageRepo.context = context.applicationContext
    }

    val allMessage: LiveData<List<Message>> by lazy {
        MessageDao.allMessage.map {
            it.filter {
                it.userId == UserConfigs.user.value!!.personId
            }
            it
        }
    }

    fun insert(items: List<Message>) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            items.forEach { item ->
                if (item.userId == null)
                    item.userId = UserConfigs.user.value!!.personId
            }
            MessageDao.insertAll(items)
        }
    }

    fun insert(item: Message) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            if (item.userId == null)
                item.userId = UserConfigs.user.value!!.personId
            MessageDao.insert(item)
        }
    }

    fun update(item: Message) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            if (item.userId == null)
                item.userId = UserConfigs.user.value!!.personId
            MessageDao.update(item)
        }
    }

    fun delete(item: Message) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            MessageDao.delete(item)
        }
    }

    fun deleteAllMessages() {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            MessageDao.deleteAllMessage()
        }
    }

    fun cancelJobs() {
        if (MessageRepo::job.isInitialized && job.isActive)
            job.cancel()
    }
}