package com.behraz.fastermixer.batch.ui.osm

import com.behraz.fastermixer.batch.R
import org.osmdroid.views.MapView

class TruckMarker(mapView : MapView, width: Int = 42, height: Int = 42): ImageMarker(R.drawable.ic_mixer, mapView, width, height)