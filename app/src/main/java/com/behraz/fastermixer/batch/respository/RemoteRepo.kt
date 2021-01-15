package com.behraz.fastermixer.batch.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.*
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.models.requests.behraz.*
import com.behraz.fastermixer.batch.models.requests.openweathermap.CurrentWeatherByCoordinatesResponse
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.apiservice.MapService
import com.behraz.fastermixer.batch.respository.apiservice.WeatherService
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.fastermixer.fakeAdminManageAccountPage
import com.behraz.fastermixer.batch.utils.fastermixer.fakeFullReports
import com.behraz.fastermixer.batch.utils.general.RunOnceLiveData
import com.behraz.fastermixer.batch.utils.general.RunOnceMutableLiveData
import com.behraz.fastermixer.batch.utils.general.launchApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.osmdroid.util.GeoPoint
import retrofit2.Response
import java.io.IOException
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1

object RemoteRepo {
    private lateinit var serverJobs: CompletableJob

    private fun <T : Any> Response<T>.handleError(onHandled: ((ApiResult<T>) -> Unit)? = null): ApiResult<T> {
        return failedRequest<T>(
            this.code().parseHttpCodeToErrorType(),
            try {
                val gson = Gson()
                val type = object : TypeToken<ApiResult<Unit>>() {}.type
                val apiResult = gson.fromJson<ApiResult<Unit>>(this.errorBody()!!.string(), type)
                apiResult.message
            } catch (ex: Exception) {
                println("debux:${ex.message} in `RemoteRepo::handleError2`")
                null
            }
        ).also {
            onHandled?.invoke(it)
        }
    }

