package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName

class CurrentWeatherByCoordinatesResponse(
    @SerializedName("coord")   val coordinates: Coord,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("main")   val weatherDetails: WeatherDetails,
    @SerializedName("wind")   val wind: Wind,
    @SerializedName("clouds")   val clouds: Clouds,
    @SerializedName("dt")   val timestamp: Long,
    @SerializedName("sys")   val sys: Sys,
    @SerializedName("timezone")   val timezone: Int,
    @SerializedName("id")   val id: Int,
    @SerializedName("name") val cityName: String,
    @SerializedName("cod") val cityCode: Int
)

