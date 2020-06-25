package com.behraz.fastermixer.batch.respository.persistance.userdb

import android.content.Context
import com.behraz.fastermixer.batch.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object UserRepo {
    private lateinit var job: Job
    private lateinit var context: Context

    private val userDao: UserDao by lazy {
        UserDatabase.getInstance(context).getUserDao()
    }


    fun setContext(context: Context) {
        UserRepo.context = context.applicationContext
    }

    val users get() = userDao.users


    fun insert(item: User) {
        if (!UserRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            userDao.insert(item)
        }
    }

    fun update(item: User) {
        if (!UserRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            userDao.update(item)
        }
    }

    fun delete(item: User) {
        if (!UserRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            userDao.delete(item)
        }
    }

    fun cancelJobs() {
        if (UserRepo::job.isInitialized && job.isActive)
            job.cancel()
    }
}