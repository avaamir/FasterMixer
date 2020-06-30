package com.behraz.fastermixer.batch.app

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.respository.sharedprefrence.PrefsRepo
import com.behraz.fastermixer.batch.ui.activities.LoginActivity
import com.behraz.fastermixer.batch.utils.general.fullScreen
import com.behraz.fastermixer.batch.utils.general.hideStatusBar

class FasterMixerApplication : Application() {
    //Typefaces
    val iransans: Typeface by lazy {
        ResourcesCompat.getFont(this,
            R.font.iransans
        )!!
    }
    val iransansMedium: Typeface by lazy {
        ResourcesCompat.getFont(this,
            R.font.iransans_medium
        )!!
    }
    val iransansLight: Typeface by lazy {
        ResourcesCompat.getFont(this,
            R.font.iransans_light
        )!!
    }
    val belham: Typeface by lazy {
        ResourcesCompat.getFont(this, R.font.belham)!!
    }


    override fun onCreate() {
        super.onCreate()

        initRepos()
        registerApiInterceptorsCallbacks()
    }

    private fun initRepos() {
        UserRepo.setContext(applicationContext)
        ApiService.setContext(applicationContext)
        MessageRepo.setContext(applicationContext)
        PrefsRepo.setContext(applicationContext)
        UserConfigs.init()
    }


    private fun registerApiInterceptorsCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                if (activity !is LoginActivity) {
                    activity.fullScreen()
                }

                if (activity is ApiService.InternetConnectionListener) {
                    ApiService.setInternetConnectionListener(activity)
                }
                if (activity is ApiService.OnUnauthorizedListener) {
                    ApiService.setOnUnauthorizedAction(activity)
                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (activity is ApiService.InternetConnectionListener) {
                    ApiService.removeInternetConnectionListener(activity)
                }
                if (activity is ApiService.OnUnauthorizedListener) {
                    ApiService.removeUnauthorizedAction(activity)
                }
            }

            override fun onActivityStarted(activity: Activity?) {}
            override fun onActivityDestroyed(activity: Activity?) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
            override fun onActivityStopped(activity: Activity?) {}
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                if (resources.getBoolean(R.bool.landscape_only)) {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    //todo activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //todo make another layout for phones
                }
                if (activity is ApiService.InternetConnectionListener) {
                    ApiService.setInternetConnectionListener(activity)
                }

                activity.hideStatusBar()
                if (activity !is LoginActivity) {
                    activity.fullScreen()
                }

                activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        })
    }

}