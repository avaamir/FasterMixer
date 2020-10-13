package com.behraz.fastermixer.batch.models.requests.behraz

import android.location.Location
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.utils.general.now
import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint
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

class GetVehicleLocationResponse(
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
    val dateTime: Date,
    private val _location: GeoPoint? = null
) {
    val location: GeoPoint get() = _location ?: CircleFence.strToCircleFence(locationStr).center


    companion object {
        fun create(location: Location) = GetVehicleLocationResponse(
            "0",
            "",
            location.bearing,
            location.altitude.toString(),
            0f,
            false,
            now(),
            GeoPoint(location)
        )
    }


}