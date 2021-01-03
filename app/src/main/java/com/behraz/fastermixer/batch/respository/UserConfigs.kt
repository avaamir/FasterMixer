package com.behraz.fastermixer.batch.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.respository.sharedprefrence.PrefsRepo
import kotlinx.coroutines.CoroutineDispatcher

object UserConfigs {
    private val userLive = MutableLiveData<User?>(null)
    val user: LiveData<User?> get() = userLive
    val isLoggedIn get() = user.value != null

    fun init() {
        UserRepo.users.observeForever { users ->
            if (users.isNotEmpty()) {
                val user = users[0]
                ApiService.setToken(user.token)
                println("debug: UserConfigs token set: $user")
                userLive.value = user
            }
        }
    }


    fun loginUser(user: User) {
        if (UserConfigs.user.value != user) {
            UserRepo.clearAndInsert(user)
        }
    }

    suspend fun loginUserBlocking(user: User) {
        UserRepo.clearAndInsertBlocking(user)
    }


    fun logout() {
        ApiService.setToken(null)
        userLive.value?.let {
            PrefsRepo.flush()
            UserRepo.deleteAll()
        }
        userLive.postValue(null)
    }

    fun updateUser(equipmentId: Int ) {
        user.value!!.copy(equipmentId = equipmentId).let { user ->

                UserRepo.update(user)

        }
    }

    suspend  fun updateUserBlocking(equipmentId: Int) {
        user.value!!.copy(equipmentId = equipmentId).let { user ->
                UserRepo.updateBlocking(user)
        }
    }


}