package com.behraz.fastermixer.batch.ui.activities

import android.content.IntentSender
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.models.requests.behraz.ChooseBatchRequest
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.utils.general.toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        btnLogin.setOnClickListener {
            /*val gson = Gson()

          val x = gson.fromJson(
               "{\"entity\":{\"id\":2,\"isAvailable\":true,\"name\":\"batch num1\"},\"isSuccess\":true,\"message\":\"fuck me\"}"
               ,
               EntityResponse<Batch>::class.java
           )
           println(x)

          // println(gson.toJson(EntityRequest(Batch(2, "batch num1", true))))
         //  println(gson.toJson(EntityResponse(Batch(2, "batch num1", true), true, "fuck me")))*/


            RemoteRepo.login(LoginRequest("Admin", "Admin")).observe(this, Observer {
                println("debug: $it")
                it?.let {  UserRepo.insert(it.entity) }

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
            RemoteRepo.chooseBatch(ChooseBatchRequest(
                "14581cea-7969-478a-f515-08d80dd443ec"
            )).observe(this, Observer {
                println("debug: ChooseBatch: $it")
            })
        }

        btnFetch.setOnClickListener {
        }

        UserRepo.users.observe(this, Observer {
            println("debug: room: $it")
        })


    }


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
}