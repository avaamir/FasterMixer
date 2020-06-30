package com.behraz.fastermixer.batch.models.requests.behraz

import android.location.Location
import com.google.gson.annotations.SerializedName

class UpdateLocationRequest( //this is for pomp and mixer
    location: Location,
    @SerializedName("")
    val dateTime: String,
    @SerializedName("")
    val lastSpeed: String?,
    @SerializedName("")
    val aveSpeed: String?,
    @SerializedName("")
    val signalStrength: String,
    @SerializedName("")
    val bearing: String?,
    @SerializedName("")
    val battery: String
) {
    @SerializedName("")
    val location = "point(${location.latitude}, ${location.longitude}, 4326)"
}