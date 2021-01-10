package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName

data class SummeryReport(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val carName: String,
    @SerializedName("timeInMove") val onMove: String,
    @SerializedName("timeOnStop") val onStop: String,
    @SerializedName("offTime") val onOff: String,
    @SerializedName("traveledDistance") val distance: String,
    @SerializedName("maxSpeed") val maxSpeed: String,
    @SerializedName("avrSpeed") val aveSpeed: String,
)