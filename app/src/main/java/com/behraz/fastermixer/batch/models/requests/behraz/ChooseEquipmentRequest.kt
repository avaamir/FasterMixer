package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

class ChooseEquipmentRequest(
    @SerializedName("id")
    val equipmentId: Int
)