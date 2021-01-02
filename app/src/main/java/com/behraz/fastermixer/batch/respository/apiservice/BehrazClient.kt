package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.behraz.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface BehrazClient {
    @POST("AppVersion/FindLastAppVersion")
    suspend fun checkUpdates(): ApiResult<UpdateResponse>


    @POST("Users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


    @POST("Users/GetUserInfo")
    suspend fun getUserInfo(): ApiResult<GetUserInfoResponse>






    @POST("Person/LogOut")
    suspend fun logout(): Response<ApiResult<Unit>>

    @POST("Equipment/GetBatch")
    suspend fun getBatches(): Response<ApiResult<List<Batch>>>

    @POST("Equipment/ChoseEquipment")
    suspend fun chooseBatch(@Body chooseEquipmentRequest: ChooseEquipmentRequest): Response<ApiResult<Unit>>

    @POST("Planning/FindAllPlanningForBatch")
    suspend fun getBatchMixers(): Response<ApiResult<List<Mixer>>>

    //TODO hazf shode az scenario
    @POST("Equipment/GetPomp")
    suspend fun getPomps(): Response<ApiResult<List<Pomp>>>
    //TODO hazf shode az scenario
    @POST("Equipment/ChosePomp")//todo not implemented server side check URL
    suspend fun choosePomp(@Body chooseEquipmentRequest: ChooseEquipmentRequest): Response<ApiResult<Unit>>

    @Multipart
    @POST("SendMessage/not_implemented")                           //todo not implemented server side check URL
    suspend fun sendVoiceMessage(@Part voice: MultipartBody.Part): Response<ApiResult<Unit>>

    @POST("SendMessage/FindAllSendMessageByReceiverId")
    suspend fun getMessages(): Response<ApiResult<List<Message>>>

    @POST("Planning/FindAllPlanningForPump")
    suspend fun getPompMixers(): Response<ApiResult<List<Mixer>>>

    @POST("Vehicle/FindAllMixerForPump")
    suspend fun getAllMixers(): Response<ApiResult<List<Mixer>>>

    @POST("Equipment/FindLocationBatch")
    suspend fun getBatchLocation(@Body getEquipmentRequest: GetEquipmentRequest): Response<ApiResult<GetBatchLocationResponse>>

    @POST("Customer/FindAllCustomerWithRequest")
    suspend fun getCustomers(): Response<ApiResult<List<Customer>>>

    @POST("InputLastData/FindLocationByVehicleId")
    suspend fun getVehicleLocation(@Body getEquipmentRequest: GetEquipmentRequest): Response<ApiResult<GetVehicleLocationResponse>>

    @POST("SendMessage/UpdateViewedSendMessageById")
    suspend fun seenMessage(@Body seenMessageRequest: SeenMessageRequest): Response<ApiResult<Unit>>

    @POST("Vehicle/GetMission") //mixer
    suspend fun getMixerMission(): Response<ApiResult<Mission>>

    @POST("Vehicle/GetMissionPump") //pomp
    suspend fun getPompMission(): Response<ApiResult<Mission>>


    @POST("Breakdown/InsertBreakdown")
    suspend fun insertBreakdown(@Body request: EntityRequest<BreakdownRequest>) : Response<ApiResult<Any>>

    @POST("Vehicle/GetVehicleListForCompany")
    suspend fun getEquipmentsForAdmin(): Response<ApiResult<List<AdminEquipment>>>

    @POST("Planning/FindAllPlanningByDate")
    suspend fun getPlansForAdmin(): Response<ApiResult<List<Plan>>>

    @POST("not implemented")
    suspend fun getFullReport(@Body request: GetReportRequest): Response<ApiResult<List<FullReport>>>

}

