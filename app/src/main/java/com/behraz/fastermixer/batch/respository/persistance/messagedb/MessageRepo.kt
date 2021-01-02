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

    private val dao: MessageDao by lazy {
        MessageDatabase.getInstance(context).getDao()
    }


    fun setContext(context: Context) {
        MessageRepo.context = context.applicationContext
    }

    val allMessage: LiveData<List<Message>> by lazy {
        dao.allMessage.map { messages ->
            messages.filter {
                it.userId == UserConfigs.user.value!!.id
            }
        }
    }

    fun insert(items: List<Message>) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            items.forEach { item ->
                if (item.userId == null)
                    item.userId = UserConfigs.user.value!!.id
            }
            dao.insertAll(items)
        }
    }

    fun insert(item: Message) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            if (item.userId == null)
                item.userId = UserConfigs.user.value!!.id
            dao.insert(item)
        }
    }

    fun update(item: Message) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            if (item.userId == null)
                item.userId = UserConfigs.user.value!!.id
            dao.update(item)
        }
    }

    fun delete(item: Message) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            dao.delete(item)
        }
    }

    fun deleteAllMessages() {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            dao.deleteAllMessage()
        }
    }

    fun cancelJobs() {
        if (MessageRepo::job.isInitialized && job.isActive)
            job.cancel()
    }

    fun seenMessage(message: Message) {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            dao.update(message.copy(viewed = true))
        }
    }

    fun seenAllMessages() {
        if (!MessageRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            dao.seenAllMessages()
        }
    }
}