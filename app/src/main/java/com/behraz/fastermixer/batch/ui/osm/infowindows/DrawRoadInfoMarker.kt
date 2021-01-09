package com.behraz.fastermixer.batch.ui.osm.infowindows

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.ReportPoint
import com.behraz.fastermixer.batch.utils.general.estimateTime
import eo.view.batterymeter.BatteryMeterView
import eo.view.signalstrength.SignalStrengthView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayWithIW
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

class DrawRoadInfoMarker(mapView: MapView) :
    MarkerInfoWindow(R.layout.draw_road_info_marker, mapView) {


    @SuppressLint("SetTextI18n")
    fun setData(data: ReportPoint) {
        val tvSpeed = view.findViewById<TextView>(R.id.tvSpeed)
        val tvCarBattery = view.findViewById<TextView>(R.id.tvCarBattery)
        val tvDelay = view.findViewById<TextView>(R.id.tvDelay)
        val tvDateTime = view.findViewById<TextView>(R.id.tvDateTime)
        val viewCharge = view.findViewById<BatteryMeterView>(R.id.viewCharge)
        val viewSignal = view.findViewById<SignalStrengthView>(R.id.viewSignal)

        tvSpeed.text = "${data.speed.toInt()} Km/h"
        tvCarBattery.text = if(data.carBattery == null) "نامشخص" else "${data.carBattery} ولت"
        tvDelay.text = estimateTime(data.timeDifference.toLong()).substringBefore('پ').trim()
        tvDateTime.text = data.clientTime
        viewCharge.chargeLevel = data.charge
        viewSignal.signalLevel = data.signal
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

        if (mapView.overlays.size < 100) {
            mapView.overlays.forEach {
                if (it is Marker) {
                    if (it.isInfoWindowShown) {
                        it.closeInfoWindow()
                    }
                }
            }
        } else { //vase in ke age tedad marker ha ziad bud UI freeze nashe
            CoroutineScope(Dispatchers.Main).launch {
                mapView.overlays.forEach {
                    if (it is Marker) {
                        if (it.isInfoWindowShown) {
                            withContext(Dispatchers.Main) {
                                it.closeInfoWindow()
                            }
                        }
                    }
                }
            }
        }


    }

}