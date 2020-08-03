package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.osm.MixerMarker
import com.behraz.fastermixer.batch.ui.osm.PompMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.PompMapFragmentViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class PompMapFragment : BaseMapFragment() {


    private var isFirstTime = true
    override val myLocation: GeoPoint
        get() = mapViewModel.myLocation


    private lateinit var mapViewModel: PompMapFragmentViewModel
    private lateinit var pompViewModel: PompActivityViewModel


    override fun onBtnMyLocationClicked() {}


    private val userMarker by lazy {
        PompMarker(mBinding.map).also {
            addMarkerToMap(it, it.position)
        }
    }

    /** har kelasi ke az BaseMapFragment Inheritance mikonad in method bayad daresh copy shavad*/
    companion object {
        fun newInstance(btnMyLocationId: Int) = PompMapFragment()
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

        mapViewModel = ViewModelProvider(this).get(PompMapFragmentViewModel::class.java)
        pompViewModel = ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)

        initViews()
        subscribeObservers()

        return mBinding.root
    }

    override fun initViews() {
        super.initViews()
    }


    private fun subscribeObservers() {

        pompViewModel.pompArea.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapViewModel.myLocation = it.center
                userMarker.position = it.center
                if (isFirstTime) {
                    isFirstTime = false
                    moveCamera(it.center)
                }
            }
        })


        pompViewModel.mixers.observe(viewLifecycleOwner, Observer {
            if (it?.isSucceed == true) {
                val excludeMarkerList =
                    ArrayList(mapViewModel.markers.keys) // we need this for exclude mixers from original list if mixer not exists in new mixerList
                it.entity?.forEach { mixer ->
                    excludeMarkerList.remove(mixer.id) //do not need to exclude this mixer because it's exists in new List too
                    val mixerMarker = mapViewModel.markers[mixer.id]
                    if (mixerMarker == null) { //new mixer in pomp incoming list (taze az batch kharej shode va dare be pomp mire)
                        //add to marker hash map and MapView
                        mapViewModel.markers[mixer.id] =
                            MixerMarker(mBinding.map).also { _marker ->
                                _marker.position = mixer.latLng
                                addMarkerToMap(
                                    _marker,
                                    mixer.latLng,
                                    "${mixer.carName} ${mixer.driverName}"
                                )
                            }
                    } else { //mixer already exists, update location
                        mixerMarker.position = mixer.latLng
                    }
                }
                excludeMarkerList.forEach { mixerId ->
                    mBinding.map.overlayManager.remove(
                        mapViewModel.markers.remove(mixerId)
                    )
                }
            }
        })
    }

    override fun onMapTapped(geoPoint: GeoPoint) {
    }
}