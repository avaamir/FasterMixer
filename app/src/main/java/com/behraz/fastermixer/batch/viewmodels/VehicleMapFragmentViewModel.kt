package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.RemoteRepo
import org.osmdroid.util.GeoPoint

open class VehicleMapFragmentViewModel: ViewModel(){
    var myLocation: GeoPoint? = null

    var hasNewMission: Boolean = false


    private var coordinates: MutableLiveData<List<GeoPoint>> = MutableLiveData()
    val getRouteResponse: LiveData<GetRouteResponse?> =
        Transformations.switchMap(coordinates) { coordinates ->
            RemoteRepo.getRoute(coordinates = coordinates)
        }

    fun getRoute(coordinates: List<GeoPoint>) {
        println("debux: getRoute($coordinates)=====================================")
        this.coordinates.value = coordinates
    }

    fun tryGetRouteAgain() {
        val lastCoordinates = this.coordinates.value
        val dest = lastCoordinates!![1]
        println("debux:tryGetRouteAgain: ${listOf(myLocation!!, dest)}===============")
        this.coordinates.value = listOf(myLocation!!, dest)
    }
}