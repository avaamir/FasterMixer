package com.behraz.fastermixer.batch.utils.general

import android.location.Location
import android.location.LocationManager
import org.osmdroid.util.GeoPoint

/** this class is use to Calculate Bearing , Speed , Average Speed */
object LocationHandler {
    const val MIN_LOCATION_UPDATE_TIME = 5L
    private const val LOCATION_CAPACITY = 100


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

    private val locations =
        FIFO<Location>(
            LOCATION_CAPACITY
        )

    fun saveLocation(location: Location) {
        if (locations.size > 1) {
            if (locations.size == LOCATION_CAPACITY) {
                distanceSum -= (locations[0].distanceTo(
                    locations[1])) //remove first distance from sum
            }
            val lastDistance = locations.last.distanceTo(location)
            distanceSum += lastDistance
            lastSpeed = lastDistance / MIN_LOCATION_UPDATE_TIME
        }
        locations.add(location)
        println("debug: location added -> $location")
    }
}