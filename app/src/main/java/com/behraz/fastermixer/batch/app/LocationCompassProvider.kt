package com.behraz.fastermixer.batch.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.GeomagneticField
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.utils.general.LocationHandler
import com.behraz.fastermixer.batch.utils.general.log
import org.osmdroid.views.overlay.compass.IOrientationConsumer
import org.osmdroid.views.overlay.compass.IOrientationProvider
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider


fun Context.isGpsEnabled() =
    LocationCompassProvider.isProviderEnable(this)

object LocationCompassProvider : LocationListener, IOrientationConsumer {


    data class AngleResult(
        val angle: Float,
        val isCompassProvider: Boolean
    )
    // val isStarted get() = compass != null

    private val _userAngle = MutableLiveData<AngleResult>()
    private val _northAngle = MutableLiveData<AngleResult>()
    private val _location = MutableLiveData<Location>()
    private val _providerStateChanged = MutableLiveData<String>()

    val userAngle: LiveData<AngleResult> = _userAngle
    val northAngle: LiveData<AngleResult> = _northAngle
    val location: LiveData<Location> = _location
    val providerStateChanged: LiveData<String> = _providerStateChanged

    private const val TAG = "LocationCompassProvider"

    private var compass: InternalCompassOrientationProvider? = null


    val lastKnownLocation get() = _location.value //todo ?: Constants.mapStartPoint

    private val lock = Any()
    private var deviceOrientation: Int = -1
        set(value) {
            log(value)
            field = value
        }

    private var gpsspeed = 0f
    private var lat = 0f
    private var lon = 0f
    private var alt = 0f
    private var timeOfFix: Long = 0
    private var trueNorth = 0f
    private val shouldUseCompass get() = gpsspeed < 1.0//0.01


