package com.behraz.fastermixer.batch.models.requests.behraz

import com.google.gson.annotations.SerializedName

class UpdateResponse(
    @SerializedName("appVersionID")
    private val appVersionID: String,
    @SerializedName("version")
    val version: Long,
    @SerializedName("link")
    val link: String,
    @SerializedName("fource")
    val isForce: Boolean,
    @SerializedName("description")
    val description: String
)