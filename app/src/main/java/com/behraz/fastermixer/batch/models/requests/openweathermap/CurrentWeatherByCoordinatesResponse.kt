package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName

class CurrentWeatherByCoordinatesResponse(
    @SerializedName("coord") private val coordinates : Coord,
    @SerializedName("weather") private  val weather : List<Weather>,
    @SerializedName("main") private  val weatherDetails : WeatherDetails,
    @SerializedName("wind") private  val wind : Wind,
    @SerializedName("clouds") private  val clouds : Clouds,
    @SerializedName("dt") private  val dateTimeTimeStamp : Long,
    @SerializedName("sys") private  val sys : Sys,
    @SerializedName("timezone") private  val timezone : Int,
    @SerializedName("id") private  val id : Int,
    @SerializedName("name") val cityName : String,
    @SerializedName("cod") val cityCode : Int
) {
    val description get() = weather[0].description
    val iconURL get() = weather[0].iconURL
    val temp get() = weatherDetails.temp
    val maxTemp get() = weatherDetails.maxTemp
    val minTemp get() = weatherDetails.minTemp
    val pressure get() = weatherDetails.pressure
    val humidity get() = weatherDetails.humidity
    val windSpeed get() = wind.speed
    val windDegree get() = wind.degree
}