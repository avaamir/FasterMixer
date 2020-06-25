package com.behraz.fastermixer.batch.models.requests.route

import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint

data class GetRouteResponse(
    @SerializedName("code")
    private val code: String,
    @SerializedName("routes")
    val routes: List<Route>,
    @SerializedName("waypoints")
    val wayPoints: List<WayPoint>
) {
    val isSuccessful get() = code == "Ok"


    fun getRoutePoints(): List<GeoPoint> {
        val points = ArrayList<GeoPoint>()
        routes.forEach { route->
            route.legs.forEach {leg->
                leg.steps.forEach { step->
                    step.intersections.forEach {intersection->
                        val lon = intersection.location[0]
                        val lat = intersection.location[1]
                        points.add(GeoPoint(lat, lon))
                    }
                }

            }
        }

        return points
    }


}