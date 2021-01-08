package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.behraz.fastermixer.batch.ui.fragments.BaseMapFragment
import com.behraz.fastermixer.batch.ui.osm.markers.MixerMarker
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint

class DrawRoadMapFragment : BaseMapFragment() {

    private var shouldTrackCar = true

    private var animator: ValueAnimator? = null
    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    override fun initViews() {
        super.initViews()
        mBinding.map.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent): Boolean {
                event.run {
                    if (x != 0 || y != 0)
                        shouldTrackCar = false
                }
                return false
            }

            override fun onZoom(event: ZoomEvent): Boolean {
                return false
            }
        })
    }

    private fun subscribeObservers() {
        viewModel.drawRoadReport.observe(viewLifecycleOwner) {
            if (it.isSucceed) {
                val points = it.entity!!.map { _data ->
                    _data.point
                }
                drawPolyline(points)
                //viewModel.isPaused = false
                resumeAnimation()
            }
        }

        viewModel.currentPointIndex.observe(viewLifecycleOwner) { index ->
            //Seekbar age avaz beshe va Resume ke hast in observer call mishe

            //TODO update marker info window
            val data = viewModel.drawRoadReport.value!!.entity!!
            if (data.isEmpty()) {
                toast("در این بازه هیچ دیتایی ثبت نشده است", true)
                findNavController().navigateUp()
                return@observe
            }

            val point = data[index].point
            if (!viewModel.isPaused) {
                if (animator?.isRunning == true) { //age animator running bashe va liveData(currentPointIndex) taghir kone yaani ba seekBar taghir karde pas bayad animation feli stop beshe ta dobare seekbar ro bar nagardune be halat avalesh
                    animator?.cancel()
                    marker.position = point
                    mBinding.map.invalidate()
                }
                if (shouldTrackCar) {
                    moveCamera(point, mBinding.map.zoomLevelDouble, true)
                }
                animator = animateMarker(marker, point, LinearInterpolator(), viewModel.speed) {
                    if (index + 1 < data.size) {
                        animator = null
                        viewModel.currentPointIndex.value = index + 1
                    } else {
                        viewModel.isPaused = true
                    }
                }
            } else {
                marker.position = point
                mBinding.map.invalidate()
                if (shouldTrackCar) {
                    moveCamera(point, mBinding.map.zoomLevelDouble, true)
                }
            }
        }

        viewModel.isPausedLiveData.observe(viewLifecycleOwner) { isPaused ->
            if (isPaused)
                animator?.cancel()
            else
                resumeAnimation()
        }

    }

    private fun resumeAnimation() {
        viewModel.currentPointIndex.value = viewModel.currentPointIndex.value ?: 0
    }

    private val marker by lazy {
        //todo according to vehicle type must change
        MixerMarker(mBinding.map).also {
            addMarkerToMap(it, it.position, "")
            //TODO it.infoWindow = CustomInfoWindowForReport() //add info window, show speed, dateTime, ..
            it.setOnMarkerClickListener { marker, _ ->
                shouldTrackCar = true
                if (!marker.isInfoWindowShown)
                    marker.showInfoWindow()
                else
                    marker.closeInfoWindow()
                return@setOnMarkerClickListener true
            }
        }
    }

    override val myLocation: GeoPoint?
        get() = Constants.mapStartPoint

    override fun onBtnMyLocationClicked(view: View) {
        val points = viewModel.drawRoadReport.value!!.entity
        if (points != null) {
            val index = viewModel.currentPointIndex.value!!
            shouldTrackCar = true
            moveCamera(points[index].point, mBinding.map.zoomLevelDouble, false)
        } else {
            toast("در حال دریافت گزارش")
        }
    }


}