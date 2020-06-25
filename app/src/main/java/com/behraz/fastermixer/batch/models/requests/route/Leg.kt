package com.behraz.fastermixer.batch.models.requests.route

import com.google.gson.annotations.SerializedName

data class Leg(
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("duration")
    val duration: Double,
    @SerializedName("steps")
    val steps: List<Step>,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("weight")
    val weight: Double
)