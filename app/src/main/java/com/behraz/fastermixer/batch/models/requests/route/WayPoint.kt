package com.behraz.fastermixer.batch.models.requests.route

import com.google.gson.annotations.SerializedName

data class WayPoint(
    @SerializedName("hint")
    val hint: String,
    @SerializedName("location")
    val location: List<Double>,
    @SerializedName("name")
    val name: String
)