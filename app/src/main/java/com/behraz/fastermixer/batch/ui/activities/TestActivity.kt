package com.behraz.fastermixer.batch.ui.activities


import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityTestBinding
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.ui.dialogs.NewMessageDialog
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMessages
import com.behraz.fastermixer.batch.utils.general.log
import com.behraz.fastermixer.batch.utils.general.subscribeSignalStrengthChangeListener
import com.behraz.fastermixer.batch.utils.general.toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, MixerAdapter.BatchAdapterInteraction {

    protected val parties = arrayOf(
        "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
        "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
        "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
        "Party Y", "Party Z"
    )

    private lateinit var mBinding: ActivityTestBinding

    private var counter = 0

    private var googleApiClient: GoogleApiClient? = null


    var batchLocation: CircleFence? = null
    private val mixerAdapter = MixerAdapter(false, this)

    private val batteryLevel: Float
        get() {
            val batteryIntent =
                registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            // Error checking that probably isn't needed but I added just in case.
            return if (level == -1 || scale == -1) {
                50.0f
            } else level.toFloat() / scale.toFloat() * 100.0f
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        val x = subscribeSignalStrengthChangeListener(true) {
            println("debug:signal: $it")
        }


        // initProgressChart()
        initViews()

    }

    private fun initViews() {
       btnApiTest.setOnClickListener {
           RemoteRepo.getMessage {
               log(it, "debux")
           }
       }
    }



    //enable GPS req by google Play--------------------------------------------

    private fun enableGPSReq() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
        }
        googleApiClient?.let {
            it.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000
            locationRequest.fastestInterval = 5 * 1000
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            // **************************
            builder.setAlwaysShow(true) // this is the key ingredient
            // **************************

            val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi
                .checkLocationSettings(it, builder.build())
            result.setResultCallback {
                val status: Status = it.status
                val state: LocationSettingsStates = it
                    .locationSettingsStates
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        println("debug: Location settings are satisfied ")
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be
                        // fixed by showing the user
                        // a dialog.
                        try {
                            println("debug: Location settings are not satisfied, fixed by showing the user a dialog")
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(this@TestActivity, 1000)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        println("debug: Location settings are not satisfied, no way to fix ")
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                    }
                }
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        println("debug: onConnected -> $p0")
    }

    override fun onConnectionSuspended(p0: Int) {
        println("debug: onConnectionSuspended -> $p0")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        println("debug: onConnectionFailed -> $p0")
    }

    override fun onCallClicked(mixer: Mixer) {
        toast("Not yet implemented")
    }

    override fun onEndLoadingClicked(mixer: Mixer) {
        toast("Not yet implemented")
    }

}