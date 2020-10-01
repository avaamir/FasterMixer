package com.behraz.fastermixer.batch.ui.fragments

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutMapBinding
import com.behraz.fastermixer.batch.ui.osm.MyOSMMapView
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.LocationHandler
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.utils.map.MyMapTileSource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

//** read overpassAPI -> https://github.com/MKergall/osmbonuspack/wiki/OverpassAPIProvider

abstract class BaseMapFragment : Fragment(),
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
        _mBinding.map.overlays.add(line)
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

    fun rotateMarker(
        marker: Marker,
        destRotation: Float,
        interpolator: TimeInterpolator = LinearInterpolator()
    ) {
        /**
         * rotation
         * jam shodan => anti clock wise
         * tafrigh shdan => clock wise
         **/
        val startRotation = marker.rotation
        if (startRotation == destRotation) {
            return
        }
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 500L
        animator.interpolator = interpolator
        animator.addUpdateListener {
            marker.rotation = startRotation + ((destRotation - startRotation) * it.animatedFraction)
            _mBinding.map.postInvalidate()
        }
        animator.start()
    }

    fun animateCameraToMapOrientation(
        destOrientation: Float,
        interpolator: TimeInterpolator = LinearInterpolator()
    ) {
        val startOrientation = mBinding.map.mapOrientation
        if (destOrientation == startOrientation) {
            return
        }
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000L
        animator.interpolator = interpolator
        animator.addUpdateListener {
            mBinding.map.mapOrientation =
                startOrientation + ((destOrientation - startOrientation) * it.animatedFraction)
            println("debux: ${mBinding.map.mapOrientation}")
            mBinding.map.postInvalidate()
        }
        animator.start()
    }

    fun animateMarker(
        marker: Marker,
        destGeoPoint: GeoPoint,
        interpolator: TimeInterpolator = LinearInterpolator()
    ) {
        if ((marker.position.latitude == destGeoPoint.latitude) && (marker.position.longitude == destGeoPoint.longitude))
            return
        //val projection = mBinding.map.projection
        //val startPoint = projection.toPixels(marker.position, null)
        val startGeoPoint = marker.position //projection.fromPixels(startPoint.x, startPoint.y)
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.interpolator = interpolator
        animator.duration = 500L

        animator.addUpdateListener {
            val weight = it.animatedFraction
            val lng = weight * destGeoPoint.longitude + (1 - weight) * startGeoPoint.longitude
            val lat = weight * destGeoPoint.latitude + (1 - weight) * startGeoPoint.latitude

            marker.position = GeoPoint(lat, lng)
            _mBinding.map.postInvalidate()
        }
        animator.start()
    }


    fun moveCamera(geoPoint: GeoPoint, zoom: Double = 18.0) {
        _mBinding.map.controller.run {
            setCenter(geoPoint)
            zoomTo(zoom)
            // animateTo(geoPoint)
        }
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
