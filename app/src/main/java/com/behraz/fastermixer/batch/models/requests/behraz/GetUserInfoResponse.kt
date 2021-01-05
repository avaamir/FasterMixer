package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

data class GetUserInfoResponse(
    @SerializedName("userId")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("personalCode")
    val personalCode: String?,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("equipmentId")
    val equipmentId: Int?
)