package com.behraz.fastermixer.batch.ui.fragments

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Environment
import androidx.lifecycle.Observer
import com.behraz.fastermixer.batch.app.LocationCompassProvider
import com.behraz.fastermixer.batch.ui.osm.DestMarker
import com.behraz.fastermixer.batch.ui.osm.DriverInfoWindow
import com.behraz.fastermixer.batch.ui.osm.ImageMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.MapTileProviderArray
import org.osmdroid.tileprovider.modules.*
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import java.io.File
import kotlin.math.abs

class BaseMapFragmentImpl : BaseMapFragment() {
    private lateinit var userMarker: ImageMarker
    //private var routePolyline: Polyline? = null

    override val myLocation: GeoPoint?
        get() {
            val mLoc = LocationCompassProvider.location.value
            if (mLoc != null) {
                return GeoPoint(mLoc.latitude, mLoc.longitude)
            }
            return Constants.mapStartPoint
        }

    override fun onBtnMyLocationClicked() {
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        //unlock the orientation
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun initMapSettings() {
        super.initMapSettings()

        mBinding.map.setUseDataConnection(true)
        //Using Offline Map
        //mBinding.map.setTileSource(MAPQUESTOSM)
        mBinding.map.setTileSource(TileSourceFactory.MAPNIK)

        val startPoint = Constants.mapStartPoint
        // GeoPoint(52.516667, 13.383333)

        val mapViewController: IMapController = mBinding.map.controller
        mapViewController.setZoom(15)
        mapViewController.setCenter(startPoint)


        //marker
        userMarker = DestMarker(mBinding.map, 48, 48)


        "12,ب,735,48".split(",")
            .run {
                userMarker.infoWindow = DriverInfoWindow(mBinding.map).also {
                    it.setPelakText(get(0), get(1), get(2), get(3))
                }
            }

        // marker.image = ContextCompat.getDrawable(context!!, R.drawable.ic_mixer)!!
        userMarker.title = "امیرحسین مهدی پور"
        // marker.snippet = "توضیحات"
        // marker.subDescription = "بیشتر"
        addMarkerToMap(userMarker, startPoint, "")
        mBinding.map.invalidate()


        //poi
        /*CoroutineScope(Dispatchers.IO).launch {
            val poiProvider = NominatimPOIProvider("OSMBonusPackTutoUserAgent")
            //com.behraz.fastermixer.batch.models.requests.openweathermap.List of facilities is in strings.xml
            val pois: ArrayList<POI> = poiProvider.getPOICloseTo(startPoint, "Fuel", 50, 0.1)

            val poiMarkers = FolderOverlay(context)
            withContext(Dispatchers.Main) {
                mBinding.map.overlays.add(poiMarkers)

                val poiIcon: Drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_map)!!
                pois.forEach { poi ->
                    val poiMarker = Marker(mBinding.map)
                    poiMarker.title = poi.mType
                    poiMarker.snippet = poi.mDescription
                    poiMarker.position = poi.mLocation
                    poiMarker.icon = poiIcon
                    if (poi.mThumbnail != null) {
                        poiMarker.image = BitmapDrawable(poi.mThumbnail)
                    }
                    poiMarkers.add(poiMarker)
                    mBinding.map.invalidate()
                }
            }
        }*/

        //gereftan poi haye yek masir
        //val pois = poiProvider.getPOIAlong(road.getRouteLow(), "fuel", 50, 2.0)

        ////////////////////
        /*mBinding.map.controller.setZoom(14)
        mBinding.map.controller.setCenter(Constants.mapStartPoint)
        mBinding.map.setMultiTouchControls(true)

        setMapOfflineSource()*/


        //==rotation gesture===========================================
        /*val mRotationGestureOverlay = RotationGestureOverlay(mBinding.map)
        mRotationGestureOverlay.isEnabled = true
        mBinding.map.setMultiTouchControls(true)
        mBinding.map.overlays.add(mRotationGestureOverlay)*/


        LocationCompassProvider.fixDeviceOrientationForCompassCalculation(activity!!)
        LocationCompassProvider.start(context!!)

        LocationCompassProvider.location.observe(viewLifecycleOwner, Observer { location ->
            //TODO age location va lastLocation kheli fasele dasht dg animate nashe va mostaghim bere un noghte
            val point = GeoPoint(location.latitude, location.longitude)
            animateMarker(userMarker, point)
            println("debuxL:$point")

            /*lastLocation?.let {
                if (it.distanceToAsDouble(point) > 10) {
                    mBinding.map.controller.setCenter(point)
                }
            }
            lastLocation = point*/
            // toast("new Location")
        })

        LocationCompassProvider.userAngle.observe(viewLifecycleOwner, Observer {
            println("debug:$it")
            if (abs(it.angle - lastOrientation) > 0.5f) {
                lastOrientation = it.angle
                println("debug2:$it")
                animateCameraToMapOrientation(it.angle)
            }
        })

        LocationCompassProvider.northAngle.observe(viewLifecycleOwner, Observer {
            println("debugN: $it")
        })


    }

    private var lastLocation: GeoPoint? = null
    var lastOrientation = 0f

    override fun onDestroy() {
        super.onDestroy()
        LocationCompassProvider.stop(context!!)
    }

    override fun onMapTapped(geoPoint: GeoPoint) {
        //animateMarker(userMarker, geoPoint)
        val destRotation = userMarker.rotation - 90

        println("debux: INIT: ${mBinding.map.mapOrientation}")
        //rotateMarker(userMarker, destRotation)

        // animateCameraToMapOrientation(mBinding.map.mapOrientation - 90)

    }

    private fun setMapOfflineSource() {
        val validFiles = ArrayList<File>()
        val f = File(Environment.getExternalStorageDirectory().absolutePath + "/osmdroid/")
        if (f.exists()) {
            println("debux:file found")
            val files = f.listFiles()
            println("debux: $files")
            if (files != null) {
                println("debux:files is not null")
                for (file in files) {
                    if (file.isDirectory) {
                        println("debux: ${file.name} isDir")
                        continue
                    }
                    var name = file.name.toLowerCase()
                    if (!name.contains(".")) {
                        println("debux: ${file.name} , no extension")
                        continue
                    }
                    name = name.substring(name.lastIndexOf(".") + 1)
                    if (name.isEmpty()) {
                        println("debux: ${file.name} , no extension")
                        continue
                    }


                    println("debux:isArchiveSupported?")
                    if (ArchiveFileFactory.isFileExtensionRegistered(name)) {
                        println("debux:isArchiveSupported? -> YES ${file.name}")
                        validFiles.add(file)
                    }
                }
                if (validFiles.isNotEmpty()) {
                    try {
                        val tileProvider = OfflineTileProvider(
                            SimpleRegisterReceiver(activity),
                            validFiles.toTypedArray()
                        )

                        mBinding.map.tileProvider = tileProvider

                        val archives = tileProvider.archives
                        if (archives.isNotEmpty()) {
                            val tileSources = archives[1].tileSources
                            if (tileSources.isNotEmpty()) {
                                val source = tileSources.iterator().next()
                                mBinding.map.setTileSource(FileBasedTileSource.getSource(source))
                                println(
                                    "debux:FINISHED , source: ${
                                        FileBasedTileSource.getSource(
                                            source
                                        )
                                    }"
                                )
                            } else {
                                println("debux:FINISHED->DEFAULT")
                                mBinding.map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                            }
                        } else {
                            println("debux:FINISHED->DEFAULT")
                            mBinding.map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                        }
                        mBinding.map.invalidate()
                        //TODO mapListener.mapLoadSuccess(map, mapUtils)
                        return
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        //TODO mapListener.mapLoadFailed(ex.toString())
                    }
                }
            }
        } else {
            //TODO should download Map and then call `setMapOfflineSource`
            println("debux:file not found")
            /*if (!FileUtils.isMapFileExists()) {
                FileUtils.copyMapFilesToSdCard(activity, object : FileTransferListener() {
                    fun onLoadFailed() {
                        //WARNING Fabric.getInstance() custom event
                        mapListener.mapLoadFailed("")
                    }

                    fun onLoadSuccess() {
                        setMapOfflineSource()
                    }
                })
            }*/
        }
    }
}

