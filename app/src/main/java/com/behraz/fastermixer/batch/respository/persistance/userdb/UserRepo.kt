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

    val userDao: UserDao by lazy {
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

    suspend fun updateBlocking(user: User) {
        userDao.update(user)
        println("debugx: UserRepo: blocking finished, User db updated")
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

    suspend fun clearAndInsertBlocking(user: User) {
        userDao.deleteAll()
        userDao.insert(user)
    }

    fun clearAndInsertBlocking2(user: User) {
        userDao.deleteAll1()
        userDao.insert1(user)
    }


}