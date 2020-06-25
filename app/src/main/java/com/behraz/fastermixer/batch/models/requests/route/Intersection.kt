package com.behraz.fastermixer.batch.models.requests.route

import com.google.gson.annotations.SerializedName

data class Intersection(
    @SerializedName("bearings")
    val bearings: List<Int>,
    @SerializedName("entry")
    val entry: List<Boolean>,
    @SerializedName("in")
    val `in`: Int,
    @SerializedName("location")
    val location: List<Double>,
    @SerializedName("out")
    val `out`: Int
)