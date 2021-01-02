package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.respository.apiservice.interceptors.NetworkConnectionInterceptor
import com.behraz.fastermixer.batch.BuildConfig
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.GlobalErrorHandlerInterceptor
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MapService {

    private lateinit var networkAvailability: NetworkConnectionInterceptor.NetworkAvailability
    private lateinit var errorHandler: GlobalErrorHandlerInterceptor.ApiResponseErrorHandler

    private const val Base_API_URL = "https://map.ir/"
    private val token get() = Constants.MAP_IR_ACCESS_TOKEN

    val client: MapClient by lazy {
        retrofitBuilder.build().create(MapClient::class.java)
    }


    private val retrofitBuilder: Retrofit.Builder by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val client: OkHttpClient = OkHttpClient.Builder().apply {
            addNetworkInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .removeHeader("Content-Type")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", token)
                    .build()
                chain.proceed(newRequest)
            }
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            addInterceptor(GlobalErrorHandlerInterceptor(errorHandler))
            addInterceptor(NetworkConnectionInterceptor(networkAvailability))
        }.build()

        Retrofit.Builder()
            .baseUrl(Base_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    fun init(
        networkAvailability: NetworkConnectionInterceptor.NetworkAvailability,
        apiResponseErrorHandler: GlobalErrorHandlerInterceptor.ApiResponseErrorHandler
    ) {
        this.networkAvailability = networkAvailability
        this.errorHandler = apiResponseErrorHandler
    }

}