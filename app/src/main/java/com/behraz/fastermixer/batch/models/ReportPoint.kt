package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint

data class ReportPoint(
    @SerializedName("latitude")
    private val lat: Double,
    @SerializedName("longitude")
    private val lon: Double,
    @SerializedName("speed")
    val speed: Float,
    @SerializedName("ignition")
    val ignition: Boolean,
    @SerializedName("timeDifference")
    val timeDifference : Long,

    @SerializedName("battery")
    val battery: Float?,
) {
    val point get() = GeoPoint(lat, lon)
}