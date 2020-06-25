package com.behraz.fastermixer.batch.models

data class Customer(
    val id: Int,
    val name: String,
    val startTime: String,
    val address: String,
    private val _slump: Int,
    private val _density: Int,
    private val _amount: Int,
    private val _mixerCount: Int, //test
    val jobType: String,
    val area: List<LatLng> //mahdude moshtari
) {
    val amount get() = "$_amount متر مکعب"
    val slump get() = "$_slump"
    val density get() = "$_density"
    val mixerCount get() = "$_mixerCount"
}