package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.RemoteRepo
import org.osmdroid.util.GeoPoint

class MixerMapFragmentViewModel: ViewModel() {

    var shouldFindRoutesAfterUserLocationFound: Boolean = false

    var myLocation: GeoPoint? = null


    private var coordinates: MutableLiveData<List<GeoPoint>> = MutableLiveData()
    val getRouteResponse: LiveData<GetRouteResponse?> =
        Transformations.switchMap(coordinates) { coordinates ->
            RemoteRepo.getRoute(coordinates = coordinates)
        }

    fun getRoute(coordinates: List<GeoPoint>) {
        this.coordinates.value = coordinates
    }

    fun tryGetRouteAgain() {
        println("debux:tryGetRouteAgain: ${coordinates.value}")
        this.coordinates.value = this.coordinates.value
    }


}