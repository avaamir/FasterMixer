package com.behraz.fastermixer.batch.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Batch(
    @SerializedName("equipmentID")
    val id: String ,
    @SerializedName("name")
    val name: String,
    @SerializedName("isAvailable")
    val isAvailable: Boolean
) {
    val state get() = if (isAvailable) "در دسترس" else "در حال استفاده"
}