package com.behraz.fastermixer.batch.app.receivers

import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import androidx.lifecycle.LiveData

class SignalStrengthReceiverLiveData(val context: Context) : LiveData<Int>() {

    private lateinit var mTelephonyManager: TelephonyManager
    private val mPhoneStateListener = object : PhoneStateListener() {
        override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
            super.onSignalStrengthsChanged(signalStrength)
            var mSignalStrength = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (signalStrength.cellSignalStrengths.isNotEmpty())
                    signalStrength.cellSignalStrengths[0].asuLevel //TODO check this match with  buildVersion < Q
                else
                    0
            } else {
                signalStrength.gsmSignalStrength
            }
            mSignalStrength = 2 * mSignalStrength - 113 // -> dBm
            postValue(mSignalStrength)
            println("debug: SignalStrength: $mSignalStrength")
        }
    }


    override fun onActive() {
        super.onActive()
        mTelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
    }

}