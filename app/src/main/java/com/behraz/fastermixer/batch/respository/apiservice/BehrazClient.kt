package com.behraz.fastermixer.batch.respository.apiservice

import com.behraz.fastermixer.batch.models.Batch
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.Pomp
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.models.requests.behraz.ChooseEquipmentRequest
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.models.requests.behraz.EntityRequest
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
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




}