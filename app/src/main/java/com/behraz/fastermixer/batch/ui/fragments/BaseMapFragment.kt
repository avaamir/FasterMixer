package com.behraz.fastermixer.batch.ui.fragments

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutMapBinding
import com.behraz.fastermixer.batch.ui.osm.MyOSMMapView
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.utils.map.MyMapTileSource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import kotlin.math.abs

//** read overpassAPI -> https://github.com/MKergall/osmbonuspack/wiki/OverpassAPIProvider

abstract class BaseMapFragment : Fragment(),
    MyOSMMapView.OnMapClickListener {

    protected val mapAnimationUtil = MapSingleMarkerAnimationUtil()

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
        line.outlinePaint.color = Color.CYAN
        line.outlinePaint.alpha = 100
        _mBinding.map.overlays.add(line)
        //moveCamera(GeoPoint(line.bounds.centerLatitude, line.bounds.centerLongitude), 1.0) //todo how move camera to polygon area
        //line.outlinePaint.strokeWidth = 3f
        _mBinding.map.invalidate()
        return line
    }


    protected fun setTileMapSource(tileSource: ITileSource) {
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

        //TODO TEST CHECK THIS CODE
        _mBinding.map.isHorizontalMapRepetitionEnabled = false
        _mBinding.map.isVerticalMapRepetitionEnabled = false

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

    override fun onDestroyView() {
        _mBinding.map.onDetach()
        super.onDestroyView()
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


    fun rotateMarker(
        marker: Marker,
        destRotation: Float,
        interpolator: TimeInterpolator = LinearInterpolator()
    ) {
        mapAnimationUtil.rotateMarker(_mBinding.map, marker, destRotation, interpolator)
    }

    fun animateCameraToMapOrientation(
        destOrientation: Float,
        interpolator: TimeInterpolator = LinearInterpolator()
    ) {
        mapAnimationUtil.animateCameraToMapOrientation(_mBinding.map, destOrientation, interpolator)
    }

    fun animateMarker(
        marker: Marker,
        destGeoPoint: GeoPoint,
        interpolator: TimeInterpolator = LinearInterpolator()
    ) {
        mapAnimationUtil.animateMarker(_mBinding.map, marker, destGeoPoint, interpolator)
    }


}

class MapSingleMarkerAnimationUtil { //TODO in faghat vase ye marker dorost kar mikone , dorost ine ke rotateMarker va aniamteMarker dar class marker bashe na inja

    private var rotateAnimator: ValueAnimator? = null
    private var moveMarkerAnimator: ValueAnimator? = null
    private var orientationAnimator: ValueAnimator? = null


    private var lastDestOrientation = 0f
    private var lastDestRotation = 0f
    private var lastDestLocation: GeoPoint? = null

    fun rotateMarker(
        map: MapView,
        marker: Marker,
        destRotation: Float,
        interpolator: TimeInterpolator = LinearInterpolator(),
        duration: Long = 500L
    ) {
        /*rotation: jam shodan => anti clock wise , tafrigh shdan => clock wise*/


        if (destRotation == lastDestRotation) {
            return
        }

        lastDestRotation = destRotation
        rotateAnimator?.cancel()

        val startRotation = marker.rotation
        if (startRotation == destRotation) {
            return
        }

        /*if (destRotation > 360) {
            destRotation %= 360
        }
        if (startRotation > 360) {
            startRotation %= 360
        }*/

        var diff = destRotation - startRotation
        if (abs(diff) > 180) {
            if (diff > 0) {
                diff -= 360
            } else {
                diff += 360
            }
        }

        rotateAnimator = ValueAnimator.ofFloat(0f, 1f).also { animator ->
            animator.duration = duration
            animator.interpolator = interpolator
            animator.addUpdateListener {
                marker.rotation =
                    startRotation + ((diff) * it.animatedFraction)
                map.postInvalidate()
            }
            animator.start()
        }

    }

    fun animateCameraToMapOrientation(
        map: MapView,
        destOrientation: Float,
        interpolator: TimeInterpolator = LinearInterpolator(),
        duration: Long = 500L
    ) {

        if (destOrientation == lastDestOrientation) {
            return
        }

        orientationAnimator?.cancel()
        lastDestOrientation = destOrientation

        val startOrientation = map.mapOrientation
        if (destOrientation == startOrientation) {
            return
        }

        var diff = destOrientation - startOrientation
        if (abs(diff) > 180) {
            if (diff > 0) {
                diff -= 360
            } else {
                diff += 360
            }
        }

        orientationAnimator = ValueAnimator.ofFloat(0f, 1f).also { animator ->
            animator.duration = duration
            animator.interpolator = interpolator
            animator.addUpdateListener {
                map.mapOrientation =
                    startOrientation + (diff * it.animatedFraction)
                println("debux: ${map.mapOrientation}")
                map.postInvalidate()
            }
            animator.start()
        }
    }

    fun animateMarker(
        map: MapView,
        marker: Marker,
        destGeoPoint: GeoPoint,
        interpolator: TimeInterpolator = LinearInterpolator(),
        duration: Long = 1000L
    ) {

        if ((lastDestLocation?.latitude == destGeoPoint.latitude) && (lastDestLocation?.longitude == destGeoPoint.longitude)) {
            return
        }

        moveMarkerAnimator?.cancel()
        lastDestLocation = destGeoPoint

        if ((marker.position.latitude == destGeoPoint.latitude) && (marker.position.longitude == destGeoPoint.longitude))
            return

        //val projection = mBinding.map.projection
        //val startPoint = projection.toPixels(marker.position, null)
        val startGeoPoint = marker.position //projection.fromPixels(startPoint.x, startPoint.y)
        moveMarkerAnimator = ValueAnimator.ofFloat(0f, 1f).also { animator ->
            animator.interpolator = interpolator
            animator.duration = duration

            animator.addUpdateListener {
                val weight = it.animatedFraction
                val lng = weight * destGeoPoint.longitude + (1 - weight) * startGeoPoint.longitude
                val lat = weight * destGeoPoint.latitude + (1 - weight) * startGeoPoint.latitude

                marker.position = GeoPoint(lat, lng)
                map.postInvalidate()
            }
            animator.start()
        }
    }

}

