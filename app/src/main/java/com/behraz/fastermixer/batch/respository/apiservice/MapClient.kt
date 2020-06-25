package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapClient {
/*    @GET("fast-reverse")
    suspend fun reverseGeo(
        @Query("lat") lat: String,
        @Query("lon") lng: String
    ): Response<ReverseGeo>

    @POST("search/v2/autocomplete")
    suspend fun search(@Body searchAddressRequest: SearchAddressRequest): Response<SearchAddressResponse>*/

    @GET("routes/route/v1/driving/{coordinates}")
    suspend fun getRoute(
        @Path("coordinates") coordinates: String,
        @Query("alternatives") alternative: Boolean,
        @Query("steps") steps: Boolean
    ): Response<GetRouteResponse>
}