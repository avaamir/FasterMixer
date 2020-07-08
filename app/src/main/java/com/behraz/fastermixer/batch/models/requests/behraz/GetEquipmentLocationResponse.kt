package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint

class GetEquipmentLocationResponse(
    // private val equipmentID: String,
    // private val geofenceID: String,
    private val geofencepoint: String
) {
    val equipmentLocation: GeoPoint
        get() = geofencepoint.substring(8, geofencepoint.indexOf(",")).split(" ").let {
            println("debug:GeoPoint: $it")
            GeoPoint(it[0].toDouble(), it[1].toDouble())
        }
}


class GetEquipmentRequest(@SerializedName("id") val id: String)