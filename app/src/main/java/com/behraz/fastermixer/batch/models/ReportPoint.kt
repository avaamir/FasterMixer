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
    val timeDifference : Double,

    @SerializedName("signal")
    val signal: Float?,
    @SerializedName("clientTime")
    private val _clientTime: String?,

    @SerializedName("battery")
    val battery: Float?,
) {
    val clientTime get() = _clientTime ?: "نامشخص"
    val point get() = GeoPoint(lat, lon)
}