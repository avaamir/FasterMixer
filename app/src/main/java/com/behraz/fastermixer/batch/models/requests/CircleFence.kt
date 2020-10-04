package com.behraz.fastermixer.batch.models.requests

import org.osmdroid.util.GeoPoint
import java.util.*

data class CircleFence(
    val center: GeoPoint,
    val radius: Double,
    val dateTime : Date?
) {

    //POLYGON (31.900338 54.347477,31.897351 54.350138,31.897788 54.351082,31.900703 54.348335,31.900338 54.347477)
    companion object {
        fun circleFenceToCenterGeoPoint(
            geofencepoint: String, date: Date?
        ) =
            geofencepoint.substring(
                startIndex = geofencepoint.indexOf("(") + 1,
                endIndex = geofencepoint.indexOf(")")
            ).split(",").let { locAndRad ->
                locAndRad[0].split(" ").let { loc ->
                    CircleFence(
                        GeoPoint(loc[0].toDouble(), loc[1].toDouble()),
                        locAndRad[1].toDouble(),
                        date
                    )
                }
            }
    }

}