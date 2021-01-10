package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName

data class FullReport(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val carName: String,
    @SerializedName("state") val state: String,
    @SerializedName("periodTime") val duration: String,
    @SerializedName("maxSpeed") val maxSpeed: String,
    @SerializedName("avrSpeed") val aveSpeed: String,
    @SerializedName("traveledDistance") val distance: String,
    @SerializedName("date") val date: String,
    @SerializedName("startTime") private  val _startTime: String,
    @SerializedName("endTime") private val _endTime: String
) {
    val time get() = "$_startTime تا $_endTime"
}