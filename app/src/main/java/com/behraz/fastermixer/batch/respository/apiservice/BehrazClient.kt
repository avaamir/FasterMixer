package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.behraz.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BehrazClient {

    @POST("Person/LoginDriver")
    suspend fun login(@Body loginRequest: EntityRequest<LoginRequest>): Response<Entity<User>>

    @POST("Person/LogOut")
    suspend fun logout(): Response<Entity<Unit>>

    @POST("Equipment/GetBatch")
    suspend fun getBatches(): Response<Entity<List<Batch>>>

    @POST("Equipment/GetPomp")
    suspend fun getPomps(): Response<Entity<List<Pomp>>> //todo not implemented server side


    @POST("Equipment/ChoseEquipment")
    suspend fun chooseBatch(@Body chooseEquipmentRequest: ChooseEquipmentRequest): Response<Entity<Unit>>

    @POST("Equipment/ChosePomp")//todo not implemented server side check URL
    suspend fun choosePomp(@Body chooseEquipmentRequest: ChooseEquipmentRequest): Response<Entity<Unit>>

    @Multipart
    @POST("SendMessage/not_implemented")                           //todo not implemented server side check URL
    suspend fun sendVoiceMessage(@Part voice: MultipartBody.Part): Response<Entity<Unit>>

    @POST("SendMessage/FindAllSendMessageByReceiverId")
    suspend fun getMessages(): Response<Entity<List<Message>>>

    @POST("Planning/FindAllPlanningForBatch")
    suspend fun getBatchMixers(): Response<Entity<List<Mixer>>>

    @POST("Planning/FindAllPlanningForPump")
    suspend fun getPompMixers(): Response<Entity<List<Mixer>>>

    @POST("Equipment/FindLocationBatch")
    suspend fun getBatchLocation(@Body getEquipmentRequest: GetEquipmentRequest): Response<Entity<GetBatchLocationResponse>>

    @POST("Customer/FindAllCustomerWithRequest")
    suspend fun getCustomers(): Response<Entity<List<Customer>>>

    @POST("InputLastData/FindLocationByVehicleId")
    suspend fun getVehicleLocation(@Body getEquipmentRequest: GetEquipmentRequest): Response<Entity<GetVehicleLocationResponse>>

    @POST("SendMessage/UpdateViewedSendMessageById")
    suspend fun seenMessage(@Body seenMessageId: ChooseEquipmentRequest): Response<Entity<Unit>>

    @POST("Vehicle/GetMission")
    suspend fun getMixerMission(): Response<Entity<MixerMission>>


}