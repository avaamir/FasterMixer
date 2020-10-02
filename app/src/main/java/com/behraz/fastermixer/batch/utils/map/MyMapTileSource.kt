package com.behraz.fastermixer.batch.utils.map

import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.MapTileIndex


object MyMapTileSource {


    val OpenWeatherMap: OnlineTileSourceBase by lazy { //https://openweathermap.org/api/weathermaps -> list of layers are here, layers will replace with /temp_new/ in base url for different layer
        object : XYTileSource(
            "GoogleAlteredRoadMap",
            0, 19, 256, ".png",
            arrayOf(
                "https://tile.openweathermap.org/map"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return baseUrl.toString() + "/temp_new/${
                    MapTileIndex.getZoom(pMapTileIndex)
                }/${
                    MapTileIndex.getX(pMapTileIndex)
                }/${
                    MapTileIndex.getY(pMapTileIndex)
                }.png?appid=${Constants.OPEN_WEATHER_MAP_ACCESS_TOKEN}"
            }
        }
    }

    val MapBoxSat by lazy {
        MapBoxTileSource().also {
            it.accessToken = Constants.MAPBOX_ACCESS_TOKEN
            it.setMapboxMapid("mapbox.satellite")
        }
    }

    val HOT by lazy {
        XYTileSource(
            "HOT", 1, 20, 256, ".png", arrayOf(
                "http://a.tile.openstreetmap.fr/hot/",
                "http://b.tile.openstreetmap.fr/hot/"
            ), "© OpenStreetMap contributors"
        )
    }


    val GoogleAlteredRoadMap: OnlineTileSourceBase by lazy {
        object : XYTileSource(
            "GoogleAlteredRoadMap",
            0, 19, 256, ".png", arrayOf(
                "http://mt0.google.com",
                "http://mt1.google.com",
                "http://mt2.google.com",
                "http://mt3.google.com"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String? {
                return baseUrl.toString() + "/vt/lyrs=r&x=${MapTileIndex.getX(pMapTileIndex)}&y=${
                    MapTileIndex.getY(
                        pMapTileIndex
                    )
                }&z=${MapTileIndex.getZoom(pMapTileIndex)}"
            }
        }
    }

    val GoogleStandardRoadMap: OnlineTileSourceBase by lazy {
        object : XYTileSource(
            "GoogleStandardRoadMap",
            0, 19, 256, ".png", arrayOf(
                "http://mt0.google.com",
                "http://mt1.google.com",
                "http://mt2.google.com",
                "http://mt3.google.com"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String? {
                return baseUrl.toString() + "/vt/lyrs=m&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                    pMapTileIndex
                ) + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
            }
        }
    }

    val GoogleTerrainOnly: OnlineTileSourceBase by lazy {
        object : XYTileSource(
            "GoogleTerrainOnly",
            0, 19, 256, ".png", arrayOf(
                "http://mt0.google.com",
                "http://mt1.google.com",
                "http://mt2.google.com",
                "http://mt3.google.com"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String? {
                return baseUrl.toString() + "/vt/lyrs=t&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                    pMapTileIndex
                ) + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
            }
        }
    }

    val GoogleHybrid: OnlineTileSourceBase by lazy {
        object : XYTileSource(
            "Google-Hybrid",
            0, 19, 256, ".png", arrayOf(
                "http://mt0.google.com",
                "http://mt1.google.com",
                "http://mt2.google.com",
                "http://mt3.google.com"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String? {
                return baseUrl.toString() + "/vt/lyrs=y&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                    pMapTileIndex
                ) + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
            }
        }
    }


    val GoogleSat by lazy {
        object : XYTileSource(
            "Google-Sat",
            0, 19, 256, ".png", arrayOf(
                "http://mt0.google.com",
                "http://mt1.google.com",
                "http://mt2.google.com",
                "http://mt3.google.com"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String? {
                return baseUrl.toString() + "/vt/lyrs=s&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                    pMapTileIndex
                ) + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
            }
        }
    }

    val GoogleRoads: OnlineTileSourceBase by lazy {
        object : XYTileSource(
            "Google-Roads",
            0, 19, 256, ".png", arrayOf(
                "http://mt0.google.com",
                "http://mt1.google.com",
                "http://mt2.google.com",
                "http://mt3.google.com"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String? {
                return baseUrl.toString() + "/vt/lyrs=h&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                    pMapTileIndex
                ) + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
            }
        }
    }

    val GoogleTerrain: OnlineTileSourceBase by lazy {
        object : XYTileSource(
            "Google-Terrain",
            0, 19, 256, ".png", arrayOf(
                "http://mt0.google.com",
                "http://mt1.google.com",
                "http://mt2.google.com",
                "http://mt3.google.com"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String? {
                return baseUrl.toString() + "/vt/lyrs=p&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                    pMapTileIndex
                ) + "&z=" + MapTileIndex.getZoom(pMapTileIndex)
            }
        }
    }

}