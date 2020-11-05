package com.behraz.fastermixer.batch.ui.osm.markers

import com.behraz.fastermixer.batch.ui.osm.infowindows.DriverInfoWindow
import org.osmdroid.views.MapView

open class VehicleMarker(drawableId: Int, mapView: MapView, width: Int = 42, height: Int = 42) : ImageMarker(drawableId, mapView, width, height) {
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