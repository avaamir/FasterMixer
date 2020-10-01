package com.behraz.fastermixer.batch.ui.fragments.mixer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.models.Mission
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.fragments.VehicleFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.PompMapFragment
import com.behraz.fastermixer.batch.ui.osm.DestMarker
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.MixerMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleMapFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.VehicleViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class MixerMapFragment : VehicleFragment() {

    private lateinit var mapViewModel: MixerMapFragmentViewModel
    private lateinit var mixerActivityViewModel: MixerActivityViewModel

    override val vehicleActivityViewModel: VehicleViewModel get() = mixerActivityViewModel
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