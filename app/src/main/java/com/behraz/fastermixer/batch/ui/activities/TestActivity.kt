package com.behraz.fastermixer.batch.ui.activities


import android.app.DownloadManager
import android.content.*
import android.content.res.Configuration
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.respository.apiservice.WeatherService
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragmentImpl
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.subscribeSignalStrengthChangeListener
import com.behraz.fastermixer.batch.utils.general.toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File


class TestActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, MixerAdapter.BatchAdapterInteraction {


    private var counter = 0

    private val dialog: MyProgressDialog by lazy {
        MyProgressDialog(this, R.style.my_dialog_animation, true).also {
            it.show()
        }
    }

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


        if (true) {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) { //age land bud bayad baghiye code ejra beshe ta 2ta fragment ijad nashe va code be moshkel nakhore


                val mapFragment = BaseMapFragmentImpl()
                supportFragmentManager.beginTransaction()
                    .add(R.id.mapContainer, mapFragment, "base-map")
                    .commit()

                fab.setOnClickListener {
                    //mapFragment.setTileMapSource(MyMapTileSource.GoogleStandardRoadMap)
                    CoroutineScope(IO).launch {
                        val response = WeatherService.client.getCurrentWeatherByCoordinates(
                            Constants.mapStartPoint.latitude.toString(),
                            Constants.mapStartPoint.longitude.toString()
                        )
                    }
                }


            }
            return
        }


        val x = subscribeSignalStrengthChangeListener(true) {
            println("debug:signal: $it")
        }


/*
        btnLogin.setOnClickListener {
            *//*  val currentTime: Date = Calendar.getInstance().time
              println("debug: currentTime : $currentTime, Battery:$batteryLevel")

              val persianCaldroidDialog = PersianCaldroidDialog()
                  .setOnDateSetListener { dialog, date -> //do something when a date is picked
                      dialog.dismiss()
                  }
              persianCaldroidDialog.typeface = ResourcesCompat.getFont(this, R.font.iransans);
              //set a date to be selected and shown on calendar
              persianCaldroidDialog.selectedDate = (PersianDate(1396, 9, 24))
              persianCaldroidDialog.show(supportFragmentManager,  PersianCaldroidDialog::class.java.name)
  *//*
            //downloadAndUpdateApp()

            *//*
                        fun install(file: File) {
                            println("debug: ${file.exists()}:: ${file.absolutePath}")

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                if (!packageManager.canRequestPackageInstalls()) {
                                    val intent = Intent(
                                        Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                        Uri.parse("package:$packageName")
                                    )
                                    startActivityForResult(intent, 123)
                                    return
                                }
                            }


                            val mimeType = "application/vnd.android.package-archive"

                            val install = Intent(Intent.ACTION_VIEW)
                            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            // Old Approach
                            install.setDataAndType(Uri.fromFile(file), mimeType);
                            // End Old approach
                            // New Approach
                            val apkURI = FileProvider.getUriForFile(
                                this,
                                applicationContext.packageName + ".provider", file
                            )
                            install.setDataAndType(apkURI, mimeType)
                            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            // End New Approach
                            startActivity(install)
                        }*//*
            fun install(file: File) {

                println("debug:" + applicationContext.packageName + ".provider")
                println("debug:" + BuildConfig.APPLICATION_ID + ".provider")
                println("debug:" + file.absolutePath)
                if (file.exists()) {
                    val intent = Intent(Intent.ACTION_VIEW);
                    val type = "application/vnd.android.package-archive"

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val downloadedApk =
                            FileProvider.getUriForFile(
                                this,
                                applicationContext.packageName + ".provider",
                                file
                            );
                        //intent.setData(downloadedApk);
                        intent.setDataAndType(downloadedApk, type);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        intent.setDataAndType(Uri.fromFile(file), type);
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    }

                    startActivity(intent)
                } else {
                    Toast.makeText(this, "ّFile not found!", Toast.LENGTH_SHORT).show();
                }
            }


            val fileName = "AppName.apk"
            val destination: String =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/" + fileName

            install(File(destination))
        }*/

/*
        calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            println("debug: cal: $year/$month/$day -> ${JalaliCalendar.gregorianToJalali(JalaliCalendar.YearMonthDate(year, month, day))}")
        }*/


        initViews()

    }

    private fun initViews() {

    }


    private fun downloadAndUpdateApp() {
        val updateLink =
            "https://raw.githubusercontent.com/avaamir/FasterMixer/master/app/release/app-release.apk"


        //get destination to update file and set Uri
        //TODO: First I wanted to store my update .apk file on internal storage for my app but apparently android does not allow you to open and install
        //aplication with existing package from there. So for me, alternative solution is Download directory in external storage. If there is better
        //solution, please inform us in comment

        //get destination to update file and set Uri
        //TODO: First I wanted to store my update .apk file on internal storage for my app but apparently android does not allow you to open and install
        //aplication with existing package from there. So for me, alternative solution is Download directory in external storage. If there is better
        //solution, please inform us in comment
        var destination: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/"

        val fileName = "AppName.apk"
        destination += fileName
        val uri: Uri = Uri.parse("file://$destination")

        //Delete update file if exists

        //Delete update file if exists
        val file = File(destination)
        if (file.exists()) //file.delete() - test this, I think sometimes it doesnt work
            file.delete()

        //get url of app on server

        //get url of app on server
        val url: String = updateLink

        //set downloadmanager

        //set downloadmanager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription("Downloading New Update")
        request.setTitle(getString(R.string.app_name))

        //set destination

        //set destination
        request.setDestinationUri(uri)

        // get download service and enqueue file

        // get download service and enqueue file
        val manager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)

        //set BroadcastReceiver to install app when .apk is downloaded

        //set BroadcastReceiver to install app when .apk is downloaded
        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context?, intent: Intent?) {
                val install = Intent(Intent.ACTION_VIEW)
                install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                install.setDataAndType(
                    uri,
                    manager.getMimeTypeForDownloadedFile(downloadId)
                )
                startActivity(install)
                unregisterReceiver(this)
                finish()
            }
        }
        //register receiver for when .apk download is compete
        //register receiver for when .apk download is compete
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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