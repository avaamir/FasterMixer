package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.enums.EquipmentState
import com.behraz.fastermixer.batch.models.enums.EquipmentType
import com.behraz.fastermixer.batch.models.requests.toGeoPoint
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression
import com.google.gson.annotations.SerializedName
import java.util.*

data class AdminEquipment(
    @SerializedName("vehicleID")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("vehicleTypeName")
    val typeName: String,
    @SerializedName("plaque")
    val carIdStr: String,
    @SerializedName("isDamaged")
    val isDamaged: Boolean,
    /*@SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,*/
    @SerializedName("inputLastDataLocation")
    private val inputLastDataLocation: String?,
    @SerializedName("inputLastDataClientDatetime")
    val dateTime: Date?,
    @SerializedName("inputLastDataIgnition")
    val ignition: Boolean
) {

    val location get() = inputLastDataLocation?.toGeoPoint()

    val type: EquipmentType
        get() = when {
            typeName.contains("میکسر") -> EquipmentType.Mixer
            typeName.contains("لودر") -> EquipmentType.Loader
            typeName.contains("پمپ") -> EquipmentType.Pomp
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