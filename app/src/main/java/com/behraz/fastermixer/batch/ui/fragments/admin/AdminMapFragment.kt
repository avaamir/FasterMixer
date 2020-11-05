package com.behraz.fastermixer.batch.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.app.LocationCompassProvider
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.enums.EquipmentType
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.osm.markers.*
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.OnSourceMapChange
import com.behraz.fastermixer.batch.utils.general.diffSourceFromNewValues
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.AdminMapFragmentViewModel
import kotlinx.android.synthetic.main.map_with_locationbox.*
import org.osmdroid.util.GeoPoint

class AdminMapFragment : BaseMapFragment() {
    private lateinit var mapViewModel: AdminMapFragmentViewModel
    private lateinit var adminActivityViewModel: AdminActivityViewModel

    private val userMarker by lazy {
        DestMarker(mBinding.map, 42, 42)
    }

    private var isFirstTimeLocationFound = true

    override val myLocation: GeoPoint?
        get() = mapViewModel.myLocation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminActivityViewModel =
            ViewModelProvider(activity!!).get(AdminActivityViewModel::class.java)
        mapViewModel = ViewModelProvider(this).get(AdminMapFragmentViewModel::class.java)

        LocationCompassProvider.fixDeviceOrientationForCompassCalculation(activity!!)
        LocationCompassProvider.start(context!!)

        subscribeObservers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun subscribeObservers() {
        LocationCompassProvider.location.observe(viewLifecycleOwner, {
            mapViewModel.myLocation = GeoPoint(it)
            userMarker.position = mapViewModel.myLocation
            if (isFirstTimeLocationFound) {
                isFirstTimeLocationFound = false
                addUserMarkerToMap(userMarker)
                moveCamera(userMarker.position)
            }
            mBinding.map.invalidate()
        })

        adminActivityViewModel.equipments.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isSucceed) {
                    if (it.entity?.isEmpty() == true) {
                        toast("تجهیزی در سرور تعریف نشده است")
                    }

                    mapViewModel.markers.diffSourceFromNewValues(
                        it.entity!!,
                        AdminEquipment::id,
                        object : OnSourceMapChange<String, VehicleMarker, AdminEquipment> {
                            override fun onAddItem(
                                key: String,
                                item: AdminEquipment
                            ): VehicleMarker {
                                val marker = when (item.type) {
                                    EquipmentType.Mixer -> MixerMarker(mBinding.map)
                                    EquipmentType.Pomp -> PompMarker(mBinding.map)
                                    EquipmentType.Loader -> LoaderMarker(mBinding.map)
                                    EquipmentType.Other -> LoaderMarker(mBinding.map)
                                }.exhaustiveAsExpression()
                                item.carIdStr.split(",").run {
                                    marker.setPelakText(get(0), get(1), get(2), get(3))
                                }
                                addMarkerToMap(marker, item.location, item.name)
                                return marker
                            }

                            override fun onItemExistInBoth(
                                keyId: String,
                                marker: VehicleMarker,
                                item: AdminEquipment
                            ) {
                                if (marker.position != item.location)
                                    marker.position = item.location
                            }

                            override fun onRemoveItem(keyId: String) {
                                mapViewModel.markers.remove(keyId)
                            }
                        })
                    mBinding.map.invalidate()

                    //
                    /*
                    val excludeVehiclesList = mapViewModel.markers.keys
                    it.entity!!.forEach { _equipment ->
                        var marker = mapViewModel.markers[_equipment.id]
                        if (marker == null) {
                            marker = when (_equipment.type) {
                                EquipmentType.Mixer -> MixerMarker(mBinding.map)
                                EquipmentType.Pomp -> PompMarker(mBinding.map)
                                EquipmentType.Loader -> LoaderMarker(mBinding.map)
                                EquipmentType.Other -> LoaderMarker(mBinding.map)
                            }.exhaustiveAsExpression()
                            _equipment.carIdStr.split(",").run {
                                marker.setPelakText(get(0), get(1), get(2), get(3))
                            }
                            addMarkerToMap(marker, _equipment.location, _equipment.name)
                            excludeVehiclesList.remove(_equipment.id)
                            mapViewModel.markers[_equipment.id] = marker
                        } else {
                            excludeVehiclesList.remove(_equipment.id)
                            marker.position = _equipment.location
                        }
                    }
                    mBinding.map.invalidate()
                    excludeVehiclesList.forEach { vehicleId ->
                        mapViewModel.markers.remove(vehicleId)
                    }
                   */
                    //
                } else {
                    toast(it.message)
                }
            } else {
                toast(Constants.SERVER_ERROR)
            }

        })

        adminActivityViewModel.onVehicleSelectedToShowOnMap.observe(viewLifecycleOwner, {
            mapViewModel.markers[it.id]?.let { marker ->
                moveCamera(marker.position, shouldAnimate = false)
                marker.showInfoWindow()
            }
        })
    }

    override fun onBtnMyLocationClicked() {}


    override fun onDestroy() {
        super.onDestroy()
        LocationCompassProvider.stop(context!!)
    }
}


