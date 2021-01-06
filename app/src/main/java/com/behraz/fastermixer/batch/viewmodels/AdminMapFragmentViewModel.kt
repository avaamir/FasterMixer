package com.behraz.fastermixer.batch.viewmodels

import com.behraz.fastermixer.batch.ui.osm.markers.VehicleMarker

class AdminMapFragmentViewModel : VehicleMapFragmentViewModel() {
    val markers = HashMap<Int, VehicleMarker>()
}