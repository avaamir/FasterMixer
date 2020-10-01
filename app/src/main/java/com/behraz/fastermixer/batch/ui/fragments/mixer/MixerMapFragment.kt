package com.behraz.fastermixer.batch.ui.fragments.mixer

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.ui.fragments.VehicleFragment
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.MixerMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleActivityViewModel
import org.osmdroid.util.GeoPoint

class MixerMapFragment : VehicleFragment() {

    private lateinit var mapViewModel: MixerMapFragmentViewModel
    private lateinit var mixerActivityViewModel: MixerActivityViewModel

    override val vehicleActivityViewModel: VehicleActivityViewModel get() = mixerActivityViewModel
    override val mMapViewModel: VehicleMapFragmentViewModel get() = mapViewModel

    companion object {
        fun newInstance(btnMyLocationId: Int, btnRouteId: Int) = MixerMapFragment()
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
        mapViewModel = ViewModelProvider(this).get(MixerMapFragmentViewModel::class.java)
        mixerActivityViewModel = ViewModelProvider(activity!!).get(MixerActivityViewModel::class.java)
    }

    override fun onMapTapped(geoPoint: GeoPoint) {
    }
}