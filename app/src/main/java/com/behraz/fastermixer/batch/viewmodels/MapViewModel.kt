package com.behraz.fastermixer.batch.viewmodels

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.route.GetRouteResponse
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.utils.general.FIFO
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class MapViewModel : ViewModel() {


    companion object {
        const val MIN_LOCATION_UPDATE_TIME = 5L
        const val LOCATION_CAPACITY = 100
    }


    val markers =  HashMap<String, Marker>()

    private var distanceSum: Float = 0f

    private val lastBearing: Float?
        get() {
            locations.peek()?.let {
                if (LocationManager.GPS_PROVIDER == it.provider && it.hasBearing())
                    return it.bearing
            }
            return null
        }

    private var lastSpeed: Float = 0f

    private val averageSpeed: Float
        get() {
            return distanceSum / (LOCATION_CAPACITY * MIN_LOCATION_UPDATE_TIME)
        }

    private val locations = FIFO<Location>(LOCATION_CAPACITY)
    var myLocation =  GeoPoint(31.891413345001638, 54.35357135720551)

    private var coordinates: MutableLiveData<List<GeoPoint>> = MutableLiveData()
    val getRouteResponse: LiveData<GetRouteResponse?> =
        Transformations.switchMap(coordinates) { coordinates ->
            RemoteRepo.getRoute(coordinates = coordinates)
        }

    fun getRoute(coordinates: List<GeoPoint>) {
        this.coordinates.value = coordinates
    }


    fun saveLocation(location: Location) {
        if (locations.size > 1) {
            if (locations.size == LOCATION_CAPACITY) {
                distanceSum -= (locations[0].distanceTo(locations[1])) //remove first distance from sum
            }
            val lastDistance = locations.last.distanceTo(location)
            distanceSum += lastDistance
            lastSpeed = lastDistance / MIN_LOCATION_UPDATE_TIME
        }
        locations.add(location)
        println("debug: location added -> $location")
    }


}