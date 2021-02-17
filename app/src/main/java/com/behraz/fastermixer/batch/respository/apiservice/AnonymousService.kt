package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.BuildConfig
import com.behraz.fastermixer.batch.respository.apiservice.calladapters.ApiResultCallAdapterFactory
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.GlobalErrorHandlerInterceptor
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.NetworkConnectionInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AnonymousService {
    var companyCode = 0
        private set
    private const val DEFAULT_DOMAIN = ApiService.DEFAULT_DOMAIN
    var domain = DEFAULT_DOMAIN
        private set
    private val BASE_API_URL get() = "$domain/api/v1/"

    private var networkAvailability: NetworkConnectionInterceptor.NetworkAvailability? = null
    private lateinit var errorHandler: GlobalErrorHandlerInterceptor.ApiResponseErrorHandler

    private var token: String? = null

    private var mClient: AnonymousClient? = null

    @Synchronized
    private fun createRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            //UnsafeOkHttpClient.getUnsafeOkHttpClientBuilder()
            .apply {
                addInterceptor { chain ->
                    val builder = chain.request().newBuilder()
                        //   .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")

                    val newRequest: Request = builder.build()
                    chain.proceed(newRequest)
                }

                addInterceptor(GlobalErrorHandlerInterceptor(errorHandler))
                networkAvailability?.let {
                    addInterceptor(NetworkConnectionInterceptor(it))
                }

                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.build()

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val client: AnonymousClient
        get() = mClient ?: createRetrofit().create(AnonymousClient::class.java).also {
            mClient = it
        }


    fun init(
        networkAvailability: NetworkConnectionInterceptor.NetworkAvailability?,
        apiResponseErrorHandler: GlobalErrorHandlerInterceptor.ApiResponseErrorHandler
    ) {
        this.networkAvailability = networkAvailability
        this.errorHandler = apiResponseErrorHandler
    }

    @Synchronized
    fun setAddress(address: String, companyCode: Int) {
        if (address != domain) {
            this.companyCode = companyCode
            mClient = null
            domain = address
        }
    }
}