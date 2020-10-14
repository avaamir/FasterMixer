package com.behraz.fastermixer.batch.respository

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
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.respository.persistance.userdb.UserRepo
import com.behraz.fastermixer.batch.utils.general.RunOnceLiveData
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
                    println("debug:error:RemoteRepo.${requestFunction.name} has exception->${it.message}")
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }
            }
        }
    }

    private fun <ResM> apiReq(
        requestFunction: KSuspendFunction0<Response<Entity<ResM>>>,
        repoLevelHandler: ((Response<Entity<ResM>>) -> (Unit))? = null  //This will only excute if LiveData has an observer, because it called on active method
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
                    println("debug:error:RemoteRepo.${requestFunction.name} has exception->${it.message}")
                    CoroutineScope(Main).launch {
                        value = null
                    }
                }
            }
        }
    }


    fun login(loginRequest: LoginRequest): RunOnceLiveData<Entity<User>?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceLiveData<Entity<User>?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = ApiService.client.login(EntityRequest(loginRequest))
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.isSucceed == true) {
                            body.entity?.let { _user ->
                                UserRepo.userDao.deleteAll()
                                UserRepo.userDao.insert(_user)
                                //UserConfigs.loginUser(_user, true)
                            }
                        }
                    }
                    postValue(response.body())
                }, {
                    postValue(null)
                })
            }

        }
    }


    fun login2(loginRequest: LoginRequest) = apiReq(
        EntityRequest(loginRequest),
        ApiService.client::login
    ) { response ->
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.isSucceed) {
                    println("debug:login: ${it.entity}")
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

    fun getMessage(onResponse: (Entity<List<Message>>?) -> Unit) { //Resonse ro mikhad
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
        equipmentId: String,
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
        equipmentId: String,
        onResponse: (Entity<GetVehicleLocationResponse>?) -> Unit
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

    fun getMission(isPomp: Boolean): RunOnceLiveData<Entity<Mission>?> {
        return if (isPomp) getPompMission() else getMixerMission()
    }


    fun getCustomers() = apiReq(ApiService.client::getCustomers)

    //Admin
    fun getEquipmentsForAdmin() = apiReq(ApiService.client::getEquipmentsForAdmin)
    fun getPlansForAdmin() = apiReq(ApiService.client::getPlansForAdmin)

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

    fun checkUpdates() = apiReq(ApiService.client::checkUpdates)

    fun getCurrentWeatherByCoordinates(location: GeoPoint): RunOnceLiveData<Entity<CurrentWeatherByCoordinatesResponse>> {
        return object : RunOnceLiveData<Entity<CurrentWeatherByCoordinatesResponse>>() {
            override fun onActiveRunOnce() {
                if (!RemoteRepo::serverJobs.isInitialized || !serverJobs.isActive) serverJobs =
                    Job()
                CoroutineScope(IO + serverJobs).launchApi({
                    val response = WeatherService.client.getCurrentWeatherByCoordinates(
                        location.latitude.toString(),
                        location.longitude.toString()
                    )
                    withContext(Main) {
                        value = if (response.isSuccessful) {
                            Entity(response.body(), true, null)
                        } else {
                            Entity(
                                null,
                                false,
                                response.code().toString() + ":" + response.message()
                            )

                        }
                    }
                }, {
                    value = null
                })
            }
        }
    }


}