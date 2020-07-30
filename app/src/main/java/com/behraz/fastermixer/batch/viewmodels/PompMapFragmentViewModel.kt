package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class PompMapFragmentViewModel : ViewModel() {
    var myLocation =
        GeoPoint(31.891413345001638, 54.35357135720551)

    val markers =  HashMap<String, Marker>()
}

