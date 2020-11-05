package com.behraz.fastermixer.batch.ui.osm.markers

import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.ui.osm.markers.ImageMarker
import org.osmdroid.views.MapView

class DestMarker(mapView : MapView, width: Int = 42, height: Int = 63): ImageMarker(R.drawable.ic_marker, mapView, width, height)