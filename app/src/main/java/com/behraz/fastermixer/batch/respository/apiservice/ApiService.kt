package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.BuildConfig
import com.behraz.fastermixer.batch.respository.apiservice.calladapters.ApiResultCallAdapterFactory
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.AuthCookieInterceptor
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.EmptyBodyInterceptor
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.GlobalErrorHandlerInterceptor
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.NetworkConnectionInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiService {
    const val Domain = "http://78.39.159.41:9001"
    private const val BASE_API_URL = "$Domain/api/v1/"

    private lateinit var networkAvailability: NetworkConnectionInterceptor.NetworkAvailability
    private lateinit var errorHandler: GlobalErrorHandlerInterceptor.ApiResponseErrorHandler

    private var token: String? = null

    val client: BehrazClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        retrofitBuilder.build().create(BehrazClient::class.java)
    }


    private val retrofitBuilder: Retrofit.Builder by lazy {

        val client: OkHttpClient = OkHttpClient.Builder()
            //UnsafeOkHttpClient.getUnsafeOkHttpClientBuilder()
            .apply {
                addInterceptor { chain ->
                    val builder = chain.request().newBuilder()
                        //   .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")

                    token?.let { token ->
                        builder.addHeader("Authorization", "Bearer $token")
                    }
                    val newRequest: Request = builder.build()
                    chain.proceed(newRequest)
                }

                addInterceptor(AuthCookieInterceptor())
                addInterceptor(GlobalErrorHandlerInterceptor(errorHandler))
                addInterceptor(NetworkConnectionInterceptor(networkAvailability))
                addInterceptor(EmptyBodyInterceptor())

                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.build()

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }

    fun init(
        networkAvailability: NetworkConnectionInterceptor.NetworkAvailability,
        apiResponseErrorHandler: GlobalErrorHandlerInterceptor.ApiResponseErrorHandler
    ) {
        this.networkAvailability = networkAvailability
        this.errorHandler = apiResponseErrorHandler
    }

    @Synchronized
    fun setToken(token: String?) {
        ApiService.token = token
    }
}