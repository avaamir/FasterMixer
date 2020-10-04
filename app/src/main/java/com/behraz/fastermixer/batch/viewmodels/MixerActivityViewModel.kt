package com.behraz.fastermixer.batch.viewmodels

import java.util.*

class MixerActivityViewModel : VehicleActivityViewModel() {

    var mixerTimer: Timer? = null
    var mixerTimerValue = 0


    override fun onTimerTick() {
        getUserLocation(user.value!!.equipmentId!!) //get location from Car GPS
        getMission()
    }


    override fun onCleared() {
        super.onCleared()
        mixerTimer?.cancel()
        mixerTimer?.purge()
    }
}