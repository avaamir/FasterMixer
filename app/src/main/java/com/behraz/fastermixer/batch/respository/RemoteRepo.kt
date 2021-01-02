package com.behraz.fastermixer.batch.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.models.requests.behraz.*
import com.behraz.fastermixer.batch.models.requests.openweathermap.CurrentWeatherByCoordinatesResponse
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.apiservice.MapService
import com.behraz.fastermixer.batch.respository.apiservice.WeatherService
import com.behraz.fastermixer.batch.respository.apiservice.interceptors.fromJsonToModel
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.fastermixer.fakeAdminManageAccountPage
import com.behraz.fastermixer.batch.utils.fastermixer.fakeDrawRoadReport
import com.behraz.fastermixer.batch.utils.fastermixer.fakeFullReports
import com.behraz.fastermixer.batch.utils.fastermixer.fakeSummeryReports
import com.behraz.fastermixer.batch.utils.general.RunOnceLiveData
import com.behraz.fastermixer.batch.utils.general.RunOnceMutableLiveData
import com.behraz.fastermixer.batch.utils.general.launchApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MultipartBody
import org.osmdroid.util.GeoPoint
import retrofit2.Response
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1

object RemoteRepo {
    private lateinit var serverJobs: CompletableJob

    fun <T: Any> Response<T>.handleError(onHandled: (ApiResult<T>) -> Unit) {
        onHandled(
            failedRequest(
                this.code().parseHttpCodeToErrorType(),
                try {
                    when (this.code()) {
                        401, 403, 500 -> {
                            val apiResult = this.errorBody()!!.string()
                                .fromJsonToModel<ApiResult<T>>()
                            apiResult.message
                        }
                        else -> throw IllegalStateException("Unhandled Error Code")
                    }
                } catch (ex: Exception) {
                    println("debux:${ex.message} in `RemoteRepo::handleError2`")
                    null
                }
            )

        )
    }

    private fun <ResM: Any, ReqM> apiReq(
        request: ReqM,
        requestFunction: KSuspendFunction1<ReqM, Response<ApiResult<ResM>>>,
        repoLevelHandler: ((Response<ApiResult<ResM>>) -> (Unit))? = null
    ): RunOnceLiveData<ApiResult<ResM>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceMutableLiveData<ApiResult<ResM>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = requestFunction(request)
                    repoLevelHandler?.invoke(response)

