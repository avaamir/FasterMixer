package com.behraz.fastermixer.batch.ui.fragments.mixer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.MixerMapFragmentViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class MixerMapFragment: BaseMapFragment() {

    private var isFirstTime = true
    override val myLocation: GeoPoint
        get() = mapViewModel.myLocation


    private var btnMyLocationId: Int =
        0 //btnMylocation Mitune tu activity bashe niaz hast refrencesh ro dashte bashim age tu activity hast
    private lateinit var mapViewModel: MixerMapFragmentViewModel
    private lateinit var mixerActivityViewModel: MixerActivityViewModel


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
        mixerActivityViewModel = ViewModelProvider(activity!!).get(MixerActivityViewModel::class.java)

        initViews()
        subscribeObservers()

        return mBinding.root
    }

    override fun initViews() {
        super.initViews()
    }


    private fun subscribeObservers() {
        mixerActivityViewModel.mixerLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapViewModel.myLocation = it.center
                userMarker.position = it.center
                if (isFirstTime) {
                    isFirstTime = false
                    moveCamera(it.center)
                }
            }
        })

        mapViewModel.getRouteResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isSuccessful) {
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