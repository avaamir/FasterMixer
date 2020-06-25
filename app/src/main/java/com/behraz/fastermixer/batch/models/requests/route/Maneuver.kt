package com.behraz.fastermixer.batch.models.requests.route

import com.google.gson.annotations.SerializedName

data class Maneuver(
    @SerializedName("bearing_after")
    val bearing_after: Int,
    @SerializedName("bearing_before")
    val bearing_before: Int,
    @SerializedName("exit")
    val exit: Int,
    @SerializedName("location")
    val location: List<Double>,
    @SerializedName("modifier")
    val modifier: String,
    @SerializedName("type")
    val type: String
)