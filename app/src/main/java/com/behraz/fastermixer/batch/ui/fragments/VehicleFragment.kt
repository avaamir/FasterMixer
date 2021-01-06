package com.behraz.fastermixer.batch.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.behraz.fastermixer.batch.app.LocationCompassProvider
import com.behraz.fastermixer.batch.app.receivers.isNetworkAvailable
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.models.requests.CircleFence
import com.behraz.fastermixer.batch.models.requests.Fence
import com.behraz.fastermixer.batch.models.requests.PolygonFence
import com.behraz.fastermixer.batch.ui.dialogs.NewMessageDialog
import com.behraz.fastermixer.batch.ui.fragments.pomp.PompMapFragment
import com.behraz.fastermixer.batch.ui.osm.markers.DestMarker
import com.behraz.fastermixer.batch.ui.osm.markers.MixerMarker
import com.behraz.fastermixer.batch.ui.osm.markers.PompMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.log
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.VehicleActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleMapFragmentViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import kotlin.math.abs

abstract class VehicleFragment : BaseMapFragment() {
    private var lastLocation: GeoPoint = Constants.mapStartPoint
    private var lastOrientation: Float = 0f

    private var onUserAndDestLocRetrieved: OnUserAndDestLocRetrieved? = null

    abstract val vehicleActivityViewModel: VehicleActivityViewModel
    abstract val mMapViewModel: VehicleMapFragmentViewModel

    private var routePolyline: Polyline? = null
    private var polygon: Polygon? = null

    private var isDestMarkerAddedToMap = false
    private val destMarker: DestMarker by lazy {
        isDestMarkerAddedToMap = true
        DestMarker(mBinding.map, 42, 42).also {
            addMarkerToMap(
                it,
                it.position,
                "مقصد"
            )
            it.setOnMarkerClickListener { _, _ ->
                it.showInfoWindow()
                true
            }
        }
    }


    private var isFirstCameraMove = true

    //age map ro scroll kard dg track nakone ama age dokme myLocation ro zad trackesh bokone
    protected var shouldCameraTrackUser = true

    /** har kelasi ke az BaseMapFragment Inheritance mikonad in method bayad daresh copy shavad*/
    companion object {
        const val BUNDLE_BTN_ROUTE_ID = "route-btn-id"
    }

    override fun onBtnMyLocationClicked(view: View) {
        super.onBtnMyLocationClicked(view)
        shouldCameraTrackUser = true
    }


    override val myLocation: GeoPoint? get() = mMapViewModel.myLocation

