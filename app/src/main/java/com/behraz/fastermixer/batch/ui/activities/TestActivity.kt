package com.behraz.fastermixer.batch.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.os.BatteryManager
import android.os.Bundle
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.behraz.ChooseEquipmentRequest
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMixers
import com.behraz.fastermixer.batch.utils.general.hardware.compass.Compass
import com.behraz.fastermixer.batch.utils.general.subscribeSignalStrengthChangeListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.view_numeric_keyboard.*
import org.osmdroid.util.GeoPoint


class TestActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, Compass.Interactions, MixerAdapter.Interaction {

    private var googleApiClient: GoogleApiClient? = null


    var batchLocation: GeoPoint? = null
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

        numericKeyboard.setInteractions {
            println("debug: $it")
        }

        val x = subscribeSignalStrengthChangeListener(true) {
            println("debug:signal: $it")
        }

        btnLogin.setOnClickListener {
            RemoteRepo.login(LoginRequest("ali", "12345")).observe(this, Observer {
                println("debug: $it")
                it?.let { UserConfigs.loginUser(it.entity!!) }

            })
        }


        btnGetBatches.setOnClickListener {
            RemoteRepo.getBatches().observe(this, Observer {
                println("debug: $it")
            })
        }

        btnLogout.setOnClickListener {
            RemoteRepo.logout().observe(this, Observer {
                println("debug: logout: $it")
            })
        }


        btnChooseBatch.setOnClickListener {
            RemoteRepo.chooseBatch(
                ChooseEquipmentRequest(
                    "14581cea-7969-478a-f515-08d80dd443ec"
                )
            ).observe(this, Observer {
                println("debug: ChooseBatch: $it")
            })
        }

        btnFetch.setOnClickListener {
            /*  val currentTime: Date = Calendar.getInstance().time
              println("debug: currentTime : $currentTime, Battery:$batteryLevel")

              val persianCaldroidDialog = PersianCaldroidDialog()
                  .setOnDateSetListener { dialog, date -> //do something when a date is picked
                      dialog.dismiss()
                  }
              persianCaldroidDialog.typeface = ResourcesCompat.getFont(this, R.font.iransans);
              //set a date to be selected and shown on calendar
              persianCaldroidDialog.selectedDate = (PersianDate(1396, 9, 24))
              persianCaldroidDialog.show(supportFragmentManager,  PersianCaldroidDialog::class.java.name)
  */
        }

        UserRepo.users.observe(this, Observer {
            println("debug: room: $it")
        })



        recycler_view.adapter = mixerAdapter
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )




        btnGetBatchLoc.setOnClickListener {
            RemoteRepo.getEquipmentLocation("d675641c-5c11-4d43-f79f-08d82268081d") {
                batchLocation = it
            }
        }

        btnGetMixers.setOnClickListener {

            fakeMixers().let { mixers ->
                val xx = batchLocation?.let { batchLocation ->
                    mixers.sortedWith(compareBy { mixer ->
                        val y = mixer.latLng.distanceToAsDouble(batchLocation)
                        println("debug:batchLoc=$batchLocation, distance:$y,mixer:${mixer.id}")
                        y
                    })
                }
                mixerAdapter.submitList(xx ?: mixers)
            }

        }




/*
        calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            println("debug: cal: $year/$month/$day -> ${JalaliCalendar.gregorianToJalali(JalaliCalendar.YearMonthDate(year, month, day))}")
        }*/


    }


    override fun onNewAzimuth(azimuth: Float, animation: RotateAnimation) {
        btnFetch.startAnimation(animation)
        println("debug: azimuth: $azimuth")
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
        TODO("Not yet implemented")
    }

    override fun onEndLoadingClicked(mixer: Mixer) {
        TODO("Not yet implemented")
    }


}