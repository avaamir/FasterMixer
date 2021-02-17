package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName

open class RecordedPointInfo(
    @SerializedName("latitude")
    val lat: Double,
    @SerializedName("longitude")
    val lon: Double,
    @SerializedName("speed")
    val speed: Float,
    @SerializedName("clientTime")
    val clientTime: String,
    /*TODO add this attributes to PointInfo
    @SerializedName("signal")
    val signal: Float?,
    @SerializedName("battery")
    val battery: Float?,*/
)


class PointInfo(
    @SerializedName("macAddress")
    val mac: String,
    lat: Double,
    lon: Double,
    speed: Float,
    clientTime: String
) : RecordedPointInfo(lat, lon, speed, clientTime)



class SubmitRecordedPointInfo(
    @SerializedName("macAddress")
    val mac: String,
    @SerializedName("locationInfos")
    val pointInfos: List<RecordedPointInfo>
)
