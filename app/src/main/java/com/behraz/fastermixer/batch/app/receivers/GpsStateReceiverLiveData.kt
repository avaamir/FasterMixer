package com.behraz.fastermixer.batch.app.receivers

import android.content.Context
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.LiveData

class GpsStateReceiverLiveData(val context: Context) : LiveData<Boolean>(),
    GpsStateReceiver.OnGpsStatusChanged {

    private lateinit var gpsStateReceiver: GpsStateReceiver


    override fun onActive() {
        super.onActive()
        update()
        gpsStateReceiver = GpsStateReceiver()
        gpsStateReceiver.listener = this
        context.registerReceiver(gpsStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }


    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(gpsStateReceiver)
    }

    private fun update() {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        postValue(isEnabled)
    }

    override fun onGpsStatusChanged(isEnable: Boolean) {
        postValue(isEnable)
    }


}