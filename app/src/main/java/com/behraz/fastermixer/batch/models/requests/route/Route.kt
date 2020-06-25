package com.behraz.fastermixer.batch.models.requests.route

import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("duration")
    val duration: Double,
    @SerializedName("legs")
    val legs: List<Leg>,
    @SerializedName("weight")
    val weight: Double,
    @SerializedName("weight_name")
    val weight_name: String
)