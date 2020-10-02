package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherClient {
    @GET("weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): ResponseBody
}