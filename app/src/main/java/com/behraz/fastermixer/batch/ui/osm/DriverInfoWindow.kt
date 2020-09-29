package com.behraz.fastermixer.batch.ui.osm

import android.view.View
import android.widget.TextView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.ui.customs.fastermixer.CarIdView
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayWithIW
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow


class DriverInfoWindow(
    mapView: MapView
) : MarkerInfoWindow(R.layout.my_map_info, mapView) {

    fun setPelakText(
        firstPelakText: String,
        secondText: String,
        thirdText: String,
        forthText: String
    ) {
        val carIdView: CarIdView = mView.findViewById(R.id.pelakView)
        if (firstPelakText.isBlank())
            carIdView.visibility = View.GONE
        else {
            carIdView.visibility = View.VISIBLE
            carIdView.setText(firstPelakText, secondText, thirdText, forthText)
        }
    }

    fun shouldShowCadIdView(shouldShow: Boolean) {
        val carIdView: CarIdView = mView.findViewById(R.id.pelakView)
        if (shouldShow) {
            carIdView.visibility = View.VISIBLE
        } else {
            carIdView.visibility = View.GONE
        }
    }

    override fun onOpen(item: Any) {
        super.onOpen(item)
        val overlay = item as OverlayWithIW
        val title = overlay.title
        val tvTitle = mView.findViewById<TextView>(R.id.bubble_title)
        if (title.isNullOrBlank()) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            //Title dar super.onOpen set mishe va niazi nist dg setesh konam
        }

        mapView.overlays.forEach {
            if (it is Marker) {
                if (it.isInfoWindowShown) {
                    it.closeInfoWindow()
                }
            }
        }


    }
}