package com.behraz.fastermixer.batch.respository

import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.apiservice.MapService
import com.behraz.fastermixer.batch.utils.general.RunOnceLiveData
import com.behraz.fastermixer.batch.utils.general.launchApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.osmdroid.util.GeoPoint

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