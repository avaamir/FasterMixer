package com.behraz.fastermixer.batch.utils.general.hardware.compass

import androidx.lifecycle.LiveData
import java.util.*
import kotlin.concurrent.fixedRateTimer

class TimerLiveData(private val interval: Long) : LiveData<Unit>() {

    private lateinit var timer: Timer

    override fun onActive() {
        super.onActive()
        timer = fixedRateTimer(period = interval) {
            postValue(kotlin.Unit)
        }
    }

    override fun onInactive() {
        super.onInactive()
        timer.cancel()
        timer.purge()
    }
}