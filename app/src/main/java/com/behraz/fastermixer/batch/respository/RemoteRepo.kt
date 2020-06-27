package com.behraz.fastermixer.batch.respository

import com.behraz.fastermixer.batch.models.requests.behraz.ChooseBatchRequest
import com.behraz.fastermixer.batch.models.requests.behraz.EntityRequest
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.models.requests.behraz.LoginRequest
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.respository.apiservice.MapService
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.RunOnceLiveData
import com.behraz.fastermixer.batch.utils.general.launchApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.osmdroid.util.GeoPoint
import retrofit2.Response
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.KSuspendFunction0

object RemoteRepo {
    private lateinit var serverJobs: CompletableJob

    /*fun getMainPage(): RunOnceLiveData<MainPageResponse?> {
        if (!::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceLiveData<MainPageResponse?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(Dispatchers.IO + serverJobs).launchApi({
                    val response = ApiService.getStoreClient().getMainPage()
                    if (response.isSuccessful) {
                        response.body()?.let { mainPageResponse ->
                            postValue(mainPageResponse)
                        }
                    } else {
                        postValue(null)
                    }
                }, {
                    postValue(null)
                })
            }

        }
    }*/

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
                    ApiService.setToken(it.entity.token)
                }
            }
        }
    }

    fun logout() = apiReq(ApiService.client::logout)

    fun getBatches() = apiReq(ApiService.client::getBatches)
    fun chooseBatch(chooseBatchRequest: ChooseBatchRequest) = apiReq(
        chooseBatchRequest,
        ApiService.client::chooseBatch
    )


    fun getRoute(coordinates: List<GeoPoint>): RunOnceLiveData<GetRouteResponse?> {
        if (!RemoteRepo::serverJobs.isInitialized || !serverJobs.isActive) serverJobs = Job()
        return object : RunOnceLiveData<GetRouteResponse?>() {
            override fun onActiveRunOnce() {
                CoroutineScope(Dispatchers.IO + serverJobs).launchApi({
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