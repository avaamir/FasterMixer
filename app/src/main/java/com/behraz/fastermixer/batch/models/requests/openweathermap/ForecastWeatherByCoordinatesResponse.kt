package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName

class ForecastWeatherByCoordinatesResponse(
    @SerializedName("cod") val cod : Int,
    @SerializedName("cnt") val cnt : Int,
    @SerializedName("message") val message : Int,
    @SerializedName("list") val forecasts : List<Forecast>,
    @SerializedName("city") val city : City
)