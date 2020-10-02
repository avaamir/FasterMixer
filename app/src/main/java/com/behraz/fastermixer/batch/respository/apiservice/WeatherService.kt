package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.BuildConfig
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.NetworkConnectionInterceptor
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherService {
    private const val Base_API_URL = "https://api.openweathermap.org/data/2.5/"
    //private val token get() = Constants.OPEN_WEATHER_MAP_ACCESS_TOKEN

    val client: WeatherClient by lazy {
        retrofitBuilder.build().create(WeatherClient::class.java)
    }

    private var internetConnectionListener: ApiService.InternetConnectionListener? = null

    private val retrofitBuilder: Retrofit.Builder by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val client: OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val currentURL = chain.request().url
                val newRequest: Request = chain.request().newBuilder()
                    //adding {token, language, Units} at end of all request urls
                    .url("$currentURL&appid=${Constants.OPEN_WEATHER_MAP_ACCESS_TOKEN}&lang=fa&units=metric")
                    .build()
                chain.proceed(newRequest)
            }
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            addInterceptor(object : NetworkConnectionInterceptor() {
                override fun isInternetAvailable(): Boolean {
                    return ApiService.isNetworkAvailable()
                }

                override fun onInternetUnavailable() {
                    CoroutineScope(Dispatchers.Main).launch {
                        internetConnectionListener?.onInternetUnavailable()
                    }
                }

            })
        }.build()

        Retrofit.Builder()
            .baseUrl(Base_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    @Synchronized
    fun setInternetConnectionListener(action: ApiService.InternetConnectionListener) {
        internetConnectionListener = action
    }

    @Synchronized
    fun removeInternetConnectionListener(action: ApiService.InternetConnectionListener) {
        if (action == internetConnectionListener) {
            internetConnectionListener = null
        }
    }

}