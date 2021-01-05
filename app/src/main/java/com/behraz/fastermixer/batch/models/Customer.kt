package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.requests.Fence
import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("customerId")
    val id: String,
    @SerializedName("customerName")
    val name: String,
    @SerializedName("startedTime")
    val startTime: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("islamp")
    val slump: Int,
    @SerializedName("productTypeCarat")
    private val _density: Int,
    @SerializedName("value")
    private val _amount: Float,
    @SerializedName("mixerCount")
    private val _mixerCount: Int, //test
    @SerializedName("geofencePoint")
    private val areaStr: String,
    @SerializedName("isStarted")
    val isStarted: Boolean,

    @Transient
    var isSelected: Boolean
) {
    val locationFence: Fence get() = Fence.strToFence(areaStr)
    val amount get() = "$_amount متر مکعب"
    val density get() = "$_density"
    val mixerCount get() = "$_mixerCount"
}