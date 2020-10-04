package com.behraz.fastermixer.batch.models.requests.behraz

import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.models.requests.Fence
import com.google.gson.annotations.SerializedName
import java.util.*

class GetBatchLocationResponse(
    // private val equipmentID: String,
    // private val geofenceID: String,
    @SerializedName("geofencepoint")
    private val strFence: String
) {
    val equipmentLocation: Fence
        get() = Fence.strToFence(strFence)
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
    val circleFence: CircleFence get() = CircleFence.strToCircleFence(locationStr)
}