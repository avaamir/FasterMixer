package com.behraz.fastermixer.batch.utils.fastermixer

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.behraz.fastermixer.batch.app.receivers.NetworkStateReceiverLiveData
import com.behraz.fastermixer.batch.app.receivers.GpsStateReceiverLiveData

fun FragmentActivity.subscribeNetworkStateChangeListener(onStateChanged: (Boolean) -> Unit) {
    NetworkStateReceiverLiveData(this).observe(this, Observer {
        onStateChanged(it)
    })
}

fun FragmentActivity.subscribeGpsStateChangeListener(onStateChanged: (Boolean) -> Unit) {
    GpsStateReceiverLiveData(this).observe(this, Observer {
        onStateChanged(it)
    })
}