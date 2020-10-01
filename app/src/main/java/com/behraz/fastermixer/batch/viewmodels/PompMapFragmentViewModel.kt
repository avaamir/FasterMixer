package com.behraz.fastermixer.batch.viewmodels

import org.osmdroid.views.overlay.Marker


class PompMapFragmentViewModel : VehicleMapFragmentViewModel() {
    val markers = HashMap<String, Marker>()
}

