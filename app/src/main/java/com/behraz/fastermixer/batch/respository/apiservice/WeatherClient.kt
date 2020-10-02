package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.requests.openweathermap.CurrentWeatherByCoordinatesResponse
import com.behraz.fastermixer.batch.models.requests.openweathermap.ForecastWeatherByCoordinatesResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherClient {
    @GET("weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Response<CurrentWeatherByCoordinatesResponse>


    @GET("forecast")
    suspend fun getForecastWeatherByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Response<ForecastWeatherByCoordinatesResponse>
}