class MyCustomTileProvider(context: Context, mGemfArchiveFilename: File, mapView: MapView) {
    init {
        val registerReceiver = SimpleRegisterReceiver(context)

// Create a custom tile source
        val tileSource = XYTileSource(
            "Mapnik", 1, 18, 256, ".png",
            arrayOf(
                "https://a.tile.openstreetmap.org/",
                "https://b.tile.openstreetmap.org/",
                "https://c.tile.openstreetmap.org/"
            )
        )

// Create a file cache modular provider
        val tileWriter = TileWriter()
        val fileSystemProvider = MapTileFilesystemProvider(
            registerReceiver, tileSource
        )

// Create an archive file modular tile provider
        val gemfFileArchive =
            GEMFFileArchive.getGEMFFileArchive(mGemfArchiveFilename) // Requires try/catch
        val fileArchiveProvider = MapTileFileArchiveProvider(
            registerReceiver, tileSource, arrayOf(gemfFileArchive)
        )

// Create a download modular tile provider
        val networkAvailabilityCheck = NetworkAvailabliltyCheck(context)
        val downloaderProvider = MapTileDownloader(
            tileSource, tileWriter, networkAvailabilityCheck
        )

// Create a custom tile provider array with the custom tile source and the custom tile providers
        val tileProviderArray = MapTileProviderArray(
            tileSource,
            registerReceiver,
            arrayOf(fileSystemProvider, fileArchiveProvider, downloaderProvider)
        )

// Create the mapview with the custom tile provider array
        mapView.tileProvider = tileProviderArray
    }
}

val MAPQUESTOSM = XYTileSource(
    "MapquestOSM",
    0,
    17,
    256,
    ".png",
    arrayOf("http://openptmap.org/tiles/"),
    "© OpenStreetMap contributors"
)