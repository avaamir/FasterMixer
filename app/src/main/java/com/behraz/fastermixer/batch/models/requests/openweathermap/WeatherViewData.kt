package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.behraz.fastermixer.batch.utils.general.JalaliCalendar
import com.behraz.fastermixer.batch.utils.general.getDateFromTimestamp

data class WeatherViewData(
    val description: String,
    private val icon: String,
    private val _temp: String,
    private val _maxTemp: String,
    private val _minTemp: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windDegree: String,
    private val timestamp: Long
) {
    val iconURL get() = "http://openweathermap.org/img/wn/${icon}@2x.png"
    val date: String get() = "not yet implemented"
    val time: String get() = "not yet implemented"

    val temp get() = "$_temp°C"
    val maxTemp get() = "${_maxTemp}°C"
    val minTemp get() = "${_minTemp}°C"


    constructor(current: CurrentWeatherByCoordinatesResponse) : this(
        current.weather[0].description,
        current.weather[0].icon,
        current.weatherDetails.temp.toString(),
        current.weatherDetails.maxTemp.toString(),
        current.weatherDetails.minTemp.toString(),
        current.weatherDetails.pressure.toString(),
        current.weatherDetails.humidity.toString(),
        current.wind.speed.toString(),
        current.wind.degree.toString(),
        current.timestamp
    ) {
    }

    constructor(forecast: Forecast) : this(
        forecast.weather[0].description,
        forecast.weather[0].icon,
        forecast.weatherDetails.temp.toString(),
        forecast.weatherDetails.maxTemp.toString(),
        forecast.weatherDetails.minTemp.toString(),
        forecast.weatherDetails.pressure.toString(),
        forecast.weatherDetails.humidity.toString(),
        forecast.wind.speed.toString(),
        forecast.wind.degree.toString(),
        forecast.timestamp
    )
}