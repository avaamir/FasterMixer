package com.behraz.fastermixer.batch.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.respository.sharedprefrence.PrefsRepo

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


    fun loginUser(user: User, blocking: Boolean = false) {
        if (!blocking) {
            if (UserConfigs.user.value != user) {
                UserRepo.clearAndInsert(user)
            }
        } else {
            UserRepo.clearAndInsertBlocking(user)
        }
    }

    fun logout() {
        userLive.value?.let {
            PrefsRepo.flush()
            UserRepo.deleteAll()
        }
        userLive.postValue(null)
    }

    fun updateUser(equipmentId: String, blocking: Boolean) {
        user.value!!.copy(equipmentId = equipmentId).let { user ->
            if (!blocking) {
                UserRepo.update(user)
            } else {
                UserRepo.updateBlocking(user)
            }
        }
    }


}