                    if (response.isSuccessful) {
                        CoroutineScope(Main).launch {
                            value = response.body()
                        }
                    } else {
                        //handleError(response)
                        TODO()
                    }
                }) {
                    println("debug:error:RemoteRepo.${requestFunction.name} has exception->${it.message}")
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }
            }
        }
    }

    private fun <T: Any> apiReq(
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
    }


    private fun <ResM: Any> apiReq(
        requestFunction: KSuspendFunction0<Response<ApiResult<ResM>>>,
        repoLevelHandler: ((Response<ApiResult<ResM>>) -> (Unit))? = null  //This will only excute if LiveData has an observer, because it called on active method
    ): RunOnceLiveData<ApiResult<ResM>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceMutableLiveData<ApiResult<ResM>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = requestFunction()
                    repoLevelHandler?.invoke(response)
                    if (response.isSuccessful) {
                        CoroutineScope(Main).launch {
                            value = response.body()
                        }
                    } else {
                        //val model = handleError(response)
                        TODO()
                    }
                }) {
                    println("debug:error:RemoteRepo.${requestFunction.name} has exception->${it.message}")
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }
            }
        }
    }


    private fun <T: Any> mockApiReq(
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

    fun login(loginRequest: LoginRequest): RunOnceLiveData<ApiResult<User>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceMutableLiveData<ApiResult<User>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val loginResponse = ApiService.client.login(loginRequest)
                    if (loginResponse.isSuccessful) {
                        val body = loginResponse.body()
                        if (body?.token != null) {
                            ApiService.setToken(body.token)
                            val userInfoResponse = ApiService.client.getUserInfo()
                            if (userInfoResponse.isSucceed) {
                                val userInfo = userInfoResponse.entity!!
                                val user = User(
                                    userInfo.id,
                                    userInfo.name,
                                    body.token,
                                    userInfo.roleId,
                                    userInfo.equipmentId
                                )
                                UserConfigs.loginUser(user, true)
                                withContext(Main) {
                                    value = succeedRequest(user)
                                }
                            } else {
                                CoroutineScope(Main).launch {
                                    value = failedRequest(
                                        userInfoResponse.errorType,
                                        userInfoResponse.message
                                    )
                                }
                            }
                        }
                    } else {
                        loginResponse.handleError {
                            CoroutineScope(Main).launch {
                                value = failedRequest(loginResponse.code().parseHttpCodeToErrorType())
                            }
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
        apiReq(EntityRequest(description), ApiService.client::insertBreakdown)


    fun getBatches() = apiReq(ApiService.client::getBatches)
    fun getPomps() = apiReq(ApiService.client::getPomps)

    fun chooseBatch(chooseEquipmentRequest: ChooseEquipmentRequest) =
        apiReq(chooseEquipmentRequest, ApiService.client::chooseBatch) {
            if (it.isSuccessful) {
                if (it.body()?.isSucceed == true) {
                    UserConfigs.updateUser(chooseEquipmentRequest.equipmentId, blocking = true)
                    println("debugx: RemoteRepo: blocking finished, User db updated, Now Activity Will Call..")
                }
            }
        }

    fun choosePomp(chooseEquipmentRequest: ChooseEquipmentRequest) =
        apiReq(chooseEquipmentRequest, ApiService.client::chooseBatch)


    fun getRequestMixers(batchNotPomp: Boolean) =
        if (batchNotPomp) apiReq(ApiService.client::getBatchMixers) else apiReq(ApiService.client::getPompMixers)

    fun getAllMixers() = apiReq(ApiService.client::getAllMixers)

    fun getMessage(onResponse: (ApiResult<List<Message>>?) -> Unit) { //Resonse ro mikhad
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        CoroutineScope(IO + serverJobs).launchApi({
            val response = ApiService.client.getMessages()
            if (response.isSuccessful) {
                val messages = response.body()?.entity
                if (response.body()?.isSucceed == true) {
                    if (messages != null) {
                        MessageRepo.insert(messages)
                        val seenMessagesIdList = messages.map { it.id }
                        seenMessage(seenMessagesIdList)
                    }
                    withContext(Main) {
                        onResponse(response.body())
                    }
                } else {
                    withContext(Main) {
                        onResponse(response.body())
                    }
                }
            } else {
                withContext(Main) {
                    onResponse(response.body())
                }
            }
        }) {
            onResponse(null)
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
            val response = ApiService.client.getBatchLocation(GetEquipmentRequest(equipmentId))
            CoroutineScope(Main).launch {
                onResponse(response.body()?.entity?.equipmentLocation)
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
            val response = ApiService.client.getVehicleLocation(GetEquipmentRequest(equipmentId))
            CoroutineScope(Main).launch {
                onResponse(response.body())
            }
        }) {
            CoroutineScope(Main).launch {
                onResponse(null)
            }
        }
    }


    private fun seenMessage(messageIds: List<String>) {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        CoroutineScope(IO + serverJobs).launchApi({
            ApiService.client.seenMessage(SeenMessageRequest(messageIds))
        }) {}
    }

    private fun getMixerMission() = apiReq(ApiService.client::getMixerMission)
    private fun getPompMission() = apiReq(ApiService.client::getPompMission)

    fun getMission(isPomp: Boolean): RunOnceLiveData<ApiResult<Mission>?> {
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

    fun checkUpdates() = apiReq(ApiService.client::checkUpdates) {
        println("debux:$it")
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
                                failedRequest(response.code().parseHttpCodeToErrorType(), response.message())
                        } else {
                            failedRequest(response.code().parseHttpCodeToErrorType(), response.message())
                        }
                    }
                }, {
                    postValue(null)
                })
            }
        }
    }

    fun getFullReport2(request: GetReportRequest) =
        apiReq(request, ApiService.client::getFullReport)

    fun getFullReport(request: GetReportRequest) = mockApiReq(succeedRequest(fakeFullReports()))

    fun getSummeryReport(request: GetReportRequest) =
        mockApiReq(succeedRequest(fakeSummeryReports()))

    fun getDrawRoadReport(request: GetReportRequest) =
        mockApiReq(succeedRequest(fakeDrawRoadReport()))
}