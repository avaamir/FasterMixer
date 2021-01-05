package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName
import java.util.*

open class Equipment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("selected")
    val isAvailable: Boolean  // this is for choose equipment
) {
    val availabilityMessage get() = if (isAvailable) "در دسترس" else "در حال استفاده"
}