package com.behraz.fastermixer.batch.respository

import com.behraz.fastermixer.batch.models.requests.behraz.ChooseEquipmentRequest
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.models.requests.behraz.EntityRequest
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.apiservice.MapService
import com.behraz.fastermixer.batch.utils.general.RunOnceLiveData
import com.behraz.fastermixer.batch.utils.general.launchApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.osmdroid.util.GeoPoint
import retrofit2.Response
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1

object RemoteRepo {
    private lateinit var serverJobs: CompletableJob

    private fun <ResM, ReqM> apiReq(
        request: ReqM,
        requestFunction: KSuspendFunction1<ReqM, Response<Entity<ResM>>>,
        repoLevelHandler: ((Response<Entity<ResM>>) -> (Unit))? = null
    ): RunOnceLiveData<Entity<ResM>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceLiveData<Entity<ResM>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = requestFunction(request)
                    repoLevelHandler?.invoke(response)
                    CoroutineScope(Main).launch {
                        value = response.body()
                    }
                }) {
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }
            }
        }
    }

    private fun <ResM> apiReq(
        requestFunction: KSuspendFunction0<Response<Entity<ResM>>>,
        repoLevelHandler: ((Response<Entity<ResM>>) -> (Unit))? = null
    ): RunOnceLiveData<Entity<ResM>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceLiveData<Entity<ResM>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = requestFunction()
                    repoLevelHandler?.invoke(response)
                    CoroutineScope(Main).launch {
                        value = response.body()
                    }
                }) {
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }
            }
        }
    }


    fun login(loginRequest: LoginRequest) = apiReq(
        EntityRequest(loginRequest),
        ApiService.client::login
    ) { response ->
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.isSucceed) {
                    UserConfigs.loginUser(
                        it.entity!!,
                        blocking = true
                    ) //block the response till changes saved to db
                }
            }
        }
    }

    fun logout() = apiReq(ApiService.client::logout) {
        UserConfigs.logout()
    }

    fun getBatches() = apiReq(ApiService.client::getBatches)
    fun getPomps() = apiReq(ApiService.client::getPomps)

    fun chooseBatch(chooseEquipmentRequest: ChooseEquipmentRequest) =
        apiReq(chooseEquipmentRequest, ApiService.client::chooseBatch)

    fun choosePomp(chooseEquipmentRequest: ChooseEquipmentRequest) =
        apiReq(chooseEquipmentRequest, ApiService.client::chooseBatch)


    fun getMessages() = apiReq(ApiService.client::getMessages)


    fun sendVoice(imageRequest: MultipartBody.Part) =
        apiReq(imageRequest, ApiService.client::sendVoiceMessage)


    fun getRoute(coordinates: List<GeoPoint>): RunOnceLiveData<GetRouteResponse?> {
        if (!RemoteRepo::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceLiveData<GetRouteResponse?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    var strCoordinates = ""
                    coordinates.forEach {
                        //origin_longitude,origin_latitude; destination_longitude,destination_latitude
                        strCoordinates += "${it.longitude},${it.latitude};"
                    }
                    strCoordinates = strCoordinates.substring(0, strCoordinates.length - 1)

                    val response = MapService.client.getRoute(
                        coordinates = strCoordinates,
                        alternative = false,
                        steps = true
                    )
                    if (response.isSuccessful) {
                        response.body()?.let { getRouteResponse ->
                            CoroutineScope(Main).launch {
                                value = getRouteResponse
                            }
                        }
                    } else {
                        postValue(null)
                    }
                }, {
                    postValue(null)
                })
            }
        }
    }


}