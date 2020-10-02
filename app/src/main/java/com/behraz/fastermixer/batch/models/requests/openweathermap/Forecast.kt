package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName


data class Forecast (
	@SerializedName("dt") val dt : Int,
	@SerializedName("main") val weatherDetails : WeatherDetails,
	@SerializedName("weather") val weather : List<Weather>,
	@SerializedName("clouds") val clouds : Clouds,
	@SerializedName("wind") val wind : Wind,
	@SerializedName("visibility") val visibility : Int,
	@SerializedName("pop") val pop : Int,
	@SerializedName("sys") val sys : Sys,
	@SerializedName("dt_txt") val dt_txt : String
)