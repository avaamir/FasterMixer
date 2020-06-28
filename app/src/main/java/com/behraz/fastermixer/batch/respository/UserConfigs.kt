package com.behraz.fastermixer.batch.respository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.models.Phone
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.respository.sharedprefrence.PrefsRepo

object UserConfigs {

    private lateinit var context: Context


    //var userVal: User? = null
      //  private set

    private val userLive = MutableLiveData<User?>(null)
    val user: LiveData<User?> get() = userLive


    val isLoggedIn get() = user.value != null

    fun setContext(context: Context) {
        if (!this::context.isInitialized) {
            UserConfigs.context = context.applicationContext
            UserRepo.setContext(context)
            PrefsRepo.setContext(context)

            UserRepo.users.observeForever { users ->
                if (users.isNotEmpty()) {
                    val user = users[0]
                    println("debug: UserConfigs: $user")
                    ApiService.setToken(user.token)
                    userLive.value = user
                    // userVal = user //todo mishe hazfesh kard?
                }
            }
        }
    }

    fun loginUser(user: User) {
        if (UserConfigs.user.value != user) {
            UserRepo.insert(user)
        }
    }

    fun logout() {
        PrefsRepo.flush()
        //todo delete other databases except favorite
        userLive.value?.let {
            RemoteRepo.logout()
            UserRepo.delete(it)
        }
       // userVal = null
        userLive.postValue(null)
    }


}