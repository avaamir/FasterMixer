package com.behraz.fastermixer.batch.ui.fragments.mixer

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.models.MixerMission
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

    private var shouldCameraTrackMixer = true //age map ro scroll kard dg track nakone ama age dokme myLocation ro zad trackesh bokone
    private val destMarker: DestMarker by lazy {
        DestMarker(mBinding.map, 42, 42).also {
            addMarkerToMap(
                it,
                it.position,
                "مقصد"
            )
            it.setOnMarkerClickListener { marker, mapView ->
                shouldCameraTrackMixer = true
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
        fun newInstance(btnMyLocationId: Int) = MixerMapFragment()
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
        super.onCreateView(inflater, container, savedInstanceState)

        mapViewModel = ViewModelProvider(this).get(MixerMapFragmentViewModel::class.java)
        mixerActivityViewModel =
            ViewModelProvider(activity!!).get(MixerActivityViewModel::class.java)

        initViews()
        subscribeObservers()

        Handler().postDelayed({
            toast("Bang")
             btnMyLocation.callOnClick()
        }, 10000)


        return mBinding.root
    }

    override fun initViews() {
        super.initViews()
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
                if (mission === MixerMission.NoMission) {
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
                    println("debux: getRouteCalled")
                    if (mapViewModel.myLocation != null) {
                        val remainingDistance = mapViewModel.myLocation!!.distanceToAsDouble(mission.destLocation.center)
                        if (remainingDistance > mission.destLocation.radius) {
                            toast("مسیر یابی صدا زد") //TODO remove this
                            mapViewModel.getRoute(
                                listOf(
                                    mapViewModel.myLocation!!,
                                    mission.destLocation.center
                                )
                            )
                        } else {
                            toast("به مقصد رسیدید")
                        }
                    } else {
                        mapViewModel.shouldFindRoutesAfterUserLocationFound = true
                    }

                }
            }
        })

        mixerActivityViewModel.getMissionError.observe(viewLifecycleOwner, Observer { event ->
            event.getEventIfNotHandled()?.let {
                toast(it)
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
                    println("debux: distance: $remainingDistance")
                    if (remainingDistance > destLocationArea.radius) {
                        toast("مسیر یابی صدا زد") //TODO remove this
                        mapViewModel.getRoute(
                            listOf(
                                mapViewModel.myLocation!!,
                                destLocationArea.center
                            )
                        )
                    } else {
                        toast("به مقصد رسیدید")
                    }
                }
            }
        })

        mapViewModel.getRouteResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                println("debux: getRouteResponse Came")
                if (it.isSuccessful) {
                    println("debux: getRouteResponse isSuccessful")
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

    override fun onMapTapped(geoPoint: GeoPoint) {
    }
}