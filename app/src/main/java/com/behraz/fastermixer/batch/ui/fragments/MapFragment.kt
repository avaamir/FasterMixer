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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutMapBinding
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.osm.MyOSMMapView
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.ui.osm.WorkerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MapViewModel
import com.behraz.fastermixer.batch.viewmodels.MapViewModel.Companion.MIN_LOCATION_UPDATE_TIME
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class MapFragment : Fragment(), LocationListener, MyOSMMapView.OnMapClickListener {


    private var isFirstTime = true
    private var btnMyLocationId: Int =
        0 //btnMylocation Mitune tu activity bashe niaz hast refrencesh ro dashte bashim age tu activity hast
    private lateinit var mapViewModel: MapViewModel
    private lateinit var pompViewModel: PompActivityViewModel

    private lateinit var mBinding: LayoutMapBinding
    private lateinit var btnMyLocation: FloatingActionButton


    private lateinit var userMarker: WorkerMarker

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
        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        pompViewModel = ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)


        Configuration.getInstance()
            .load(context, context!!.getSharedPreferences("osm_map", Context.MODE_PRIVATE))


        btnMyLocationId = arguments?.getInt(BUNDLE_BTN_MY_LOC_ID) ?: 0
        initViews()
        initMapSettings()
        //setupLocationManager() //TODo felan data ra az gps ru machine migirim, yaani app android az server mikhune
        subscribeObservers()

        return mBinding.root
    }

    private fun subscribeObservers() {

        pompViewModel.pompArea.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapViewModel.myLocation = it.center
                userMarker.position = it.center
                if (isFirstTime) {
                    isFirstTime = false
                    moveCamera(it.center)
                }
            }
        })


        pompViewModel.mixers.observe(viewLifecycleOwner, Observer {
            if (it?.isSucceed == true) {
                val excludeMarkerList =
                    ArrayList(mapViewModel.markers.keys) // we need this for exclude mixers from original list if mixer not exists in new mixerList
                it.entity?.forEach { mixer ->
                    excludeMarkerList.remove(mixer.id) //do not need to exclude this mixer because it's exists in new List too
                    val mixerMarker = mapViewModel.markers[mixer.id]
                    if (mixerMarker == null) { //new mixer in pomp incoming list (taze az batch kharej shode va dare be pomp mire)
                        //add to marker hash map and MapView
                        mapViewModel.markers[mixer.id] =
                            MixerMarker(mBinding.map).also { _marker ->
                                _marker.position = mixer.latLng
                                addMarkerToMap(_marker, mixer.latLng, "${mixer.carName} ${mixer.driverName}")
                            }
                    } else { //mixer already exists, update location
                        mixerMarker.position = mixer.latLng
                    }
                }
                excludeMarkerList.forEach { mixerId ->
                    mBinding.map.overlayManager.remove(
                        mapViewModel.markers.remove(mixerId)
                    )
                }
            }
        })



        mapViewModel.getRouteResponse.observe(viewLifecycleOwner, Observer {
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
            moveCamera(mapViewModel.myLocation)
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
        val startPoint = GeoPoint(31.89141833223403, 54.353561131283634)
        mBinding.map.controller.run {
            setZoom(15.5)
            setCenter(startPoint)
        }

        addMarkerToMap(
            marker = WorkerMarker(mBinding.map).also { this.userMarker = it },
            point = mapViewModel.myLocation
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
        point: GeoPoint,
        title: String = "مکان شما"
    ) {
        marker.position = point
        marker.title = title
        mBinding.map.overlays.add(marker)
    }


    fun moveCamera(geoPoint: GeoPoint, zoom: Double = 15.0) {
        mBinding.map.controller.run {
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
                MIN_LOCATION_UPDATE_TIME,
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