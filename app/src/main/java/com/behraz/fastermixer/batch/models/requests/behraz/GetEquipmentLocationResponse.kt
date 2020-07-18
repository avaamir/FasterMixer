package com.behraz.fastermixer.batch.models.requests.behraz

import com.behraz.fastermixer.batch.utils.general.circleFenceToCenterGeoPoint
import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint

class GetEquipmentLocationResponse(
    // private val equipmentID: String,
    // private val geofenceID: String,
    private val geofencepoint: String
) {
    val equipmentLocation: GeoPoint
        get() = circleFenceToCenterGeoPoint(geofencepoint)
}

class GetEquipmentRequest(@SerializedName("id") val id: String)
