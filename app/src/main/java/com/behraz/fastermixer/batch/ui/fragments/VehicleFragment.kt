package com.behraz.fastermixer.batch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.osm.DestMarker
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.VehicleMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleActivityViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

abstract class VehicleFragment : BaseMapFragment() {


    abstract val vehicleActivityViewModel: VehicleActivityViewModel
    abstract val mMapViewModel: VehicleMapFragmentViewModel

    private val destMarker: DestMarker by lazy {
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

    private var btnRoute: View? = null

    private var routePolyline: Polyline? = null


    private var isFirstCameraMove = true

    //age map ro scroll kard dg track nakone ama age dokme myLocation ro zad trackesh bokone
    protected var shouldCameraTrackUser = true

    /** har kelasi ke az BaseMapFragment Inheritance mikonad in method bayad daresh copy shavad*/
    companion object {
        const val BUNDLE_BTN_ROUTE_ID = "route-btn-id"
    }

    override fun onBtnMyLocationClicked() {
        shouldCameraTrackUser = true
    }


    override val myLocation: GeoPoint? get() = mMapViewModel.myLocation

    private val userMarker by lazy {
        MixerMarker(mBinding.map).also {
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

        return mBinding.root
    }

    override fun initViews() {
        super.initViews()

        val btnRouteId = arguments?.getInt(BUNDLE_BTN_ROUTE_ID) ?: 0
        if (btnRouteId != 0)
            btnRoute = activity!!.findViewById(btnRouteId)


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


    protected open fun subscribeObservers() {
        vehicleActivityViewModel.userLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mMapViewModel.myLocation = it.circleFence.center
                animateMarker(userMarker, it.circleFence.center)
                if (shouldCameraTrackUser || isFirstCameraMove) {
                    isFirstCameraMove = false
                    moveCamera(it.circleFence.center)
                }
                if (mMapViewModel.hasNewMission) {
                    mMapViewModel.hasNewMission = false
                    onNewMission(vehicleActivityViewModel.newMissionEvent.value!!.peekContent())
                }
            }
        })

        vehicleActivityViewModel.newMissionEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getEventIfNotHandled()?.let { mission ->
                println("debux: (MixerMapFragment-newMissionEventObserver) `newMissionEvent` Handler Routine Start ==================================")
                if (mission === Mission.NoMission) {
                    println("debux: `newMissionEvent` NoMission")
                    mBinding.map.overlays.remove(destMarker)
                    mBinding.map.overlays.remove(routePolyline)
                    routePolyline = null
                    mBinding.map.invalidate()
                    toast("شما ماموریت دیگری ندارید")
                } else {
                    println("debux: `newMissionEvent` NewMission")
                    destMarker.position = mission.destCircleFence.center
                    destMarker.title = mission.conditionTitle
                    if (routePolyline == null) { //age routePolyline null bashe yaani halat noMission pish umade va destMarker az map remove shode
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
        })

        vehicleActivityViewModel.getMissionError.observe(viewLifecycleOwner, Observer { event ->
            event.getEventIfNotHandled()?.let {
                println("debux: (MixerMapFragment) getMissionError: $it")
                if (!it.contains("Action")) {
                    toast(it)
                }
            }
        })

        mMapViewModel.getRouteResponse.observe(viewLifecycleOwner, Observer {
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
                    toast(Constants.SERVER_ERROR)
                }
            } else {
                println("debux: getRouteResponse null")
                val networkConnected = ApiService.isNetworkAvailable()
                println("debux: getRouteResponse network:$networkConnected")
                if (networkConnected) {
                    toast(Constants.SERVER_ERROR)
                } else {
                    toast("لطفا وضعیت اینترنت خود را بررسی کنید")
                }
            }
        })
    }

    private fun onNewMission(mission: Mission) {
        val remainingDistance =
            mMapViewModel.myLocation!!.distanceToAsDouble(mission.destCircleFence.center)
        if (remainingDistance > mission.destCircleFence.radius) {
            println("debux: getRouteCalled")
            mMapViewModel.getRoute(
                listOf(
                    mMapViewModel.myLocation!!,
                    mission.destCircleFence.center
                )
            )
            /*TODO Add Animation*/
            btnRoute?.visibility = View.VISIBLE
        } else {
            println("debux: Already in DestArea")
            toast("به مقصد رسیدید")
            /*TODO Add Animation*/
            btnRoute?.visibility = View.GONE
        }
    }

    fun routeAgain() {
        println("debux: routeAgain()============================================")
        println("debux: routeAgain: ${mMapViewModel.getRouteResponse.value?.getRoutePoints()}")
        //In dokme vaghti visible mishe ke mission ro gerefte bashim va yek bar ham darkhast getRoute ro zade bashim pas niazi nist check konim (startPoint,Dest) reside hast ya na
        mMapViewModel.tryGetRouteAgain()
    }


}