package com.behraz.fastermixer.batch.ui.osm.markers

import com.behraz.fastermixer.batch.ui.osm.infowindows.AdminDriverInfoWindow
import com.behraz.fastermixer.batch.ui.osm.infowindows.DriverInfoWindow
import org.osmdroid.views.MapView

open class VehicleMarker(
    drawableId: Int,
    mapView: MapView,
    width: Int = 42,
    height: Int = 42,
    private val interactions: AdminDriverInfoWindow.Interactions?
) : ImageMarker(drawableId, mapView, width, height) {
    init {
        infoWindow = if (interactions != null)
            AdminDriverInfoWindow(mapView, interactions)
        else
            DriverInfoWindow(mapView)
    }

    /*override fun getInfoWindow(): DriverInfoWindow {
        return super.getInfoWindow() as DriverInfoWindow
    }*/

    fun setPelakText(
        firstPelakText: String,
        secondText: String,
        thirdText: String,
        forthText: String
    ) {
        if (interactions != null)
            (infoWindow as AdminDriverInfoWindow).setPelakText(firstPelakText, secondText, thirdText, forthText)
        else
            (infoWindow as DriverInfoWindow).setPelakText(firstPelakText, secondText, thirdText, forthText)
    }

}