    private val userMarker by lazy {
        (if (this is PompMapFragment) PompMarker(mBinding.map) else MixerMarker(mBinding.map)).also {
            addMarkerToMap(it, it.position)
            it.setOnMarkerClickListener { _, _ ->
                shouldCameraTrackUser = true
                it.showInfoWindow()
                true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        subscribeObservers()
        initLocationCompassProvider()

        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocationCompassProvider.stop(requireContext())
    }

    override fun initViews() {
        super.initViews()
        mBinding.map.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent): Boolean {
                event.run {
                    if (x != 0 || y != 0)
                        shouldCameraTrackUser = false
                }
                return false
            }

            override fun onZoom(event: ZoomEvent): Boolean {
                return false
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireActivity() is OnUserAndDestLocRetrieved) {
            onUserAndDestLocRetrieved = activity as OnUserAndDestLocRetrieved
        }
    }

    private fun initLocationCompassProvider() {
        LocationCompassProvider.fixDeviceOrientationForCompassCalculation(requireActivity())
        LocationCompassProvider.start(requireContext())


        LocationCompassProvider.userAngle.observeForever {
            if (!vehicleActivityViewModel.isServerLocationProvider) {
                if (!it.isCompassProvider) { //TODO dar inja niazi be compass provider nadarim va fahgat gps mohem hast
                    if (abs(it.angle - lastOrientation) > 0.5f) {
                        lastOrientation = it.angle
                        animateCameraToMapOrientation(it.angle)
                    }
                }
            } else {
                if (mBinding.map.mapOrientation != 0f) { //TODO badan alamat compass ham mishe biad va ruye un zad dorost beshe
                    lastOrientation = 0f
                    animateCameraToMapOrientation(0f)
                }
            }
        }

        /*LocationCompassProvider.northAngle.observe(viewLifecycleOwner, Observer {
            println("debugN: $it")
        })*/
    }

    protected open fun subscribeObservers() {
        vehicleActivityViewModel.getUserLocationResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                mMapViewModel.myLocation = it.location
                val distance = lastLocation.distanceToAsDouble(it.location)
                when {
                    distance > 1000 -> {
                        lastLocation = it.location
                        if (shouldCameraTrackUser || isFirstCameraMove) {
                            isFirstCameraMove = false
                            userMarker.position = it.location
                            moveCamera(it.location, shouldAnimate = false)
                            mBinding.map.invalidate()
                        } else {
                            animateMarker(userMarker, it.location)
                        }
                    }
                    distance > 1 -> {
                        //TODO envolve location accrucey in the logic, if circle(center=lastLoc, radius=accrucy).contains(newPoint) don't move
                        lastLocation = it.location
                        animateMarker(userMarker, it.location)
                        if (shouldCameraTrackUser || isFirstCameraMove) {
                            isFirstCameraMove = false
                            moveCamera(it.location)
                        }
                    }
                }

                if (mMapViewModel.hasNewMission) {
                    mMapViewModel.hasNewMission = false
                    onNewMission(vehicleActivityViewModel.newMissionEvent.value!!.peekContent())
                }
                /*lastLocation?.let {
                    if (it.distanceToAsDouble(point) > 10) {
                        mBinding.map.controller.setCenter(point)
                    }
                }
                lastLocation = point*/
            }
        }




        vehicleActivityViewModel.newMissionEvent.observe(viewLifecycleOwner) { event ->
            event.getEventIfNotHandled()?.let { mission ->
                println("debux: (MixerMapFragment-newMissionEventObserver) `newMissionEvent` Handler Routine Start ==================================")
                if (mission === Mission.NoMission) {
                    println("debux: `newMissionEvent` NoMission")
                    mBinding.map.overlays.remove(destMarker)
                    isDestMarkerAddedToMap = false
                    mBinding.map.overlays.remove(routePolyline)
                    mBinding.map.overlays.remove(polygon)
                    routePolyline = null
                    mBinding.map.invalidate()
                    toast("شما ماموریت دیگری ندارید")
                } else {
                    val missionMessage = vehicleActivityViewModel.insertMission(mission)
                    log(missionMessage, "debux1")
                    NewMessageDialog(missionMessage, requireContext()).show()
                    //
                    println("debux: `newMissionEvent` NewMission")
                    destMarker.position = mission.destFence.center
                    destMarker.title = mission.summery
                    mBinding.map.overlays.remove(polygon)
                    polygon = preparePolygon(mission.destFence)
                    mBinding.map.overlays.add(0, polygon)
                    if (!isDestMarkerAddedToMap) { //age routePolyline null bashe yaani halat noMission pish umade va destMarker az map remove shode
                        isDestMarkerAddedToMap = true
                        mBinding.map.overlays.add(destMarker)
                    }
                    mBinding.map.invalidate()
                    if (mMapViewModel.myLocation != null) {
                        onNewMission(mission)
                    } else {
                        println("debux: getRoute() will call after userLoc received from server")
                        mMapViewModel.hasNewMission = true
                    }

                }
            }
        }

        vehicleActivityViewModel.getMissionError.observe(viewLifecycleOwner) { event ->
            event.getEventIfNotHandled()?.let {
                println("debux: (MixerMapFragment) getMissionError: $it")
                if (!it.contains("Action")) {
                    toast(it)
                }
            }
        }