    private fun <ResM : Any, ReqM : Any> apiReq(
        request: ReqM,
        requestFunction: KSuspendFunction1<ReqM, ApiResult<ResM>>,
        repoLevelHandler: ((ApiResult<ResM>) -> (Unit))? = null
    ): RunOnceLiveData<ApiResult<ResM>> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceMutableLiveData<ApiResult<ResM>>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launch {
                    val response = requestFunction(request)
                    repoLevelHandler?.invoke(response)
                    withContext(Main) {
                        setValue(response)
                    }
                } /*{
                    println("debug:error:RemoteRepo.${requestFunction.name} has exception->${it.message}")
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }*/
            }
        }
    }

    /*private fun <T : Any> apiReq(
        apiFunction: suspend () -> ApiResult<T>?,
        repoLevelHandler: ((ApiResult<T>?) -> Unit)? = null
    ): RunOnceLiveData<ApiResult<T>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceMutableLiveData<ApiResult<T>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = apiFunction()
                    repoLevelHandler?.invoke(response)
                    withContext(Main) {
                        value = response
                    }
                }) {
                    println("debug:error:RemoteRepo.${apiFunction} has exception->${it.message}")
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }
            }
        }
    }*/


    private fun <ResM : Any> apiReq(
        requestFunction: KSuspendFunction0<ApiResult<ResM>>,
        repoLevelHandler: ((ApiResult<ResM>) -> (Unit))? = null  //This will only excute if LiveData has an observer, because it called on active method
    ): RunOnceLiveData<ApiResult<ResM>> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceMutableLiveData<ApiResult<ResM>>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launch {
                    val result = requestFunction()
                    repoLevelHandler?.invoke(result)
                    withContext(Main) {
                        setValue(result)
                    }
                } /*{
                    println("debug:error:RemoteRepo.${requestFunction.name} has exception->${it.message}")
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }*/
            }
        }
    }


    private fun <T : Any> mockApiReq(
        responseApiResult: ApiResult<T>?,
        duration: Long = 1000L
    ): RunOnceLiveData<ApiResult<T>?> {
        return object : RunOnceLiveData<ApiResult<T>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO).launch {
                    delay(duration)
                    withContext(Main) {
                        value = responseApiResult
                    }
                }
            }
        }
    }




    private fun getServerAddress(
        loginRequest: LoginRequest,
        tryOnUnAuthorized: Boolean
    ): ApiResult<String> {
        val client = OkHttpClient()
        val type = object : TypeToken<LoginRequest>() {}.type
        val data = Gson().toJson(loginRequest, type)
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = data.toRequestBody(mediaType)
        val request = Request.Builder()
            .url("${ApiService.DEFAULT_DOMAIN}/api/v1/Users/Get")
            .post(body)
            .build()

        var response: okhttp3.Response? = null
        return try {
            response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val strResponse = response.body!!.string()
                val responseType = object : TypeToken<ApiResult<String>>() {}.type
                val result: ApiResult<String> = Gson().fromJson(strResponse, responseType)
                result.errorType = ErrorType.OK
                result
            } else {
                val errorType = response.code.parseHttpCodeToErrorType()
                if (errorType == ErrorType.UnAuthorized) {
                    UserConfigs.logout()
                    if (tryOnUnAuthorized) {
                        return getServerAddress(loginRequest, false)
                    }
                }
                failedRequest(errorType)
            }
        } catch (e: IOException) {
            failedRequest(response?.code?.parseHttpCodeToErrorType() ?: ErrorType.NetworkError)
        }
    }

    private suspend fun getToken(
        loginRequest: LoginRequest
    ): ApiResult<GetTokenResponse> {
        val getTokenResponse = ApiService.client.getToken(loginRequest)
        return if (getTokenResponse.isSuccessful) {
            val body = getTokenResponse.body()!!
            succeedRequest(body)
        } else {
            getTokenResponse.handleError()
        }
    }


    fun login(loginRequest: LoginRequest): RunOnceLiveData<ApiResult<User>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceMutableLiveData<ApiResult<User>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val addressResult = getServerAddress(loginRequest, true)
                    if (addressResult.isSucceed) {
                        ApiService.setAddress(addressResult.entity ?: ApiService.DEFAULT_DOMAIN)


                        val tokenResult = getToken(loginRequest)
                        if (tokenResult.isSucceed) {
                            val token = tokenResult.entity!!.token!!
                            ApiService.setToken(token)
                            val getUserInfoResult = ApiService.client.getUserInfo()
                            if (getUserInfoResult.isSucceed) {
                                val userInfo = getUserInfoResult.entity!!
                                val user = User(
                                    userInfo.id,
                                    userInfo.name,
                                    userInfo.personalCode,
                                    token,
                                    userInfo.roleId,
                                    userInfo.equipmentId
                                )
                                UserConfigs.loginUserBlocking(user)
                                withContext(Main) {
                                    value = succeedRequest(user)
                                }
                            } else { //Failed Get UserInfo
                                withContext(Main) {
                                    value = failedRequest(
                                        getUserInfoResult.errorType,
                                        getUserInfoResult.message
                                    )
                                }
                            }
                        } else { //Failed Get Token
                            withContext(Main) {
                                value = failedRequest(tokenResult.errorType, tokenResult.message)
                            }
                        }
                    } else {
                        withContext(Main) {
                            value = failedRequest(addressResult.errorType, addressResult.message)
                        }
                    }
                }, {
                    postValue(null)
                })
            }

        }
    }

    fun logout() = apiReq(ApiService.client::logout) {
        UserConfigs.logout()
    }

    fun insertBreakdownRequest(description: BreakdownRequest) =
        apiReq(description, ApiService.client::insertBreakdown)


    fun getBatches() = apiReq(ApiService.client::getBatches)
    fun getPomps() = apiReq(ApiService.client::getPomps)

    fun chooseBatch(equipmentId: Int) =
        apiReq(equipmentId, ApiService.client::chooseBatch) {
            if (it.isSucceed) {
                UserConfigs.updateUser(equipmentId = equipmentId)
                println("debugx: RemoteRepo: blocking finished, User db updated, Now Activity Will Call..")
            }
        }


    fun choosePomp(equipmentId: Int) =
        apiReq(equipmentId, ApiService.client::chooseBatch)

    fun getRequestMixers(batchNotPomp: Boolean) =
        if (batchNotPomp) apiReq(ApiService.client::getBatchMixers) else apiReq(ApiService.client::getPompMixers)

    fun getAllMixers() = apiReq(ApiService.client::getAllMixers)


    fun getMessage(onResponse: (ApiResult<List<Message>>?) -> Unit) { //Response ro mikhad
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        CoroutineScope(IO + serverJobs).launchApi({
            val result = ApiService.client.getMessages()
            if (result.isSucceed) {
                val messageDtos = result.entity!!
                val messages = messageDtos.map {
                    it.toEntity()
                }

                if (messages.isNotEmpty()) {
                    MessageRepo.insert(messages)
                    val seenMessagesIdList = messageDtos.map { it.id }
                    seenMessage(seenMessagesIdList)
                }
                withContext(Main) {
                    onResponse(succeedRequest(messages))
                }
            } else {
                withContext(Main) {
                    onResponse(failedRequest(result.errorType))
                }
            }
        }) {
            CoroutineScope(Main).launch {
                onResponse(null)
            }
        }
    }


    fun sendVoice(imageRequest: MultipartBody.Part) =
        apiReq(imageRequest, ApiService.client::sendVoiceMessage)

    fun getBatchLocation( //be khater in ke mikhastam callback dar bashe va niazi be liveData nabud az Reflection estefade nakardam va dasti code zadam
        equipmentId: Int,
        onResponse: (Fence?) -> Unit
    ) {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        CoroutineScope(IO + serverJobs).launchApi({
            val response = ApiService.client.getBatchLocation(equipmentId)
            CoroutineScope(Main).launch {
                if (response.isSucceed)
                    onResponse(Fence.strToFence(response.entity!!))
                else
                    onResponse(null)
            }
        }) {
            CoroutineScope(Main).launch {
                onResponse(null)
            }
        }
    }

    fun getEquipmentLocation( //be khater in ke mikhastam callback dar bashe (chun momkene dar ui observer nadashte bashe) va niazi be liveData nabud az Reflection estefade nakardam va dasti code zadam
        equipmentId: Int,
        onResponse: (ApiResult<GetVehicleLocationResponse>?) -> Unit
    ) {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        CoroutineScope(IO + serverJobs).launchApi({
            val result = ApiService.client.getVehicleLocation(equipmentId)
            CoroutineScope(Main).launch {
                onResponse(result)
            }
        }) {
            CoroutineScope(Main).launch {
                onResponse(null)
            }
        }
    }


    private fun seenMessage(messageIds: List<Int>) {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        CoroutineScope(IO + serverJobs).launchApi({
            ApiService.client.seenMessage(messageIds)
        }) {}
    }

    private fun getMixerMission() = apiReq(ApiService.client::getMixerMission)

    private fun getPompMission() = apiReq(ApiService.client::getPompMission)

    fun getMission(isPomp: Boolean): RunOnceLiveData<ApiResult<Mission>> {
        return if (isPomp) getPompMission() else getMixerMission()
    }


    fun getCustomers() = apiReq(ApiService.client::getCustomers)

    //Admin
    fun getEquipmentsForAdmin() = apiReq(ApiService.client::getEquipmentsForAdmin)
    fun getPlansForAdmin() = apiReq(ApiService.client::getPlansForAdmin)

    fun getAdminAccountPageData(): LiveData<GetAdminAccountPage> {
        //TODO not yet implemented
        return MutableLiveData(fakeAdminManageAccountPage())
    }


    fun checkUpdates() = apiReq(ApiService.client::checkUpdates)

    fun getFullReport(request: GetReportRequest.FullReportRequest) =
        apiReq(request, ApiService.client::getFullReport)

    fun getFullReport2(request: GetReportRequest.FullReportRequest) =
        mockApiReq(succeedRequest(fakeFullReports()))

    fun getSummeryReport(request: GetReportRequest.SummeryReportRequest) =
        apiReq(request, ApiService.client::getSummeryReport)


    fun getDrawRoadReport(
        request: GetReportRequest.DrawRoadRequest,
        onResponse: (ApiResult<List<ReportPoint>>) -> Unit
    ) {
        if (!RemoteRepo::serverJobs.isInitialized || !serverJobs.isActive)
            serverJobs = Job()
        CoroutineScope(IO + serverJobs).launch {
            val result = ApiService.client.getDrawRoadReport(request)
            withContext(Main) {
                onResponse(result)
            }
        }
    }

    fun getDrawRoadReport(drawRoadRequest: GetReportRequest.DrawRoadRequest) =
        apiReq(drawRoadRequest, ApiService.client::getDrawRoadReport)


    fun getAdminMessages() = apiReq(ApiService.client::getAdminMessages).map {
        ApiResult(
            entity = it.entity?.map { dto -> dto.toEntity() },
            isSucceed = it.isSucceed,
            _message = it.message,
            errorType = it.errorType
        )
    }

    fun getActiveServices(requestId: Int) = apiReq(requestId, ApiService.client::getActiveServices)
    fun getServiceHistory(vehicleId: Int, requestId: Int): LiveData<ApiResult<List<Service>>> {
        return object : RunOnceLiveData<ApiResult<List<Service>>>() {
            override fun onActiveRunOnce() {
                if (!RemoteRepo::serverJobs.isInitialized || !serverJobs.isActive)
                    serverJobs = Job()
                CoroutineScope(IO + serverJobs).launch {
                    val result = ApiService.client.getServiceHistory(vehicleId, requestId)
                    withContext(Main) {
                        value = result
                    }
                }
            }
        }
    }


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
                    println("debux: ${response.isSuccessful} , ${response.code()}")
                    if (response.isSuccessful) {
                        response.body()?.let { getRouteResponse ->
                            CoroutineScope(Main).launch {
                                value = getRouteResponse
                            }
                        }
                    } else {
                        response.errorBody()?.string()?.let {
                            if (it.contains("NoRoute")) {
                                postValue(GetRouteResponse("NoRoute", listOf(), listOf()))
                            } else {
                                postValue(null)
                            }
                        }
                    }
                }, {
                    postValue(null)
                })
            }
        }
    }

    fun getCurrentWeatherByCoordinates(location: GeoPoint): RunOnceLiveData<ApiResult<CurrentWeatherByCoordinatesResponse>> {
        return object : RunOnceLiveData<ApiResult<CurrentWeatherByCoordinatesResponse>>() {
            override fun onActiveRunOnce() {
                if (!RemoteRepo::serverJobs.isInitialized || !serverJobs.isActive) serverJobs =
                    Job()
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = WeatherService.client.getCurrentWeatherByCoordinates(
                        location.latitude.toString(),
                        location.longitude.toString()
                    )
                    withContext(Main) {
                        val body = response.body()
                        value = if (response.isSuccessful) {
                            if (body != null)
                                succeedRequest(body)
                            else
                                failedRequest(
                                    response.code().parseHttpCodeToErrorType(),
                                    response.message()
                                )
                        } else {
                            failedRequest(
                                response.code().parseHttpCodeToErrorType(),
                                response.message()
                            )
                        }
                    }
                }, {
                    postValue(null)
                })
            }
        }
    }
}