package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.CheckMacResponse
import com.behraz.fastermixer.batch.models.PointInfo
import com.behraz.fastermixer.batch.models.SubmitRecordedPointInfo
import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/* this class is needed because when token expired even anonymous methods gives 403 */
interface AnonymousClient {
    @POST("Vehicles/IsMacValid/mac")
    suspend fun isMacValid(@Query("mac") mac: String): ApiResult<CheckMacResponse>

    @POST("Vehicles/SubmitPoint")
    suspend fun submitPoint(@Body pointInfo: PointInfo): ApiResult<Unit>

    @POST("Vehicles/SubmitPointList")
    suspend fun submitPoint(@Body pointInfos: SubmitRecordedPointInfo): ApiResult<Unit>
}