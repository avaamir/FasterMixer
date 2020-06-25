package com.behraz.fastermixer.batch.ui.osm

import com.behraz.fastermixer.batch.R
import org.osmdroid.views.MapView

class SimpleMarker(mapView : MapView, width: Int = 42, height: Int = 42): ImageMarker(R.drawable.ic_marker, mapView, width, height)