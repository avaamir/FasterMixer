package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

data class GetUserInfoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("fullName")
    val name: String,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("role")
    val role: String,
    @SerializedName("roleId")
    val roleId: Int, //TODO not yet implemented server side
    @SerializedName("address")
    val address: String?,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("userName")
    val username: String?,
    @SerializedName("equipmentId")
    val equipmentId: Int //TODO not yet implemented server side
) {
}