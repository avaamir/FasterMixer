package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName
import java.util.*

open class Equipment(
    @SerializedName("equipmentID")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("isAvailable")
    val isAvailable: Boolean  // this is for choose equipment
) {
    val availabilityMessage get() = if (isAvailable) "در دسترس" else "در حال استفاده"
}