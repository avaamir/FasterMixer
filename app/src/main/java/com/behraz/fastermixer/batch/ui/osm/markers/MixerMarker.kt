package com.behraz.fastermixer.batch.ui.osm.markers

import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.ui.osm.infowindows.AdminDriverInfoWindow
import org.osmdroid.views.MapView

class MixerMarker(
    mapView: MapView,
    width: Int = 42,
    height: Int = 63,
    interactions: AdminDriverInfoWindow.Interactions? = null
) : VehicleMarker(
    R.drawable.ic_mixer_marker, mapView, width, height, interactions
)