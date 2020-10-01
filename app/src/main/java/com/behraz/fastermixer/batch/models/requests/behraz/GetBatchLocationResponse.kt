package com.behraz.fastermixer.batch.models.requests.behraz

import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint
import java.util.*

class GetBatchLocationResponse(
    // private val equipmentID: String,
    // private val geofenceID: String,
    private val geofencepoint: String
) {
    val equipmentLocation: CircleFence
        get() = CircleFence.circleFenceToCenterGeoPoint(geofencepoint, null)
}

class GetEquipmentRequest(@SerializedName("id") val id: String)

class GetVehicleLocationResponse (
    @SerializedName("vehicleID")
    val id: String,
    @SerializedName("location")
    private val locationStr: String,
    @SerializedName("course")
    val bearing: Float,
    @SerializedName("azimuth")
    val azimuth: String,
    @SerializedName("battery")
    val battery: Float,
    @SerializedName("ignition")
    val ignition: Boolean,
    @SerializedName("clientDatetime")
    val dateTime: Date
) {
    val location: CircleFence get() = CircleFence.circleFenceToCenterGeoPoint(locationStr, dateTime)
}