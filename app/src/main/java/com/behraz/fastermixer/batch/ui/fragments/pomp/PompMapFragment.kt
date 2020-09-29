package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.fragments.mixer.MixerMapFragment
import com.behraz.fastermixer.batch.ui.osm.DestMarker
import com.behraz.fastermixer.batch.ui.osm.DriverInfoWindow
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.ui.osm.PompMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.minus
import com.behraz.fastermixer.batch.utils.general.now
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.PompMapFragmentViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class PompMapFragment : BaseMapFragment() {


    private val destMarker: DestMarker by lazy {
        DestMarker(mBinding.map, 42, 42).also {
            addMarkerToMap(
                it,
                it.position,
                "مقصد"
            )
            it.setOnMarkerClickListener { marker, mapView ->
                //todo shouldCameraTrackMixer = true
                it.showInfoWindow()
                false
            }
        }
    }


    private var btnRoute: View? = null

    private var isFirstTime = true
    override val myLocation: GeoPoint?
        get() = mapViewModel.myLocation


    private lateinit var mapViewModel: PompMapFragmentViewModel
    private lateinit var pompViewModel: PompActivityViewModel

    private var routePolyline: Polyline? = null
        set(value) {
            println("debux: Setter: before:$field new:$value, ${now()}")
            field = value
        }


    override fun onBtnMyLocationClicked() {}


    private val userMarker by lazy {
        PompMarker(mBinding.map).also {
            addMarkerToMap(it, it.position)
        }
    }

    /** har kelasi ke az BaseMapFragment Inheritance mikonad in method bayad daresh copy shavad*/
    companion object {
        private const val BUNDLE_BTN_ROUTE_ID = "route-btn-id"
        fun newInstance(btnMyLocationId: Int, btnRouteId: Int) = PompMapFragment()
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

        mapViewModel = ViewModelProvider(this).get(PompMapFragmentViewModel::class.java)
        pompViewModel = ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)

        initViews()
        subscribeObservers()

        return mBinding.root
    }

    override fun initViews() {
        super.initViews()
        val btnRouteId = arguments?.getInt(BUNDLE_BTN_ROUTE_ID) ?: 0
        if (btnRouteId != 0)
            btnRoute = activity!!.findViewById(btnRouteId)
    }

    private fun subscribeObservers() {

        pompViewModel.pompAreaInfo.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapViewModel.myLocation = it.center
                userMarker.position = it.center
                if (isFirstTime) {
                    isFirstTime = false
                    moveCamera(it.center)
                }
                userMarker.title = "مکان شما"


                /*todo add this after tracking added if (shouldCameraTrackMixer || isFirstCameraMove) {
                    println("debux: moveCamera")
                    isFirstCameraMove = false
                    moveCamera(it.center)
                }*/
                if (mapViewModel.shouldFindRoutesAfterUserLocationFound) {
                    mapViewModel.shouldFindRoutesAfterUserLocationFound = false
                    val destLocationArea =
                        pompViewModel.newMissionEvent.value!!.peekContent().destLocation
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

        //
        pompViewModel.shouldShowAllMixers.observe(viewLifecycleOwner, Observer { shouldShowAll ->
            if (shouldShowAll) {
                sortAndShowMixers(pompViewModel.allMixers.value)
            } else {
                sortAndShowMixers(pompViewModel.requestMixers.value)
            }

        })

        pompViewModel.allMixers.observe(viewLifecycleOwner, Observer {
            if (pompViewModel.shouldShowAllMixers.value!!) {
                sortAndShowMixers(it)
            }
        })

        pompViewModel.requestMixers.observe(viewLifecycleOwner, Observer {
            if (!pompViewModel.shouldShowAllMixers.value!!) {
                sortAndShowMixers(it)
            }
        })
        //

        //
        pompViewModel.newMissionEvent.observe(viewLifecycleOwner, Observer { event ->
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

        pompViewModel.getMissionError.observe(viewLifecycleOwner, Observer { event ->
            event.getEventIfNotHandled()?.let {
                println("debux: getMissionError: $it")
                if (!it.contains("Action")) {
                    toast(it)
                }
            }
        })

        mapViewModel.getRouteResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                println("debux: getRouteResponse Came")
                if (it.isSuccessful) {
                    println("debux: getRouteResponse isSuccessful")
                    routePolyline?.let { _routes ->
                        mBinding.map.overlayManager.remove(_routes)
                        routePolyline = null
                    }
                    routePolyline = drawPolyline(it.getRoutePoints())
                    println("debux: points: ${routePolyline}, ${it} ${it.getRoutePoints()}")
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
        //
    }

    fun routeAgain() {
        println("debux: $routePolyline , ${routePolyline?.distance}")
        println("debux: routeAgain: ${mapViewModel.getRouteResponse.value?.getRoutePoints()}")
        mapViewModel.tryGetRouteAgain()
    }

    private fun sortAndShowMixers(serverResponse: Entity<List<Mixer>>?) {
        if (serverResponse?.isSucceed == true) {
            val excludeMarkerList =
                ArrayList(mapViewModel.markers.keys) // we need this for exclude mixers from original list if mixer not exists in new mixerList

            serverResponse.entity?.forEach { mixer ->
                excludeMarkerList.remove(mixer.id) //do not need to exclude this mixer because it's exists in new List too
                val mixerMarker = mapViewModel.markers[mixer.id]
                if (mixerMarker == null) { //new mixer in pomp incoming list (taze az batch kharej shode va dare be pomp mire)
                    //add to marker hash map and MapView
                    mapViewModel.markers[mixer.id] =
                        MixerMarker(mBinding.map).also { _marker ->
                            _marker.position = mixer.latLng
                            mixer.pelak.split(",")
                                .run { _marker.setPelakText(get(0), get(1), get(2), get(3)) }
                            addMarkerToMap(
                                _marker,
                                mixer.latLng,
                                mixer.driverName
                            )
                        }
                } else { //mixer already exists, update location
                    mixerMarker.position = mixer.latLng
                }
            }
            excludeMarkerList.forEach { mixerId ->
                mBinding.map.overlayManager.remove(
                    mapViewModel.markers.remove(mixerId).also {
                        if (it?.isInfoWindowShown == true)
                            it.closeInfoWindow()
                    }
                )
            }
            mBinding.map.invalidate()
            val x = mBinding.map.overlays
            println()
        }
    }

    override fun onMapTapped(geoPoint: GeoPoint) {
    }

    fun focusOnMixer(mixer: Mixer) {
        moveCamera(mixer.latLng)
        mapViewModel.markers[mixer.id]?.showInfoWindow()
    }
}