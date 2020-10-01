package com.behraz.fastermixer.batch.ui.fragments.mixer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.osm.DestMarker
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.now
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.MixerMapFragmentViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline


class MixerMapFragment : BaseMapFragment() {


    private var routePolyline: Polyline? = null
        set(value) {
            println("debux: Setter: before:$field new:$value, ${now()}")
            field = value
        }

    private var btnRoute: View? = null

    private var isFirstCameraMove = true
    private var shouldCameraTrackMixer =
        true //age map ro scroll kard dg track nakone ama age dokme myLocation ro zad trackesh bokone
    private val destMarker: DestMarker by lazy {
        DestMarker(mBinding.map, 42, 42).also {
            addMarkerToMap(
                it,
                it.position,
                "مقصد"
            )
            it.setOnMarkerClickListener { marker, mapView ->
                shouldCameraTrackMixer = true
                it.showInfoWindow()
                false
            }
        }
    }
    private lateinit var mapViewModel: MixerMapFragmentViewModel
    private lateinit var mixerActivityViewModel: MixerActivityViewModel


    override val myLocation: GeoPoint? get() = mapViewModel.myLocation

    override fun onBtnMyLocationClicked() {
        shouldCameraTrackMixer = true
    }


    private val userMarker by lazy {
        MixerMarker(mBinding.map).also {
            addMarkerToMap(it, it.position)
        }
    }

    /** har kelasi ke az BaseMapFragment Inheritance mikonad in method bayad daresh copy shavad*/
    companion object {
        private const val BUNDLE_BTN_ROUTE_ID = "route-btn-id"
        fun newInstance(btnMyLocationId: Int, btnRouteId: Int) = MixerMapFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(BUNDLE_BTN_MY_LOC_ID, btnMyLocationId)
                    putInt(BUNDLE_BTN_ROUTE_ID, btnRouteId)
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mapViewModel = ViewModelProvider(this).get(MixerMapFragmentViewModel::class.java)
        mixerActivityViewModel =
            ViewModelProvider(activity!!).get(MixerActivityViewModel::class.java)

        initViews()
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
                        shouldCameraTrackMixer = false
                }
                return false
            }

            override fun onZoom(event: ZoomEvent): Boolean {
                return false
            }
        })

    }


    private fun subscribeObservers() {
        mixerActivityViewModel.newMissionEvent.observe(viewLifecycleOwner, Observer { event ->
            event.getEventIfNotHandled()?.let { mission ->
                println("debux: (MixerMapFragment-newMissionEventObserver) `newMissionEvent` Handler Routine Start ==================================")
                if (mission === Mission.NoMission) {
                    println("debux: `newMissionEvent` NoMission")
                    mBinding.map.overlays.remove(destMarker)
                    mBinding.map.overlays.remove(routePolyline)
                    mBinding.map.invalidate()
                    toast("شما ماموریت دیگری ندارید")
                } else {
                    println("debux: `newMissionEvent` NewMission")
                    //age ghablan track dar naghshe keshide shode bud
                    destMarker.position = mission.destCircleFence.center
                    destMarker.title = mission.conditionTitle
                    mBinding.map.overlays.add(destMarker)
                    mBinding.map.invalidate()
                    if (mapViewModel.myLocation != null) {
                        val remainingDistance =
                            mapViewModel.myLocation!!.distanceToAsDouble(mission.destCircleFence.center)
                        if (remainingDistance > mission.destCircleFence.radius) {
                            println("debux: getRouteCalled")
                            mapViewModel.getRoute(
                                listOf(
                                    mapViewModel.myLocation!!,
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
                    } else {
                        println("debux: getRoute() will call after userLoc received from server")
                        mapViewModel.shouldFindRoutesAfterUserLocationFound = true
                    }

                }
            }
        })

        mixerActivityViewModel.getMissionError.observe(viewLifecycleOwner, Observer { event ->
            event.getEventIfNotHandled()?.let {
                println("debux: (MixerMapFragment) getMissionError: $it")
                if (!it.contains("Action")) {
                    toast(it)
                }
            }
        })

        mixerActivityViewModel.userLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapViewModel.myLocation = it.circleFence.center
                animateMarker(userMarker, it.circleFence.center)
                if (shouldCameraTrackMixer || isFirstCameraMove) {
                    isFirstCameraMove = false
                    moveCamera(it.circleFence.center)
                }
                if (mapViewModel.shouldFindRoutesAfterUserLocationFound) {
                    mapViewModel.shouldFindRoutesAfterUserLocationFound = false
                    val destLocationArea =
                        mixerActivityViewModel.newMissionEvent.value!!.peekContent().destCircleFence
                    val remainingDistance =
                        mapViewModel.myLocation!!.distanceToAsDouble(destLocationArea.center)
                    if (remainingDistance > destLocationArea.radius) {
                        println("debux: (MixerMapFragment-mixerLocationObserver) GetRouteCalled After Location Came From server=================")
                        mapViewModel.getRoute(
                            listOf(
                                mapViewModel.myLocation!!,
                                destLocationArea.center
                            )
                        )
                        /*TODO Add Animation*/
                        btnRoute?.visibility = View.VISIBLE
                    } else {
                        toast("به مقصد رسیدید")
                        /*TODO Add Animation*/
                        btnRoute?.visibility = View.GONE
                    }
                }
            }
        })

        mapViewModel.getRouteResponse.observe(viewLifecycleOwner, Observer {
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

    override fun onMapTapped(geoPoint: GeoPoint) {
    }

    fun routeAgain() {
        /*todo add check if out of road then request to map.ir*/
        /*todo age location ha avaz shode bud ya khat keshide nashode bud req bezan*/
        /*       val mLoc = mapViewModel.myLocation
               val destLoc =   mixerActivityViewModel.newMissionEvent.value?.peekContent()?.destLocation?.center
               if (mLoc != null && destLoc != null) {
                   mapViewModel.getRoute(
                       listOf(
                           mLoc,
                           destLoc
                       )
                   )
                   destMarker.position = destLoc
               } else {
                   toast("شما ماموریتی ندارید")
               }*/
        println("debux: routeAgain()============================================")
        println("debux: routeAgain: ${mapViewModel.getRouteResponse.value?.getRoutePoints()}")
        //In dokme vaghti visible mishe ke mission ro gerefte bashim va yek bar ham darkhast getRoute ro zade bashim pas niazi nist check konim (startPoint,Dest) reside hast ya na
        mapViewModel.tryGetRouteAgain()
    }
}