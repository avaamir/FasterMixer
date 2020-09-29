package com.behraz.fastermixer.batch.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutMapBinding
import com.behraz.fastermixer.batch.ui.customs.fastermixer.CarIdView
import com.behraz.fastermixer.batch.ui.osm.DestMarker
import com.behraz.fastermixer.batch.ui.osm.DriverInfoWindow
import com.behraz.fastermixer.batch.ui.osm.MyOSMMapView
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.LocationHandler
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.utils.map.MyMapTileSource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.location.POI
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderArray
import org.osmdroid.tileprovider.modules.*
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import java.io.File

abstract class BaseMapFragment : Fragment(), LocationListener,
    MyOSMMapView.OnMapClickListener {

    private var currentTileSourceIndex = 0

    abstract val myLocation: GeoPoint?
    abstract fun onBtnMyLocationClicked()

    private var btnMyLocationId: Int =
        0 //btnMylocation Mitune tu activity bashe niaz hast refrencesh ro dashte bashim age tu activity hast
    private var btnLayersId: Int = 0

    private lateinit var _mBinding: LayoutMapBinding
    protected val mBinding get() = _mBinding
    protected lateinit var btnMyLocation: FloatingActionButton
    protected lateinit var btnLayers: FloatingActionButton

    protected companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
        const val BUNDLE_BTN_MY_LOC_ID = "btn-m-loc"
        const val BUNDLE_BTN_LAYERS_ID = "btn-layers"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_map, container, false)


        Configuration.getInstance()
            .load(context, context!!.getSharedPreferences("osm_map", Context.MODE_PRIVATE))


        btnMyLocationId = arguments?.getInt(BUNDLE_BTN_MY_LOC_ID) ?: 0
        btnLayersId = arguments?.getInt(BUNDLE_BTN_LAYERS_ID) ?: 0

        initViews()
        initMapSettings()
        //setupLocationManager() //TODo felan data ra az gps ru machine migirim, yaani app android az server mikhune


        return _mBinding.root
    }

    protected open fun initViews() {
        if (btnMyLocationId != 0) {
            _mBinding.btnFragmentMyLocation.visibility = View.GONE
            btnMyLocation = activity!!.findViewById(btnMyLocationId)
        } else {
            btnMyLocation = _mBinding.btnFragmentMyLocation
        }

        if (btnLayersId != 0) {
            _mBinding.btnLayers.visibility = View.GONE
            btnLayers = activity!!.findViewById(btnLayersId)
        } else {
            btnLayers = _mBinding.btnLayers
        }

        btnLayers.setOnClickListener {
            currentTileSourceIndex++
            when (currentTileSourceIndex) {
                //1 -> setTileMapSource(MyMapTileSource.HOT)
                1 -> setTileMapSource(MyMapTileSource.GoogleHybrid)
                2 -> setTileMapSource(MyMapTileSource.GoogleStandardRoadMap)
                else -> {
                    setTileMapSource(TileSourceFactory.MAPNIK)
                    currentTileSourceIndex = 0
                }
            }
            mBinding.map.invalidate()
        }

        btnMyLocation.setOnClickListener {
            if (myLocation != null) {
                moveCamera(myLocation!!)
                it.animate().apply {
                    interpolator = LinearInterpolator()
                    duration = 500
                    rotationBy(360f)
                    onBtnMyLocationClicked()
                }.start()
            } else {
                toast("درحال دریافت موقعیت شما..")
            }
        }
    }

    protected fun drawPolyline(points: List<GeoPoint>): Polyline {
        val line = Polyline()
        line.setPoints(points)
        line.outlinePaint.color = Color.BLUE
        _mBinding.map.overlayManager.add(line)
        //moveCamera(GeoPoint(line.bounds.centerLatitude, line.bounds.centerLongitude), 1.0) //todo how move camera to polygon area
        //line.outlinePaint.strokeWidth = 3f
        _mBinding.map.invalidate()
        return line
    }


    fun setTileMapSource(tileSource: ITileSource) {
        _mBinding.map.setTileSource(tileSource)
    }

    protected open fun initMapSettings() {
        //default value
        _mBinding.map.setTileSource(TileSourceFactory.MAPNIK)
        //

        _mBinding.map.setMultiTouchControls(true)


        //set default location
        _mBinding.map.controller.run {
            setZoom(15.5)
            setCenter(Constants.mapStartPoint)
        }

        //TODO my location on map
        /*val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), _mBinding.map)
        mLocationOverlay.setPersonIcon(getBitmapFromVectorDrawable(context!!, R.drawable.ic_mixer))
        mLocationOverlay.enableMyLocation()
        _mBinding.map.overlays.add(mLocationOverlay)*/


        //add icon on map with click
        //your items

        /*val mCompassOverlay = CompassOverlay(context, InternalCompassOrientationProvider(context), _mBinding.map)
        mCompassOverlay.enableCompass()
        _mBinding.map.overlays.add(mCompassOverlay)*/

        _mBinding.map.setOnMapClickListener(this)
    }

    //TODO add this to sub classes NOT HEEREEEEEEEEEEEEEEEEEEEEEEEEE
    protected fun addUserMarkerToMap(marker: Marker) {
        addMarkerToMap(
            marker = marker,
            point = myLocation!!
        )
    }

    protected fun addMarkerToMap(
        marker: Marker,
        point: GeoPoint,
        title: String = "مکان شما"
    ) {
        marker.position = point
        marker.title = title
        _mBinding.map.overlays.add(marker)
    }


    fun moveCamera(geoPoint: GeoPoint, zoom: Double = 18.0) {
        _mBinding.map.controller.run {
            setCenter(geoPoint)
            zoomTo(zoom)
            // animateTo(geoPoint)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationManager() {

        val locationManager =
            activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (
            locationManager.allProviders.contains(LocationManager.NETWORK_PROVIDER)
        ) {
            println("debug: allProviders contains NETWORK_PROVIDER")
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                60, //MIN_LOCATION_UPDATE_TIME,
                5f,
                this
            )

            val lastNetworkKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (lastNetworkKnownLocation != null) {
                onLocationChanged(lastNetworkKnownLocation)
            }
        }

        if (
            locationManager.allProviders.contains(LocationManager.GPS_PROVIDER)
        ) {
            println("debug: allProviders contains GPS_PROVIDER")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LocationHandler.MIN_LOCATION_UPDATE_TIME,
                0f,
                this
            )
            val lastGpsKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastGpsKnownLocation != null) {
                onLocationChanged(lastGpsKnownLocation)
            }
            println("debug: lastGpsKnownLocation: $lastGpsKnownLocation")
        }

        /*if (
            locationManager.allProviders.contains(LocationManager.PASSIVE_PROVIDER)
        ) {
            println("debug: allProviders contains GPS_PROVIDER")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME,
                0f,
                this
            )
            val lastGpsKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            if (lastGpsKnownLocation != null) {
                onLocationChanged(lastGpsKnownLocation)
            }
            println("debug: lastGpsKnownLocation: $lastGpsKnownLocation")
        }*/

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        val permissionsToRequest: ArrayList<String?> = ArrayList()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                activity!!,
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        _mBinding.map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        _mBinding.map.onPause() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onLocationChanged(location: Location) {
        /* mapViewModel.myLocation = GeoPoint(location.latitude, location.longitude)
        mapViewModel.saveLocation(location)*/
    }

    override fun onStatusChanged(
        provider: String?,
        status: Int,
        extras: Bundle?
    ) {
        println("debug:onStatusChanged: $provider")
    }

    @SuppressLint("MissingPermission")
    override fun onProviderEnabled(provider: String?) {
        //TODO in bayad hatman gps bashe
        println("debug:onProviderEnabled: $provider")
        btnMyLocation.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.ic_gps
            )
        )
    }

    override fun onProviderDisabled(provider: String?) {
        //TODO in bayad hatman gps bashe
        println("debug:onProviderDisabled: $provider")
        btnMyLocation.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.ic_gps_off
            )
        )
    }

    override fun onMapTapped(geoPoint: GeoPoint) {
        /*println("debug: onMapTapped: " + geoPoint.latitude + " , " + geoPoint.longitude)

        if (shouldAddMarker) {
            shouldAddMarker = false
            truckMarker = SimpleMarker(mBinding.map)
            addMarkerToMap(truckMarker, geoPoint)
        }
        truckMarker.position = geoPoint
        moveCamera(geoPoint, mBinding.map.zoomLevelDouble)


        //TODO test purpose get this from server
        mapViewModel.getRoute(
            listOf(
                mapViewModel.myLocation,
                geoPoint
            )
        )*/
    }

    fun showLayersButton(shouldShow: Boolean) {
        if (shouldShow)
            _mBinding.btnLayers.visibility = View.VISIBLE
        else
            _mBinding.btnLayers.visibility = View.GONE
    }


}


