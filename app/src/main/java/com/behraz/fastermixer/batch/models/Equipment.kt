package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName

open class Equipment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("selected")
    private val isOccupied: Boolean  // this is for choose equipment
) {
    val isAvailable get() = !isOccupied
    val availabilityMessage get() = if (isAvailable) "در دسترس" else "در حال استفاده"
}