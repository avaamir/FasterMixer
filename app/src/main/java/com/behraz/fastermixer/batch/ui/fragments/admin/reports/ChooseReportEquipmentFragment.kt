package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportEquipmentBinding
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.enums.ReportType
import com.behraz.fastermixer.batch.ui.adapters.AdminEquipmentAdapter
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel

class ChooseReportEquipmentFragment : Fragment(), AdminEquipmentAdapter.Interactions {

    private val mAdapter = AdminEquipmentAdapter(this, false)
    private lateinit var reportViewModel: ReportViewModel
    private lateinit var activityViewModel: AdminActivityViewModel
    private lateinit var mBinding: FragmentChooseReportEquipmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        activityViewModel =
            ViewModelProvider(requireActivity()).get(AdminActivityViewModel::class.java)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choose_report_equipment,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
    }

    private fun initViews() {
        mBinding.recycler.adapter = mAdapter
        mBinding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun subscribeObservers() {
        activityViewModel.equipments.observe(
            viewLifecycleOwner, {
                if (mAdapter.currentList.isEmpty()) {
                    crossfade(mBinding.recycler, mBinding.progressBar)
                    val items = it?.entity
                    if (items != null) {
                        if (items.isEmpty())
                            mBinding.tvNoEquipmentMessage.visibility = View.VISIBLE
                        else
                            mAdapter.submitList(items)
                    } else {
                        snack(it?.message ?: Constants.SERVER_ERROR, {
                            mBinding.progressBar.visibility = View.VISIBLE
                            activityViewModel.getEquipments()
                        })
                    }
                } else {
                    mBinding.recycler.visibility = View.VISIBLE
                    mBinding.progressBar.visibility = View.GONE
                }
            })

    }

    override fun onBtnShowOnMapClicked(adminEquipment: AdminEquipment) {}

    override fun onEquipmentClicked(adminEquipment: AdminEquipment) {
        reportViewModel.request.vehicleId = adminEquipment.id
        navigate(
            when (reportViewModel.request.reportType!!) {
                ReportType.Full -> R.id.action_chooseReportEquipmentFragment_to_fullReportFragment
                ReportType.Summery -> R.id.action_chooseReportEquipmentFragment_to_summeryReportFragment
                ReportType.DrawRoad -> R.id.action_chooseReportEquipmentFragment_to_drawRoadFragment
            },
            bundleOf(
                Constants.INTENT_REPORT_VEHICLE to adminEquipment //used in AdminActivity for setting toolbarTitle
            )
        )
    }

}