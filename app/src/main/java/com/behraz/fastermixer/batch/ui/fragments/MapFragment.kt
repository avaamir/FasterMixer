package com.behraz.fastermixer.batch.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutMapBinding
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.osm.MyOSMMapView
import com.behraz.fastermixer.batch.ui.osm.SimpleMarker
import com.behraz.fastermixer.batch.ui.osm.WorkerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MapViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class MapFragment : Fragment(), LocationListener, MyOSMMapView.OnMapClickListener {


    private var btnMyLocationId: Int =
        0 //btnMylocation Mitune tu activity bashe niaz hast refrencesh ro dashte bashim age tu activity hast
    private lateinit var viewModel: MapViewModel
    private lateinit var mBinding: LayoutMapBinding
    private lateinit var btnMyLocation: FloatingActionButton

    private lateinit var userMarker: Marker
    private val truckMarker by lazy { SimpleMarker(mBinding.map) } //todo test purpose
    private var routePolyline: Polyline? = null

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
        private const val BUNDLE_BTN_MY_LOC_ID = "btn-m-loc"
        fun newInstance(btnMyLocationId: Int) = MapFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(BUNDLE_BTN_MY_LOC_ID, btnMyLocationId)
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_map, container, false)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)


        Configuration.getInstance()
            .load(context, context!!.getSharedPreferences("osm_map", Context.MODE_PRIVATE))






        requestPermissionsIfNecessary(
            arrayOf(
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        btnMyLocationId = arguments?.getInt(BUNDLE_BTN_MY_LOC_ID) ?: 0
        initViews()
        initMapSettings()
        setupLocationManager()
        subscribeObservers()

        return mBinding.root
    }

    private fun subscribeObservers() {
        viewModel.getRouteResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isSuccessful) {
                    routePolyline?.let { mBinding.map.overlayManager.remove(routePolyline) }
                    routePolyline = drawPolyline(it.getRoutePoints())
                } else {
                    toast(Constants.SERVER_ERROR)
                }
            } else {
                val networkConnected = ApiService.isNetworkAvailable()
                if (networkConnected) {
                    toast(Constants.SERVER_ERROR)
                } else {
                    toast("لطفا وضعیت اینترنت خود را بررسی کنید")
                }
            }
        })
    }

    private fun initViews() {
        if (btnMyLocationId != 0) {
            mBinding.btnMyLocation.visibility = View.GONE
            btnMyLocation = activity!!.findViewById(btnMyLocationId)
        } else {
            btnMyLocation = mBinding.btnMyLocation
        }

        btnMyLocation.setOnClickListener {
            moveCamera(viewModel.myLocation)
            it.animate().apply {
                interpolator = LinearInterpolator()
                duration = 500
                rotationBy(360f)
            }.start()
        }
    }

    private fun drawPolyline(points: List<GeoPoint>): Polyline {
        val line = Polyline()
        line.setPoints(points)
        line.outlinePaint.color = Color.BLUE
        mBinding.map.overlayManager.add(line)
        //moveCamera(GeoPoint(line.bounds.centerLatitude, line.bounds.centerLongitude), 1.0) //todo how move camera to polygon area
        //line.outlinePaint.strokeWidth = 3f
        return line
    }


    private fun initMapSettings() {
        mBinding.map.setTileSource(TileSourceFactory.MAPNIK)
        mBinding.map.setMultiTouchControls(true)


        //set default location
        val mapController = mBinding.map.controller
        mapController.setZoom(15.5)
        val startPoint = GeoPoint(31.89141833223403, 54.353561131283634)
        mapController.setCenter(startPoint)


        userMarker = addMarkerToMap(
            marker = WorkerMarker(mBinding.map),
            point = startPoint
        )


        //TODO my location on map
        /*val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        mLocationOverlay.setPersonIcon(markerBitmap)
        mLocationOverlay.enableMyLocation()
        map.overlays.add(mLocationOverlay)*/


        //add icon on map with click
        //your items


        mBinding.map.setOnMapClickListener(this)

    }

    private fun addMarkerToMap(
        marker: Marker,
        point: GeoPoint
    ): Marker {
        marker.position = point
        marker.title = "مکان شما"
        mBinding.map.overlays.add(marker)
        return marker
    }


    private fun moveCamera(geoPoint: GeoPoint, zoom: Double = 15.0) {
        val controller = mBinding.map.controller
        controller.setCenter(geoPoint)
        controller.zoomTo(zoom)
        controller.animateTo(geoPoint)
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
                0L,
                0f,
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
                0L,
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
                0L,
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


    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context!!, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission)
            }
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
        mBinding.map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mBinding.map.onPause() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onLocationChanged(location: Location) {
        viewModel.myLocation.latitude = location.latitude
        viewModel.myLocation.longitude = location.longitude
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


    private var isFlag = true
    override fun onMapTapped(geoPoint: GeoPoint) {
        println("debug: onMapTapped: " + geoPoint.latitude + " , " + geoPoint.longitude)

        if (isFlag) {
            isFlag = false
            addMarkerToMap(truckMarker, geoPoint)
        }
        truckMarker.position = geoPoint
        moveCamera(geoPoint, mBinding.map.zoomLevelDouble)


        //TODO test purpose get this from server
        viewModel.getRoute(
            listOf(
                viewModel.myLocation,
                geoPoint
            )
        )
    }

}