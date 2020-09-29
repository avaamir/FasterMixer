package com.behraz.fastermixer.batch.ui.osm

import com.behraz.fastermixer.batch.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MixerMarker(mapView : MapView, width: Int = 42, height: Int = 63): ImageMarker(R.drawable.ic_mixer_marker, mapView, width, height) {

    init {
        infoWindow = DriverInfoWindow(mapView)
    }

    override fun getInfoWindow(): DriverInfoWindow {
        return super.getInfoWindow() as DriverInfoWindow
    }

    fun setPelakText(
        firstPelakText: String,
        secondText: String,
        thirdText: String,
        forthText: String
    ) {
        infoWindow.setPelakText(firstPelakText, secondText, thirdText, forthText)
    }
}