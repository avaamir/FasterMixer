package com.behraz.fastermixer.batch.models.requests

import com.behraz.fastermixer.batch.utils.map.MapUtils
import org.osmdroid.util.GeoPoint


interface Fence {
    companion object {
        fun strToFence(fenceStr: String) =
            when {
                fenceStr.contains("CIRCLE", true) -> {
                    CircleFence.strToCircleFence(fenceStr)
                }
                fenceStr.contains("POLYGON", true) -> {
                    PolygonFence.strToPolygonFence(fenceStr)
                }
                else -> throw IllegalArgumentException("fenceStr must contains either cirlce or polygon, other formats not supported")
            }
    }

    val center: GeoPoint
    fun distanceTo(point: GeoPoint): Double
    fun contains(point: GeoPoint): Boolean
}


data class CircleFence(
    override val center: GeoPoint,
    val radius: Double
) : Fence {
    override fun distanceTo(point: GeoPoint) = point.distanceToAsDouble(center)
    override fun contains(point: GeoPoint) = point.distanceToAsDouble(center) < radius

    internal companion object {
        fun strToCircleFence(
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

data class PolygonFence(val points: List<GeoPoint>) : Fence {
    override val center: GeoPoint
        get() {
            var lat = 0.0
            var lon = 0.0
            points.forEach {
                lat += it.latitude
                lon += it.longitude
            }
            return GeoPoint(lat / points.size, lon / points.size)
        }

    override fun distanceTo(point: GeoPoint): Double {
        return point.distanceToAsDouble(points[0]) //TODO use a better algorithm
    }

    override fun contains(point: GeoPoint) = MapUtils.containsLocation(point, points, false)

    internal companion object {
        fun strToPolygonFence(str: String): PolygonFence {
            val points = ArrayList<GeoPoint>()
            str.substring(
                startIndex = str.indexOf("(") + 1,
                endIndex = str.indexOf(")")
            ).let { pointsListStr ->
                //POLYGON (31.900338 54.347477,31.897351 54.350138,31.897788 54.351082,31.900703 54.348335,31.900338 54.347477)
                pointsListStr.split(",").let { pointStr ->
                    pointStr.forEach {
                        val latLonStr = it.split(" ")
                        val point = GeoPoint(latLonStr[0].toDouble(), latLonStr[1].toDouble())
                        points.add(point)
                    }
                }
            }
            return PolygonFence(points)
        }
    }

}


fun String.toGeoPoint() =
    this.substring(
        startIndex = this.indexOf("(") + 1,
        endIndex = this.indexOf(")")
    ).split(",").let { locAndRad ->
        locAndRad[0].split(" ").let { loc ->
            GeoPoint(loc[0].toDouble(), loc[1].toDouble())
        }
    }
