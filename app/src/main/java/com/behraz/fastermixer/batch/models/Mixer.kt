package com.behraz.fastermixer.batch.models

data class Mixer(
    val id: Int,
    val carName: String,
    val driverName: String,
    val phone: String,
    val carId: String,
    val state: String,
    val owner: String,
    val loadInfo: LoadInfo,
    val latLng: LatLng
)

data class LoadInfo(
    val startTime: String,
    private val _slump: Int,
    private val _density: Int,
    private val _amount: Int,
    private val _totalAmount: Int,
    private val isDelivered: Boolean
) {
    val amount get() = "$_amount متر مکعب"
    val slump get() = "$_slump"
    val density get() = "$_density"
    val amountFromTotal get() = "$_totalAmount/$_amount"
}