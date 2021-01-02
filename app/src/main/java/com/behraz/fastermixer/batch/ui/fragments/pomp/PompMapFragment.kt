package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.ui.fragments.VehicleFragment
import com.behraz.fastermixer.batch.ui.osm.markers.ImageMarker
import com.behraz.fastermixer.batch.ui.osm.markers.MixerMarker
import com.behraz.fastermixer.batch.utils.general.OnSourceMapChange
import com.behraz.fastermixer.batch.utils.general.diffSourceFromNewValues
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.PompMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleActivityViewModel
import org.osmdroid.util.GeoPoint

class PompMapFragment : VehicleFragment() {

    private lateinit var mapViewModel: PompMapFragmentViewModel
    private lateinit var pompViewModel: PompActivityViewModel

    override val vehicleActivityViewModel: VehicleActivityViewModel get() = pompViewModel
    override val mMapViewModel: VehicleMapFragmentViewModel get() = mapViewModel

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
        pompViewModel = ViewModelProvider(requireActivity()).get(PompActivityViewModel::class.java)
    }

    override fun subscribeObservers() {
        super.subscribeObservers()

        pompViewModel.shouldShowAllMixers.observe(viewLifecycleOwner) { shouldShowAll ->
            if (shouldShowAll) {
                sortAndShowMixers(pompViewModel.allMixers.value)
            } else {
                sortAndShowMixers(pompViewModel.requestMixers.value)
            }

        }

        pompViewModel.allMixers.observe(viewLifecycleOwner) {
            if (pompViewModel.shouldShowAllMixers.value!!) {
                sortAndShowMixers(it)
            }
        }

        pompViewModel.requestMixers.observe(viewLifecycleOwner) {
            if (!pompViewModel.shouldShowAllMixers.value!!) {
                sortAndShowMixers(it)
            }
        }
    }


    private fun sortAndShowMixers(serverResponse: ApiResult<List<Mixer>>?) {
        if (serverResponse?.isSucceed == true) {
            mapViewModel.markers.diffSourceFromNewValues(
                serverResponse.entity,
                Mixer::id,
                object : OnSourceMapChange<Int, ImageMarker, Mixer> {
                    override fun onAddItem(key: Int, item: Mixer): ImageMarker {
                        return MixerMarker(mBinding.map).also { _marker ->
                            _marker.position = item.location
                            item.pelak.split(",")
                                .run { _marker.setPelakText(get(0), get(1), get(2), get(3)) }
                            if (item.id != UserConfigs.user.value?.equipmentId) { //momkene role taraf rannade pomp bashe ama mashinesh pomp nabashe, age in halat pish umad marker marbut be in equipment ro ezafe nemikonim chun userMarker ghablan ezafe shode va ru ham mioftan
                                addMarkerToMap(
                                    _marker,
                                    item.location,
                                    item.driverName
                                )
                            }
                        }
                    }

                    override fun onItemExistInBoth(
                        keyId: Int,
                        marker: ImageMarker,
                        item: Mixer
                    ) {
                        animateMarker(marker, item.location)
                    }

                    override fun onRemoveItem(keyId: Int) {
                        mBinding.map.overlayManager.remove(
                            mapViewModel.markers.remove(keyId).also {
                                if (it?.isInfoWindowShown == true)
                                    it.closeInfoWindow()
                            }
                        )
                    }
                })
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