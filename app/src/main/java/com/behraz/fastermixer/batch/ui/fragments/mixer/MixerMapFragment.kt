package com.behraz.fastermixer.batch.ui.fragments.mixer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.osm.DestMarker
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.MixerMapFragmentViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline


class MixerMapFragment : BaseMapFragment() {


    private var btnRoute: View? = null


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

    private var routePolyline: Polyline? = null

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

        test() //TODO

        return mBinding.root
    }

    private fun test() {
        //mBinding.map.setUseDataConnection(false)
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
                println("debux: `newMissionEvent` Handled")
                if (mission === Mission.NoMission) {
                    println("debux: `newMissionEvent` NoMission")
                    mBinding.map.overlayManager.remove(destMarker)
                    toast("شما ماموریت دیگری ندارید")
                } else {
                    println("debux: `newMissionEvent` NewMission")
                    //age ghablan track dar naghshe keshide shode bud
                    destMarker.position = mission.destLocation.center
                    destMarker.title = "مقصد ${mission.conditionTitle}"
                    routePolyline?.let { _routes ->
                        mBinding.map.overlayManager.remove(_routes)
                        routePolyline = null
                    }
                    if (mapViewModel.myLocation != null) {
                        val remainingDistance =
                            mapViewModel.myLocation!!.distanceToAsDouble(mission.destLocation.center)
                        if (remainingDistance > mission.destLocation.radius) {
                            println("debux: getRouteCalled")
                            mapViewModel.getRoute(
                                listOf(
                                    mapViewModel.myLocation!!,
                                    mission.destLocation.center
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
                println("debux: getMissionError: $it")
                if (!it.contains("Action")) {
                    toast(it)
                }
            }
        })

        mixerActivityViewModel.mixerLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapViewModel.myLocation = it.center
                userMarker.position = it.center
                if (shouldCameraTrackMixer) {
                    moveCamera(it.center)
                }
                if (mapViewModel.shouldFindRoutesAfterUserLocationFound) {
                    mapViewModel.shouldFindRoutesAfterUserLocationFound = false
                    val destLocationArea =
                        mixerActivityViewModel.newMissionEvent.value!!.peekContent().destLocation
                    val remainingDistance =
                        mapViewModel.myLocation!!.distanceToAsDouble(destLocationArea.center)
                    if (remainingDistance > destLocationArea.radius) {
                        println("debux: GetRouteCalled After Location Came From server")
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
                println("debux: getRouteResponse Came")
                if (it.isSuccessful) {
                    println("debux: getRouteResponse isSuccessful")
                    routePolyline?.let {
                        mBinding.map.overlayManager.remove(routePolyline)
                    }
                    routePolyline = drawPolyline(it.getRoutePoints())
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
        mapViewModel.tryGetRouteAgain()
    }
}