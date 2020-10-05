package com.behraz.fastermixer.batch.viewmodels

import com.behraz.fastermixer.batch.models.User
import java.util.*

class MixerActivityViewModel : VehicleActivityViewModel() {

    var mixerTimer: Timer? = null
    var mixerTimerValue = 0


    override fun onTimerTick(user: User) {
        getUserLocation(user.equipmentId!!) //get location from Car GPS
        getMission()
    }


    override fun onCleared() {
        super.onCleared()
        mixerTimer?.cancel()
        mixerTimer?.purge()
    }
}