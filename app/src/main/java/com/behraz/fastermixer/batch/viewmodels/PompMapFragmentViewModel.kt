package com.behraz.fastermixer.batch.viewmodels

import com.behraz.fastermixer.batch.ui.osm.markers.ImageMarker


class PompMapFragmentViewModel : VehicleMapFragmentViewModel() {
    val markers = HashMap<String, ImageMarker>()
}

