package com.behraz.fastermixer.batch.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
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
import com.behraz.fastermixer.batch.ui.osm.ImageMarker
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.ui.osm.MyOSMMapView
import com.behraz.fastermixer.batch.utils.general.LocationHandler
import com.behraz.fastermixer.batch.utils.general.getBitmapFromVectorDrawable
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

abstract class BaseMapFragment : Fragment(), LocationListener,
    MyOSMMapView.OnMapClickListener {

    abstract val myLocation: GeoPoint
    abstract fun onBtnMyLocationClicked()

    private var btnMyLocationId: Int =
        0 //btnMylocation Mitune tu activity bashe niaz hast refrencesh ro dashte bashim age tu activity hast

    private lateinit var _mBinding: LayoutMapBinding
    protected val mBinding get() = _mBinding
    protected lateinit var btnMyLocation: FloatingActionButton

    protected companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
        const val BUNDLE_BTN_MY_LOC_ID = "btn-m-loc"
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
        initViews()
        initMapSettings()
        //setupLocationManager() //TODo felan data ra az gps ru machine migirim, yaani app android az server mikhune

        return _mBinding.root
    }

    protected open fun initViews() {
        if (btnMyLocationId != 0) {
            _mBinding.btnMyLocation.visibility = View.GONE
            btnMyLocation = activity!!.findViewById(btnMyLocationId)
        } else {
            btnMyLocation = _mBinding.btnMyLocation
        }

        btnMyLocation.setOnClickListener {
            moveCamera(myLocation)
            it.animate().apply {
                interpolator = LinearInterpolator()
                duration = 500
                rotationBy(360f)
                onBtnMyLocationClicked()
            }.start()
        }
    }

    protected fun drawPolyline(points: List<GeoPoint>): Polyline {
        val line = Polyline()
        line.setPoints(points)
        line.outlinePaint.color = Color.BLUE
        _mBinding.map.overlayManager.add(line)
        //moveCamera(GeoPoint(line.bounds.centerLatitude, line.bounds.centerLongitude), 1.0) //todo how move camera to polygon area
        //line.outlinePaint.strokeWidth = 3f
        return line
    }


    protected fun initMapSettings() {
        _mBinding.map.setTileSource(TileSourceFactory.MAPNIK)
        _mBinding.map.setMultiTouchControls(true)


        //set default location
        val startPoint = GeoPoint(31.89141833223403, 54.353561131283634)
        _mBinding.map.controller.run {
            setZoom(15.5)
            setCenter(startPoint)
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
            point = myLocation
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


    fun moveCamera(geoPoint: GeoPoint, zoom: Double = 15.0) {
        _mBinding.map.controller.run {
            setCenter(geoPoint)
            zoomTo(zoom)
            animateTo(geoPoint)
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


}