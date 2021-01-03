package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.behraz.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface BehrazClient {
    //First Page
    @POST("AppVersion/FindLastAppVersion")
    suspend fun checkUpdates(): ApiResult<UpdateResponse>

    @POST("Users/login")
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


    //TODO mission not migrated, not implemented server side
    @POST("Vehicle/GetMission") //mixer
    suspend fun getMixerMission(): ApiResult<Mission>

    @POST("Vehicle/GetMissionPump") //pomp
    suspend fun getPompMission(): ApiResult<Mission>
    //=========================================
    //Batch

    @POST("Equipments/GetBatchLocation/{id}")
    suspend fun getBatchLocation(@Path("id") id: Int): ApiResult<GetBatchLocationResponse>

    //TODO mission not migrated
    @POST("Equipment/GetBatch")
    suspend fun getBatches(): ApiResult<List<Batch>>

    @POST("Equipment/ChoseEquipment")
    suspend fun chooseBatch(@Body chooseEquipmentRequest: ChooseEquipmentRequest): ApiResult<Unit>

    @POST("Planning/FindAllPlanningForBatch")
    suspend fun getBatchMixers(): ApiResult<List<Mixer>>

    //==========================================


    //TODO hazf shode az scenario
    @POST("Equipment/GetPomp")
    suspend fun getPomps(): ApiResult<List<Pomp>>

    //TODO hazf shode az scenario
    @POST("Equipment/ChosePomp")//todo not implemented server side check URL
    suspend fun choosePomp(@Body chooseEquipmentRequest: ChooseEquipmentRequest): ApiResult<Unit>

    @Multipart
    @POST("SendMessage/not_implemented")                           //todo not implemented server side check URL
    suspend fun sendVoiceMessage(@Part voice: MultipartBody.Part): ApiResult<Unit>


    @POST("Planning/FindAllPlanningForPump")
    suspend fun getPompMixers(): ApiResult<List<Mixer>>

    @POST("Vehicle/FindAllMixerForPump")
    suspend fun getAllMixers(): ApiResult<List<Mixer>>

    @POST("Customer/FindAllCustomerWithRequest")
    suspend fun getCustomers(): ApiResult<List<Customer>>

    @POST("Vehicle/GetVehicleListForCompany")
    suspend fun getEquipmentsForAdmin(): ApiResult<List<AdminEquipment>>

    @POST("Planning/FindAllPlanningByDate")
    suspend fun getPlansForAdmin(): ApiResult<List<Plan>>

    @POST("not implemented")
    suspend fun getFullReport(@Body request: GetReportRequest): ApiResult<List<FullReport>>

}

