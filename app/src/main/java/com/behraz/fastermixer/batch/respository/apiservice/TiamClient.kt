package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.behraz.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface TiamClient {
    //First Page
    @POST("AppVersion/FindLastAppVersion")
    suspend fun checkUpdates(): ApiResult<UpdateResponse>

    @POST("Users/loginApp")
    suspend fun getToken(@Body loginRequest: LoginRequest): Response<GetTokenResponse>

    @POST("Users/GetUserInfoForApp")
    suspend fun getUserInfo(): ApiResult<GetUserInfoResponse>

    //========================================
    //ParentViewModel
    @POST("Users/LogOut")
    suspend fun logout(): ApiResult<Unit>

    @POST("Messages/GetAllMessageForApp")
    suspend fun getMessages(): ApiResult<List<MessageDto>>

    @POST("Messages/SetSeenForApp")
    suspend fun seenMessage(@Body ids: List<Int>): ApiResult<Unit>

    @POST("Breakdowns/Create")
    suspend fun insertBreakdown(@Body request: BreakdownRequest): ApiResult<Unit>

    //=========================================
    //VehicleViewModel
    @POST("Vehicles/FindLocationById/{id}")
    suspend fun getVehicleLocation(@Path("id") id: Int): ApiResult<GetVehicleLocationResponse>

    @POST("Vehicles/GetMissionForMixer") //mixer
    suspend fun getMixerMission(): ApiResult<Mission>

    @POST("Vehicles/GetMissionForPump") //pomp
    suspend fun getPompMission(): ApiResult<Mission>

    //=========================================
    //Pomp
    @POST("Plannings/GetAllPlanServicesForPump")
    suspend fun getPompMixers(): ApiResult<List<Mixer>>

    @POST("Vehicles/GetAllMixerForPump")
    suspend fun getAllMixers(): ApiResult<List<Mixer>>

    @POST("Plannings/GetAllRequestForPump")
    suspend fun getCustomers(): ApiResult<List<Customer>>

    //=========================================
    //Batch
    @POST("Equipments/GetBatchLocation/{id}")
    suspend fun getBatchLocation(@Path("id") id: Int): ApiResult<String>

    @POST("Equipments/GetAllBacth")
    suspend fun getBatches(): ApiResult<List<Batch>>

    @POST("Equipments/ChooseBatch/{id}")
    suspend fun chooseBatch(@Path("id") id: Int): ApiResult<Unit>

    //TODO not migrated (not tested actually)
    @POST("Plannings/GetAllPlanServicesForBatch")
    suspend fun getBatchMixers(): ApiResult<List<Mixer>>

    //==========================================
    //Admin
    @POST("Messages/GetAllMessageForAdmin")
    suspend fun getAdminMessages(): ApiResult<List<AdminMessageDto>>

    @POST("Vehicles/GetAll")
    suspend fun getEquipmentsForAdmin(): ApiResult<List<AdminEquipment>>

    @POST("Request/GetRequestNotEnded")
    suspend fun getPlansForAdmin(): ApiResult<List<Plan>>

    @POST("Plannings/GetActivePlaningServiceForRequest/{requestId}")
    suspend fun getActiveServices(@Path("requestId") requestId: Int): ApiResult<List<Service>>

    @POST("Plannings/GetPlaningServiceForVehicle/{requestId}/{vehicleId}")
    suspend fun getServiceHistory(
        @Path("vehicleId") vehicleId: Int,
        @Path("requestId") requestId: Int
    ): ApiResult<List<Service>>


    @POST("Reports/DrawThePathForApp")
    suspend fun getDrawRoadReport(@Body getReportDrawRoadRequest: GetReportRequest.DrawRoadRequest): ApiResult<List<ReportPoint>>

    @POST("Reports/Reports/getFullReportForApp")
    suspend fun getFullReport(@Body request: GetReportRequest.FullReportRequest): ApiResult<List<FullReport>>

    @POST("Reports/getSummeryReportForApp")
    suspend fun getSummeryReport(@Body request: GetReportRequest.SummeryReportRequest): ApiResult<List<SummeryReport>>


    //======================================


    //TODO hazf shode az scenario
    @POST("Equipment/GetPomp")
    suspend fun getPomps(): ApiResult<List<Pomp>>

    @Multipart
    @POST("SendMessage/not_implemented")                           //todo not implemented server side check URL
    suspend fun sendVoiceMessage(@Part voice: MultipartBody.Part): ApiResult<Unit>
}

