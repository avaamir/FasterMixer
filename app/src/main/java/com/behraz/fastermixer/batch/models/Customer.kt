package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.models.requests.Fence
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Customer(
    @SerializedName("customerID")
    val id: String,
    @SerializedName("customerName")
    val name: String,
    @SerializedName("deliveryTime")
    private val _startTime: Date,
    @SerializedName("requestAddress")
    val address: String,

    @SerializedName("productTypeIslamp")
    private val _slump: Int,
    @SerializedName("productTypeCarat")
    private val _density: Int,
    @SerializedName("volume")
    private val _amount: Int,
    @SerializedName("countMixer")
    private val _mixerCount: Int, //test
    @SerializedName("geoPoint")
    private val areaStr: String,
    @SerializedName("requestType")
    private val _jobType: String?
) {
    val jobType get() = if (_jobType.isNullOrBlank()) "نامشخص" else _jobType
    val startTime: String get() = SimpleDateFormat.getTimeInstance().format(_startTime)
    val locationFence: Fence get() = Fence.strToFence(areaStr)
    val amount get() = "$_amount متر مکعب"
    val slump get() = "$_slump"
    val density get() = "$_density"
    val mixerCount get() = "$_mixerCount"
}

/*
*             "x": "مشتری پیش فرض Mehdipor",
            "x": "زارچ",
            "mobile": null,
            "productTypeIslamp": null,
            "productTypeCarat": null,
            "x": "2020-07-18T00:00:00",
            "volume": "50.00",
            "geoPoint": "CIRCLE (31.88453 54.345546, 138.0237637659366)",
            "countMixer": 16
*
* */