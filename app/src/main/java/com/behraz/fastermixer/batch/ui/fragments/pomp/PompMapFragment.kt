package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.ui.fragments.VehicleFragment
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.PompMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleActivityViewModel
import org.osmdroid.util.GeoPoint

class PompMapFragment : VehicleFragment() {

    private lateinit var mapViewModel: PompMapFragmentViewModel
    private lateinit var pompViewModel: PompActivityViewModel

    override val vehicleActivityViewModel: VehicleActivityViewModel get() = pompViewModel
    override val mMapViewModel: VehicleMapFragmentViewModel get() =  mapViewModel

    companion object {
        fun newInstance(btnMyLocationId: Int, btnRouteId: Int) = PompMapFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt(BUNDLE_BTN_MY_LOC_ID, btnMyLocationId)
                    putInt(BUNDLE_BTN_ROUTE_ID, btnRouteId)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //tuye on attach init shodan chun tuye super.onCreateView bahashun kar daran
        mapViewModel = ViewModelProvider(this).get(PompMapFragmentViewModel::class.java)
        pompViewModel = ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)
    }

    override fun subscribeObservers() {
        super.subscribeObservers()

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
    }


    private fun sortAndShowMixers(serverResponse: Entity<List<Mixer>>?) {
        if (serverResponse?.isSucceed == true) {
            val excludeMarkerList =
                ArrayList(mapViewModel.markers.keys) // we need this for exclude mixers from original list if mixer not exists in new mixerList

            serverResponse.entity?.forEach { mixer ->
                excludeMarkerList.remove(mixer.id) //do not need to exclude this mixer because it's exists in new com.behraz.fastermixer.batch.models.requests.openweathermap.List too
                val mixerMarker = mapViewModel.markers[mixer.id]
                if (mixerMarker == null) { //new mixer in pomp incoming list (taze az batch kharej shode va dare be pomp mire)
                    //add to marker hash map and MapView
                    mapViewModel.markers[mixer.id] =
                        MixerMarker(mBinding.map).also { _marker ->
                            _marker.position = mixer.location
                            mixer.pelak.split(",")
                                .run { _marker.setPelakText(get(0), get(1), get(2), get(3)) }
                            addMarkerToMap(
                                _marker,
                                mixer.location,
                                mixer.driverName
                            )
                        }
                } else { //mixer already exists, update location
                    animateMarker(mixerMarker, mixer.location)
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
        }
    }

    override fun onMapTapped(geoPoint: GeoPoint) {
    }

    fun focusOnMixer(mixer: Mixer) {
        shouldCameraTrackUser = false
        moveCamera(mixer.location)
        mapViewModel.markers[mixer.id]?.showInfoWindow()
    }

}