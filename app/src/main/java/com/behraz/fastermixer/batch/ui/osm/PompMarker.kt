package com.behraz.fastermixer.batch.ui.osm

import com.behraz.fastermixer.batch.R
import org.osmdroid.views.MapView

class PompMarker(mapView : MapView, width: Int = 42, height: Int = 63): ImageMarker(R.drawable.ic_pump_marker, mapView, width, height) {
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