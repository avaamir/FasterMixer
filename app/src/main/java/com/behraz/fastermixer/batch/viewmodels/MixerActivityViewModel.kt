package com.behraz.fastermixer.batch.viewmodels

import java.util.*

class MixerActivityViewModel : VehicleViewModel() {

    var mixerTimer: Timer? = null
    var mixerTimerValue = 0


    override fun onTimerTick() {
        user.value?.let { user ->  //TODO albate bazam momkene unauthorized bede chun shyad moghe check kardan login bashe ama bad if logout etefagh biofte, AMA jelo exception ro migire
            getUserLocation(user.equipmentId!!) //get location from Car GPS
            getMission()
        }
    }
}