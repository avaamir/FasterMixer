package com.behraz.fastermixer.batch.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log
import android.widget.Toast


class GpsStateReceiver : BroadcastReceiver() {
    private companion object {
        var isEnabled = false
        var isNotFirstTime = false
        val mLock = Any()
    }

    var listener: OnGpsStatusChanged? = null

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val manager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (isNotFirstTime) {
                if (GpsStateReceiver.isEnabled != isEnabled) {
                    synchronized(mLock) {
                        if (GpsStateReceiver.isEnabled != isEnabled) {
                            GpsStateReceiver.isEnabled = isEnabled
                            listener?.onGpsStatusChanged(isEnabled)
                         //   println("debug:state changed: $isEnabled")
                        }
                    }
                }
            } else {
                synchronized(mLock) {
                    if (!isNotFirstTime) {
                        isNotFirstTime = true
                        GpsStateReceiver.isEnabled = isEnabled
                        listener?.onGpsStatusChanged(isEnabled)
                      //  println("debug:firstTime: state: $isEnabled")
                    }
                }
            }

        }
    }



    interface OnGpsStatusChanged {
        fun onGpsStatusChanged(isEnable: Boolean)
    }
}
