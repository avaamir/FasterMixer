package com.behraz.fastermixer.batch.respository.persistance.userdb

import android.content.Context
import com.behraz.fastermixer.batch.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


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

    fun updateBlocking(user: User) {
        runBlocking {
            userDao.update(user)
            println("debugx: UserRepo: blocking finished, User db updated")
        }
    }

    fun delete(item: User) {
        if (!UserRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            userDao.delete(item)
        }
    }

    fun deleteAll() {
        if (!UserRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            userDao.deleteAll()
        }
    }

    fun clearAndInsert(user: User) {
        if (!UserRepo::job.isInitialized || !job.isActive)
            job = Job()
        CoroutineScope(IO + job).launch {
            userDao.deleteAll()
            userDao.insert(user)
        }
    }


    fun cancelJobs() {
        if (UserRepo::job.isInitialized && job.isActive)
            job.cancel()
    }

    fun clearAndInsertBlocking(user: User) {
        runBlocking {
            userDao.deleteAll()
            userDao.insert(user)
        }
    }




}