    fun isProviderEnable(context: Context) =
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )

    @SuppressLint("MissingPermission")
    fun start(context: Context): Boolean {
        if (compass != null)
            throw IllegalStateException("already started")

        //initial compass
        val compass = InternalCompassOrientationProvider(context)
        compass.startOrientationProvider(this)
        this.compass = compass


        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        val providers = ArrayList<String>()
        // val provider = when {
        if (locationManager.allProviders.contains(LocationManager.NETWORK_PROVIDER)) {
            println("debug: allProviders contains NETWORK_PROVIDER")
            providers.add(LocationManager.NETWORK_PROVIDER)
        }
        if (locationManager.allProviders.contains(LocationManager.GPS_PROVIDER)) {
            println("debug: allProviders contains GPS_PROVIDER")
            providers.add(LocationManager.GPS_PROVIDER)
        }
        /*locationManager.allProviders.contains(LocationManager.PASSIVE_PROVIDER) -> {
            println("debug: allProviders contains NETWORK_PROVIDER")
            LocationManager.PASSIVE_PROVIDER
        }*/
        //     else -> null
        //  }

        if (providers.isNotEmpty()) {
            providers.forEach {
                locationManager.requestLocationUpdates(
                    it,
                    LocationHandler.MIN_LOCATION_UPDATE_TIME,
                    0f,
                    this
                )
                val lastKnownLocation = locationManager.getLastKnownLocation(it)
                if (lastKnownLocation != null) onLocationChanged(lastKnownLocation)
            }
        } else {
            return false
        }

        return true
    }

    fun stop(context: Context) {
        if (compass == null)
            throw IllegalStateException("not started")

        compass!!.stopOrientationProvider()
        compass!!.destroy()
        compass = null

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this)
    }


    override fun onLocationChanged(location: Location) {
        _location.value = location
        val angle = calcUserBearingViaGPS(location, deviceOrientation)
        if (!shouldUseCompass) { //otherwise let the compass take over
            _userAngle.value = AngleResult(smoothAngle(angle), false)
        }
    }

    override fun onStatusChanged(
        provider: String?,
        status: Int,
        extras: Bundle?
    ) {
        println("debug:onStatusChanged: $provider")
    }

    @SuppressLint("MissingPermission")
    override fun onProviderEnabled(provider: String?) {
        println("debug:onProviderEnabled: $provider")
        _providerStateChanged.value = provider
    }

    override fun onProviderDisabled(provider: String?) {
        println("debug:onProviderDisabled: $provider")
        _providerStateChanged.value = provider
    }

    private fun calcUserBearingViaGPS(location: Location, deviceOrientation: Int): Float {

        if (!(deviceOrientation == 270 || deviceOrientation == 180 || deviceOrientation == 90 || deviceOrientation == 0)) {
            if (deviceOrientation == -1)
                throw IllegalStateException("deviceOrientation has not been initialized use `fixDeviceOrientationForCompassCalculation` for initializing it")
            else
                throw IllegalStateException("deviceOrientation must be 0 or 90 or 180 or 270")
        }

        //Get User Bearing
        val gpsbearing = location.bearing
        gpsspeed = location.speed
        lat = location.latitude.toFloat()
        lon = location.longitude.toFloat()
        alt = location.altitude.toFloat() //meters

        timeOfFix = location.time


        //use gps bearing instead of the compass
        var t: Float =
            360 - gpsbearing //- deviceOrientation //TODO dar halat landscape dorost javab nemidad, hazf ke shod dorost shod, ruye device haye dg ham barrasi shavad
        if (t < 0) {
            t += 360f
        }
        if (t > 360) {
            t -= 360f
        }
        return t
    }

    override fun onOrientationChanged(
        orientationToMagneticNorth: Float,
        source: IOrientationProvider?
    ) {
        //note, on devices without a compass this never fires...
        //only use the compass bit if we aren't moving, since gps is more accurate when we are moving
        if (shouldUseCompass) {
            val gf = GeomagneticField(lat, lon, alt, timeOfFix)
            trueNorth = orientationToMagneticNorth + gf.declination

            synchronized(lock) {
                if (trueNorth > 360.0f) {
                    trueNorth -= 360.0f
                }

                //this part adjusts the desired map rotation based on device orientation and compass heading
                var t: Float = 360 - trueNorth - deviceOrientation
                if (t < 0) {
                    t += 360f
                }
                if (t > 360) {
                    t -= 360f
                }

                _userAngle.value = AngleResult(smoothAngle(t), true)
                _northAngle.value = AngleResult(trueNorth, true)
            }
        }
    }

    private fun smoothAngle(actualHead: Float): Float {
        var t = actualHead.toInt().toFloat()
        t /= 5
        t = t.toInt().toFloat()
        return t * 5
        // return actualHead //TODO nothing has been smoothed
    }

    fun fixDeviceOrientationForCompassCalculation(activity: Activity) {
        //hack for x86
        if (!"Android-x86".equals(Build.BRAND, ignoreCase = true)) {
            //lock the device in current screen orientation
            val orientation: Int
            val rotation = (activity.getSystemService(
                Context.WINDOW_SERVICE
            ) as WindowManager).defaultDisplay.rotation
            when (rotation) {
                Surface.ROTATION_0 -> {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    deviceOrientation = 0
                    println("ROTATION_0 SCREEN_ORIENTATION_PORTRAIT")
                }
                Surface.ROTATION_90 -> {
                    deviceOrientation = 90
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    println("ROTATION_90 SCREEN_ORIENTATION_LANDSCAPE")
                }
                Surface.ROTATION_180 -> {
                    deviceOrientation = 180
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    println("ROTATION_180 SCREEN_ORIENTATION_REVERSE_PORTRAIT")
                }
                else -> {
                    deviceOrientation = 270
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    println("ROTATION_270 SCREEN_ORIENTATION_REVERSE_LANDSCAPE")
                }
            }
            activity.requestedOrientation = orientation
        } else {
            deviceOrientation = 0
        }
    }
}