package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName


data class Weather (
	@SerializedName("id") val id : Int,
	@SerializedName("main") val main : String,
	@SerializedName("description") val description : String,
	@SerializedName("icon") private val icon : String
) {
	val iconURL get() = "http://openweathermap.org/img/wn/${icon}@4x.png"
}