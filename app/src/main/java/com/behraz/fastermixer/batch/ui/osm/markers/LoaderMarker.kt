package com.behraz.fastermixer.batch.ui.osm.markers

import com.behraz.fastermixer.batch.R
import org.osmdroid.views.MapView

class LoaderMarker(mapView : MapView, width: Int = 42, height: Int = 63): VehicleMarker(R.drawable.ic_loader, mapView, width, height)