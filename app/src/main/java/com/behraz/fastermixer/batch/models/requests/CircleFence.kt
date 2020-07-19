package com.behraz.fastermixer.batch.models.requests

import org.osmdroid.util.GeoPoint

data class CircleFence(
    val center: GeoPoint,
    val radius: Double
) {

    companion object {
        fun circleFenceToCenterGeoPoint(
            geofencepoint: String
        ) =
            geofencepoint.substring(
                startIndex = geofencepoint.indexOf("(") + 1,
                endIndex = geofencepoint.indexOf(")")
            ).split(",").let { locAndRad ->
                locAndRad[0].split(" ").let { loc ->
                    CircleFence(
                        GeoPoint(loc[0].toDouble(), loc[1].toDouble()),
                        locAndRad[1].toDouble()
                    )
                }
            }
    }

}