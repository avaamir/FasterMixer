package com.behraz.fastermixer.batch.respository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.respository.sharedprefrence.PrefsRepo
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.respository.apiservice.ApiService

object UserConfigs {

    private lateinit var context: Context


    var userVal: User? = null
        private set

    private val userLive = MutableLiveData<User?>(null)
    val user: LiveData<User?> =
        userLive


    val isLoggedIn get() = userVal != null

    fun setContext(context: Context) {
        if (!this::context.isInitialized) {
            UserConfigs.context = context.applicationContext
            UserRepo.setContext(context)
            PrefsRepo.setContext(UserConfigs.context)

            UserRepo.users.observeForever { users ->
                if (users.isNotEmpty()) {
                    val user = users[0]
                    println("debug: UserConfigs: $user")
                    ApiService.setBearerToken(user.token!!)
                    userLive.value = user
                    userVal = user //todo mishe hazfesh kard?
                }
            }
        }
    }

    fun loginUser(user: User) {
        if (UserConfigs.user.value != user) {
            UserRepo.insert(user)
        }
    }

    fun updateUser(
        phone: String = userVal!!.phone,
        name: String = userVal!!.name,
        image: String? = userVal!!.profilePic
    ) {
        UserRepo.update(userVal!!.copy(phone = phone, name = name, profilePic = image))
    }

    fun logout() {
        PrefsRepo.flush()
        //todo delete other databases except favorite
        userVal?.let {
            UserRepo.delete(it)
        }
        userVal = null
        userLive.postValue(null)
    }


}