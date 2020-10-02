package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName


data class WeatherDetails (

	@SerializedName("temp") val temp : Float,
	@SerializedName("feels_like") val feels_like : Float,
	@SerializedName("temp_min") val minTemp : Float,
	@SerializedName("temp_max") val maxTemp : Float,
	@SerializedName("pressure") val pressure : Float,
	@SerializedName("humidity") val humidity : Float
)