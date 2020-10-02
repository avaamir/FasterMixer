package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName


data class Coord (
	@SerializedName("lon") val lon : Double,
	@SerializedName("lat") val lat : Double
)