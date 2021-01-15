package com.behraz.fastermixer.batch.ui.fragments.admin.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.app.LocationCompassProvider
import com.behraz.fastermixer.batch.app.receivers.isNetworkAvailable
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.enums.EquipmentType
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.osm.infowindows.AdminDriverInfoWindow
import com.behraz.fastermixer.batch.ui.osm.markers.*
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.OnSourceMapChange
import com.behraz.fastermixer.batch.utils.general.diffSourceFromNewValues
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.AdminMapFragmentViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

class AdminMapFragment : BaseMapFragment(), AdminDriverInfoWindow.Interactions {
    private lateinit var mapViewModel: AdminMapFragmentViewModel
    private lateinit var adminActivityViewModel: AdminActivityViewModel

    val markers = HashMap<Int, VehicleMarker>()

    private var routePolyline: Polyline? = null

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
            ViewModelProvider(requireActivity()).get(AdminActivityViewModel::class.java)
        mapViewModel = ViewModelProvider(this).get(AdminMapFragmentViewModel::class.java)

        LocationCompassProvider.fixDeviceOrientationForCompassCalculation(requireActivity())
        LocationCompassProvider.start(requireContext())

        subscribeObservers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        markers.clear()
        adminActivityViewModel.equipments.value?.entity?.let {
            if (it.isNotEmpty()) {
                it.forEach { equipment ->
                    markers[equipment.id] = makeEquipmentMarker(equipment)
                }
            }
        }
    }

    private fun subscribeObservers() {
        adminActivityViewModel.eventOnRouteToCarClicked.observe(viewLifecycleOwner, { event ->
            event.getEventIfNotHandled()?.let {
                routeToCar(GeoPoint(it.location))
            }
        })


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

                    markers.diffSourceFromNewValues(
                        it.entity!!,
                        AdminEquipment::id,
                        object : OnSourceMapChange<Int, VehicleMarker, AdminEquipment> {
                            override fun onAddItem(
                                key: Int,
                                item: AdminEquipment
                            ): VehicleMarker {
                                return makeEquipmentMarker(item)
                            }

                            override fun onItemExistInBoth(
                                keyId: Int,
                                marker: VehicleMarker,
                                item: AdminEquipment
                            ) {
                                if (marker.position != item.location)
                                    marker.position = item.location
                            }

                            override fun onRemoveItem(keyId: Int) {
                                markers.remove(keyId)
                            }
                        })
                    mBinding.map.invalidate()
                } else {
                    toast(it.message)
                }
            } else {
                toast(Constants.SERVER_ERROR)
            }

        })

        adminActivityViewModel.eventOnVehicleSelectedToShowOnMap.observe(
            viewLifecycleOwner,
            { event ->
                event.getEventIfNotHandled()?.let {
                    markers[it.id]?.let { marker ->
                        moveCamera(marker.position, shouldAnimate = false)
                        marker.showInfoWindow()
                    }
                }
            })

        mapViewModel.getRouteResponse.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isSuccessful) {
                    routePolyline?.let { _routes ->
                        mBinding.map.overlays.remove(_routes)
                    }
                    routePolyline = drawPolyline(it.getRoutePoints())
                } else {
                    if (it.code == "NoRoute")
                        toast("متاسفانه در این منطقه مسیریابی ممکن نیست")
                    else
                        toast(Constants.SERVER_ERROR)
                }
            } else {
                if (isNetworkAvailable()) {
                    toast(Constants.SERVER_ERROR)
                } else {
                    toast("لطفا وضعیت اینترنت خود را بررسی کنید")
                }
            }
        })
    }

    private fun makeEquipmentMarker(equipment: AdminEquipment): VehicleMarker {
        val marker = when (equipment.type) {
            EquipmentType.Mixer ->
                MixerMarker(
                    mBinding.map,
                    interactions = this@AdminMapFragment
                )
            EquipmentType.Pomp ->
                PompMarker(
                    mBinding.map,
                    interactions = this@AdminMapFragment
                )
            EquipmentType.Loader ->
                LoaderMarker(
                    mBinding.map,
                    interactions = this@AdminMapFragment
                )
            EquipmentType.Other ->
                LoaderMarker(
                    mBinding.map,
                    interactions = this@AdminMapFragment
                )
        }.exhaustiveAsExpression()
        equipment.carIdStr.split(",").run {
            marker.setPelakText(get(0), get(1), get(2), get(3))
        }
        equipment.location.let { loc ->
            addMarkerToMap(marker, loc, equipment.name)
        }
        return marker
    }

    private fun routeToCar(dest: GeoPoint) {
        val userLoc = mapViewModel.myLocation
        when {
            userLoc != null -> mapViewModel.getRoute(listOf(userLoc, dest))
            LocationCompassProvider.isProviderEnable(requireContext()) -> toast("موقعیت شما هنوز یافت نشده است")
            else -> toast("ابتدا جی پی اس خود را روشن کنید")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationCompassProvider.stop(requireContext())
    }

    override fun onRouteClicked(marker: VehicleMarker) {
        routeToCar(marker.position)
    }

}