        mMapViewModel.getRouteResponse.observe(viewLifecycleOwner, {
            if (it != null) {
                println("debux: (MixerMapFragment-GetRouteResponseObserver) getRouteResponse Came====================================")
                if (it.isSuccessful) {
                    println("debux: getRouteResponse isSuccessful")
                    routePolyline?.let { _routes ->
                        mBinding.map.overlays.remove(_routes)
                    }
                    routePolyline = drawPolyline(it.getRoutePoints())
                    println("debux: drawPolyline Called: ${routePolyline}, $it ${it.getRoutePoints()}")
                } else {
                    println("debux: getRouteResponse unSuccessful")
                    if (it.code == "NoRoute")
                        toast("متاسفانه در این منطقه مسیریابی ممکن نیست")
                    else
                        toast(Constants.SERVER_ERROR)
                }
            } else {
                println("debux: getRouteResponse null")
                val networkConnected = isNetworkAvailable()
                println("debux: getRouteResponse network:$networkConnected")
                if (networkConnected) {
                    toast(Constants.SERVER_ERROR)
                } else {
                    toast("لطفا وضعیت اینترنت خود را بررسی کنید")
                }
            }
        })
    }

    private fun preparePolygon(fence: Fence): Polygon {
        return when (fence) {
            is CircleFence -> {
                Polygon().also {
                    it.actualPoints.addAll(
                        Polygon.pointsAsCircle(
                            fence.center,
                            fence.radius
                        )
                    )
                }
            }
            is PolygonFence -> {
                Polygon(mBinding.map).also {
                    it.actualPoints.addAll(fence.points)
                }
            }
            else -> throw IllegalStateException("implement this type of polygon")
        }.apply {
            fillPaint.color = Color.CYAN
            fillPaint.alpha = 100
            outlinePaint.color = Color.YELLOW
            outlinePaint.strokeWidth = 2f
            infoWindow = null
        }
    }

    private fun onNewMission(mission: Mission) {
        if (!mission.destFence.contains(mMapViewModel.myLocation!!)) { //not yet enter in fence
            println("debux: getRouteCalled")
            mMapViewModel.getRoute(
                listOf(
                    mMapViewModel.myLocation!!,
                    mission.destFence.center
                )
            )
            onUserAndDestLocRetrieved?.onShowButtons(true)
        } else {
            println("debux: Already in DestArea")
            toast("به مقصد رسیدید")
            onUserAndDestLocRetrieved?.onShowButtons(false)
        }
    }

    fun routeAgain() { //route destLocation // khode server tashkhis dade koja bere
        println("debux: routeAgain()============================================")
        println("debux: routeAgain: ${mMapViewModel.getRouteResponse.value?.getRoutePoints()}")
        //In dokme vaghti visible mishe ke mission ro gerefte bashim va yek bar ham darkhast getRoute ro zade bashim pas niazi nist check konim (startPoint,Dest) reside hast ya na
        mMapViewModel.tryGetRouteAgain()
    }


    fun routeHomeOrDest(isHome: Boolean) {
        val mission = vehicleActivityViewModel.newMissionEvent.value!!.peekContent()
        val fence = if (isHome) mission.batchLocation else mission.requestLocation
        val title = if (isHome) "بچینگ" else "پروژه"
        if (fence != null) {
            mBinding.map.overlays.remove(polygon)
            polygon = preparePolygon(fence)
            mBinding.map.overlays.add(0, polygon)
            destMarker.position = fence.center
            destMarker.title = title
            mBinding.map.invalidate()

            mMapViewModel.getRoute(listOf(mMapViewModel.myLocation!!, fence.center))
        } else {
            toast("آدرس $title نامشخص است")
        }
    }

    interface OnUserAndDestLocRetrieved {
        fun onShowButtons(shouldShow: Boolean)
    }

}