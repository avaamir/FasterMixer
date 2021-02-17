package com.behraz.fastermixer.batch.app

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.annotation.RequiresPermission
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.receivers.isNetworkAvailable
import com.behraz.fastermixer.batch.models.PointInfo
import com.behraz.fastermixer.batch.models.RecordedPointInfo
import com.behraz.fastermixer.batch.models.SubmitRecordedPointInfo
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType.*
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.apiservice.AnonymousService
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
import com.behraz.fastermixer.batch.ui.activities.admin.AdminMessagesActivity
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.utils.general.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer

class FasterMixerApplication : Application(), NetworkConnectionInterceptor.NetworkAvailability,
    GlobalErrorHandlerInterceptor.ApiResponseErrorHandler {
    companion object {
        var isDemo: Boolean = false
    }

    //saving and restoring mac in sharedPreference
    private val deviceMacAddress by lazy {
        PrefsRepo.getMac() ?: getMAC()?.toLowerCase(Locale.ROOT)?.also {
            PrefsRepo.saveMac(it)
        }
    }

    private val pointSubmitter by lazy {
        PointSubmitter()
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

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun registerLocationUpdaterIfNeeded() {
        val mac = deviceMacAddress
        if (mac != null) {
            RemoteRepo.isMacValid(mac) {
                if (it.isSucceed) {
                    if (it.entity?.isValid == true)
                        pointSubmitter.start()
                } else {
                    Handler().postDelayed({
                        registerLocationUpdaterIfNeeded()
                    }, 5000)
                }
            }
        }
    }

    private fun initRepos() {
        UserRepo.setContext(applicationContext)
        ApiService.init(this, this)
        AnonymousService.init(null, this)
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
                if (pointSubmitter.isStarted) {
                    LocationCompassProvider.fixDeviceOrientationForCompassCalculation(activity)
                }
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
        if (activity !is LoginActivity && activity !is TestActivity && activity !is ContactActivity && activity !is AdminActivity && activity !is AdminMessagesActivity) {
            if (resources.getBoolean(R.bool.landscape_only)) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                //todo activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //todo make another layout for phones
            }
        }
    }

    private fun fullScreen(activity: Activity) {
        if (activity !is LoginActivity && activity !is AdminActivity && activity !is ContactActivity && activity !is AdminActivity && activity !is AdminMessagesActivity) {
            activity.fullScreen()
        }
    }

    private fun registerGlobalEventObservers(activity: Activity) {
        activity as LifecycleOwner
        onInternetUnavailableEvent.observe(activity) {
            it.getEventIfNotHandled()?.let {
                NoNetworkDialog(activity, R.style.my_alert_dialog).show()
            }
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
        log("debux: NetworkError1")
    }

    override fun onHandleError(errorType: ErrorType, errorBody: String?) {
        if (errorType == UnAuthorized) {
            onAuthorizeEvent.postValue(Event(Unit))
            UserConfigs.logout()
        } else if (errorType == NetworkError) {
            log("debux: NetworkError2")
        }
    }

    private inner class PointSubmitter {
        var isStarted = false
            private set

        private val fifo: FIFO<RecordedPointInfo> = FIFO(100)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)

        private lateinit var timer: Timer

        private var counter = 0

        @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        fun start() {
            if (!isStarted) {
                isStarted = true
                LocationCompassProvider.startLocationService(applicationContext)
                timer = fixedRateTimer(period = 10000L) {
                    val point = LocationCompassProvider.lastKnownLocation
                    if (point != null) {
                        if (point.speed == 0f) {
                            counter++
                            if (counter == 6) { //wait 1min when car is stopped
                                counter = 0
                                submitPoint(point)
                            }
                        } else {
                            counter = 0
                            submitPoint(point)
                        }
                    }
                }
            }
        }

        fun submitPoint(point: Location) {
            val pointInfo = PointInfo(
                deviceMacAddress!!,
                point.latitude,
                point.longitude,
                point.speed,
                simpleDateFormat.format(now())
            )
            RemoteRepo.submitPoint(pointInfo) {
                if (!it.isSucceed) {
                    fifo.add(pointInfo) //TODO add to db if FIFO is full
                } else {
                    if (fifo.isNotEmpty()) {
                        RemoteRepo.submitPoint(
                            //TODO read from db and mege it with fifo then submit recorded points //clear repo after
                            SubmitRecordedPointInfo(deviceMacAddress!!, fifo)
                        ) { result ->
                            if (result.isSucceed) {
                                fifo.clear()
                            }
                        }
                    }
                }
            }
        }

        fun stop() {
            isStarted = false
            LocationCompassProvider.stopLocationService(this@FasterMixerApplication)
            timer.cancel()
            timer.purge()
        }
    }


}