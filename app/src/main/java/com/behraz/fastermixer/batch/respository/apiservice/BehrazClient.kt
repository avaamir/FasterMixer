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

    //Main Pages
    // @POST("login")
    //suspend fun login(@Body loginRequest: LoginRequest): Response<OnLoggedInResponse>



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
    @POST("SendMessage/FindAllSendMessageByReceiverId")                           //todo not implemented server side check URL
    suspend fun sendVoiceMessage(@Part voice: MultipartBody.Part): Response<Entity<Unit>>

    @POST("SendMessage/FindAllSendMessageByReceiverId")
    suspend fun getMessages(): Response<Entity<List<Message>>>

    @POST("Planning/FindAllPlanningForBatch")
    suspend fun getBatchMixers() : Response<Entity<List<Mixer>>>

    @POST("Equipment/FindLocationBatch")
    suspend fun getEquipmentLocation(@Body getEquipmentRequest: GetEquipmentRequest): Response<Entity<GetEquipmentLocationResponse>>



}