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
    val timeDifference: Double,

    @SerializedName("signal")
    private val _signal: Float?,
    @SerializedName("clientTime")
    private val _clientTime: String?,

    @SerializedName("power")
    private val _battery: Float?,

    @SerializedName("battery")
    private val _charge: Float?,
) {
    val clientTime get() = _clientTime ?: "نامشخص"
    val point get() = GeoPoint(lat, lon)

    val carBattery get() = _battery?.toInt()
    val charge get() = (_charge?.div(5))?.times(100)?.toInt() ?: 0
    val signal get() = (_signal?.div(4))?.times(100)?.toInt() ?: 0


}