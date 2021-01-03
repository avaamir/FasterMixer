package com.behraz.fastermixer.batch.app

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.receivers.isNetworkAvailable
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.apiservice.MapService
import com.behraz.fastermixer.batch.respository.apiservice.WeatherService
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.GlobalErrorHandlerInterceptor
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.NetworkConnectionInterceptor
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.respository.sharedprefrence.PrefsRepo
import com.behraz.fastermixer.batch.ui.activities.ContactActivity
import com.behraz.fastermixer.batch.ui.activities.LoginActivity
import com.behraz.fastermixer.batch.ui.activities.TestActivity
import com.behraz.fastermixer.batch.ui.activities.admin.AdminActivity
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.fullScreen
import com.behraz.fastermixer.batch.utils.general.hideStatusBar

class FasterMixerApplication : Application(), NetworkConnectionInterceptor.NetworkAvailability,
    GlobalErrorHandlerInterceptor.ApiResponseErrorHandler {
    companion object {
        var isDemo: Boolean = false
    }

    //Global Events
    private val onAuthorizeEvent = MutableLiveData<Event<Unit>>()
    private val onInternetUnavailableEvent = MutableLiveData<Event<Unit>>()


    //Typefaces
    val iransans: Typeface by lazy {
        ResourcesCompat.getFont(
            this,
            R.font.iransans
        )!!
    }
    val iransansMedium: Typeface by lazy {
        ResourcesCompat.getFont(
            this,
            R.font.iransans_medium
        )!!
    }
    val iransansLight: Typeface by lazy {
        ResourcesCompat.getFont(
            this,
            R.font.iransans_light
        )!!
    }

    override fun onCreate() {
        super.onCreate()

        initRepos()
        registerApiInterceptorsCallbacks()
    }

    private fun initRepos() {
        UserRepo.setContext(applicationContext)
        ApiService.init(this, this)
        MapService.init(this, this)
        WeatherService.init(this, this)
        MessageRepo.setContext(applicationContext)
        PrefsRepo.setContext(applicationContext)
        UserConfigs.init()
    }


    private fun registerApiInterceptorsCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStarted(activity: Activity?) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
            override fun onActivityStopped(activity: Activity?) {}
            override fun onActivityDestroyed(activity: Activity?) {}
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleScreenOrientation(activity)
                activity.hideStatusBar()
                fullScreen(activity)
                wakeLock(activity)
                registerGlobalEventObservers(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                fullScreen(activity)
            }

        })
    }

    private fun wakeLock(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun handleScreenOrientation(activity: Activity) {
        if (activity !is LoginActivity && activity !is TestActivity && activity !is ContactActivity && activity !is AdminActivity) {
            if (resources.getBoolean(R.bool.landscape_only)) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                //todo activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //todo make another layout for phones
            }
        }
    }

    private fun fullScreen(activity: Activity) {
        if (activity !is LoginActivity && activity !is AdminActivity && activity !is ContactActivity && activity !is AdminActivity) {
            activity.fullScreen()
        }
    }

    private fun registerGlobalEventObservers(activity: Activity) {
        activity as LifecycleOwner
        onInternetUnavailableEvent.observe(activity) {
            NoNetworkDialog(activity, R.style.my_alert_dialog).show()
        }
        if (activity !is LoginActivity) {
            onAuthorizeEvent.observe(activity) {
                if (!it.hasBeenHandled) {
                    activity.finish()
                }
            }
        } else {
            onAuthorizeEvent.observe(activity) {
                it.getEventIfNotHandled()?.let {
                    UserConfigs.logout()
                }
            }
        }
    }

    override fun isInternetAvailable(): Boolean {
        return isNetworkAvailable()
    }

    override fun onInternetUnavailable() {
        onInternetUnavailableEvent.postValue(Event(Unit))
        println("debux: NetworkError1")
    }

    override fun onHandleError(errorType: ErrorType, errorBody: String?) {
        if (errorType == ErrorType.UnAuthorized) {
            onAuthorizeEvent.postValue(Event(Unit))
            UserConfigs.logout()
        } else if(errorType == ErrorType.NetworkError) {
            println("debux: NetworkError2")
        }
    }

}