class BaseMapFragmentImpl : BaseMapFragment() {

    override val myLocation: GeoPoint?
        get() = Constants.mapStartPoint

    override fun onBtnMyLocationClicked() {
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
        val marker = DestMarker(mBinding.map, 48, 48)


        "12,ب,735,48".split(",")
            .run {
                marker.infoWindow = DriverInfoWindow(mBinding.map).also {
                    it.setPelakText(get(0), get(1), get(2), get(3))
                }
            }

       // marker.image = ContextCompat.getDrawable(context!!, R.drawable.ic_mixer)!!
        marker.title = "امیرحسین مهدی پور"
       // marker.snippet = "توضیحات"
       // marker.subDescription = "بیشتر"
        addMarkerToMap(marker, startPoint, "")
        mBinding.map.invalidate()


        //poi
        CoroutineScope(IO).launch {
            val poiProvider = NominatimPOIProvider("OSMBonusPackTutoUserAgent")
            //List of facilities is in strings.xml
            val pois: ArrayList<POI> = poiProvider.getPOICloseTo(startPoint, "Fuel", 50, 0.1)

            val poiMarkers = FolderOverlay(context)
            withContext(Main) {
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
        }

        //gereftan poi haye yek masir
        //val pois = poiProvider.getPOIAlong(road.getRouteLow(), "fuel", 50, 2.0)

        ////////////////////
        /*mBinding.map.controller.setZoom(14)
        mBinding.map.controller.setCenter(Constants.mapStartPoint)
        mBinding.map.setMultiTouchControls(true)

        setMapOfflineSource()*/

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
        val networkAvailablityCheck = NetworkAvailabliltyCheck(context)
        val downloaderProvider = MapTileDownloader(
            tileSource, tileWriter, networkAvailablityCheck
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

