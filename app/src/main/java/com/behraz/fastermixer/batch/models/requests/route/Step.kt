package com.behraz.fastermixer.batch.models.requests.route

import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("destinations")
    val destinations: String,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("driving_side")
    val driving_side: String,
    @SerializedName("duration")
    val duration: Double,
    @SerializedName("geometry")
    val geometry: String,
    @SerializedName("intersections")
    val intersections: List<Intersection>,
    @SerializedName("maneuver")
    val maneuver: Maneuver,
    @SerializedName("mode")
    val mode: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("rotary_name")
    val rotary_name: String,
    @SerializedName("weight")
    val weight: Double
)