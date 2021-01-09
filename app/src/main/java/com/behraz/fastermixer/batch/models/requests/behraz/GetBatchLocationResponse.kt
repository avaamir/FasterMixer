package com.behraz.fastermixer.batch.models.requests.behraz

import android.location.Location
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.utils.general.now
import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint
import java.util.*

class GetVehicleLocationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("inputLastDataLatitude")
    private val lat: Double,
    @SerializedName("inputLastDataLongitude")
    private val lon: Double,
    @SerializedName("corrupted")
    val isDamaged: Boolean,
    @SerializedName("inputLastDataClientTime")
    val dateTime: Date,
    @SerializedName("inputLastDataIgnition")
    val ignition: Boolean
) {
    val location: GeoPoint get() = GeoPoint(lat, lon)


    companion object {
        fun create(location: Location, isDamaged: Boolean) = GetVehicleLocationResponse(
            0,
            location.latitude,
            location.longitude,
            isDamaged,
            now(),
            false
        )
    }


}