package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.behraz.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BehrazClient {
    @POST("AppVersion/FindLastAppVersion")
    suspend fun checkUpdates(): ApiResult<UpdateResponse>

    @POST("Users/login")
    suspend fun getToken(@Body loginRequest: LoginRequest): Response<GetTokenResponse>

    @POST("Users/GetUserInfoForApp")
    suspend fun getUserInfo(): ApiResult<GetUserInfoResponse>


    @POST("Person/LogOut")
    suspend fun logout(): ApiResult<Unit>

    @POST("Equipment/GetBatch")
    suspend fun getBatches(): ApiResult<List<Batch>>

    @POST("Equipment/ChoseEquipment")
    suspend fun chooseBatch(@Body chooseEquipmentRequest: ChooseEquipmentRequest): ApiResult<Unit>

    @POST("Planning/FindAllPlanningForBatch")
    suspend fun getBatchMixers(): ApiResult<List<Mixer>>

    //TODO hazf shode az scenario
    @POST("Equipment/GetPomp")
    suspend fun getPomps(): ApiResult<List<Pomp>>

    //TODO hazf shode az scenario
    @POST("Equipment/ChosePomp")//todo not implemented server side check URL
    suspend fun choosePomp(@Body chooseEquipmentRequest: ChooseEquipmentRequest): ApiResult<Unit>

    @Multipart
    @POST("SendMessage/not_implemented")                           //todo not implemented server side check URL
    suspend fun sendVoiceMessage(@Part voice: MultipartBody.Part): ApiResult<Unit>

    @POST("SendMessage/FindAllSendMessageByReceiverId")
    suspend fun getMessages(): ApiResult<List<Message>>

    @POST("Planning/FindAllPlanningForPump")
    suspend fun getPompMixers(): ApiResult<List<Mixer>>

    @POST("Vehicle/FindAllMixerForPump")
    suspend fun getAllMixers(): ApiResult<List<Mixer>>

    @POST("Equipment/FindLocationBatch")
    suspend fun getBatchLocation(@Body getEquipmentRequest: GetEquipmentRequest): ApiResult<GetBatchLocationResponse>

    @POST("Customer/FindAllCustomerWithRequest")
    suspend fun getCustomers(): ApiResult<List<Customer>>

    @POST("InputLastData/FindLocationByVehicleId")
    suspend fun getVehicleLocation(@Body getEquipmentRequest: GetEquipmentRequest): ApiResult<GetVehicleLocationResponse>

    @POST("SendMessage/UpdateViewedSendMessageById")
    suspend fun seenMessage(@Body seenMessageRequest: SeenMessageRequest): ApiResult<Unit>

    @POST("Vehicle/GetMission") //mixer
    suspend fun getMixerMission(): ApiResult<Mission>

    @POST("Vehicle/GetMissionPump") //pomp
    suspend fun getPompMission(): ApiResult<Mission>


    @POST("Breakdown/InsertBreakdown")
    suspend fun insertBreakdown(@Body request: EntityRequest<BreakdownRequest>): ApiResult<Any>

    @POST("Vehicle/GetVehicleListForCompany")
    suspend fun getEquipmentsForAdmin(): ApiResult<List<AdminEquipment>>

    @POST("Planning/FindAllPlanningByDate")
    suspend fun getPlansForAdmin(): ApiResult<List<Plan>>

    @POST("not implemented")
    suspend fun getFullReport(@Body request: GetReportRequest): ApiResult<List<FullReport>>

}

