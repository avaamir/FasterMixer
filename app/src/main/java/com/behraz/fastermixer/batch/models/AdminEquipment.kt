package com.behraz.fastermixer.batch.models

import android.os.Parcelable
import com.behraz.fastermixer.batch.models.enums.EquipmentState
import com.behraz.fastermixer.batch.models.enums.EquipmentType
import com.behraz.fastermixer.batch.models.requests.toGeoPoint
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.osmdroid.util.GeoPoint
import java.util.*

@Parcelize
data class AdminEquipment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("plaque")
    val carIdStr: String,
    @SerializedName("corrupted")
    val isDamaged: Boolean,
    @SerializedName("inputLastDataSpeed")
    val speed: Float,
    @SerializedName("inputLastDataBattery")
    val battery: Float,
    @SerializedName("inputLastDataIgnition")
    val ignition: Boolean,
    @SerializedName("inputLastDataMaxSpeed")
    val maxSpeed: Float,
    @SerializedName("userFullName")
    val driverName: String?,
    @SerializedName("timeDifference")
    val lastDataTimeDiff: Double,
    @SerializedName("inputLastDataClientTime")
    val dateTime: Date?,

    @SerializedName("vehicleTypeIsPump")
    private val isPomp: Boolean,
    @SerializedName("vehicleTypeIsMixer")
    private val isMixer: Boolean,
    @SerializedName("inputLastDataLatitude")
    private val lat: Double,
    @SerializedName("inputLastDataLongitude")
    private val lon: Double,
) : Parcelable {
    val location get() = GeoPoint(lat, lon)

    val type: EquipmentType
        get() = when {
            isMixer -> EquipmentType.Mixer
            isPomp -> EquipmentType.Pomp
            name.contains("لودر") -> EquipmentType.Loader
            else -> EquipmentType.Other
        }.exhaustiveAsExpression()

    val state
        get() = when {
            isDamaged -> EquipmentState.Fixing
            !ignition -> EquipmentState.Off
            ignition -> EquipmentState.Using
            else -> EquipmentState.Other
        }
}