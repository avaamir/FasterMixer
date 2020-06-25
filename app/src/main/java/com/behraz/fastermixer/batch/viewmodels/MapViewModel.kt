package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.RemoteRepo
import org.osmdroid.util.GeoPoint

class MapViewModel : ViewModel() {

    val myLocation: GeoPoint = GeoPoint(31.891413345001638, 54.35357135720551)

    private var coordinates: MutableLiveData<List<GeoPoint>> = MutableLiveData()
    val getRouteResponse: LiveData<GetRouteResponse?> =
        Transformations.switchMap(coordinates) { coordinates ->
            RemoteRepo.getRoute(coordinates = coordinates)
        }

    fun getRoute(coordinates: List<GeoPoint>) {
        this.coordinates.value = coordinates
    }


}