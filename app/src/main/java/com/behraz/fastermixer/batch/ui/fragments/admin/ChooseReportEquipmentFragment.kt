package com.behraz.fastermixer.batch.ui.fragments.admin

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
import com.behraz.fastermixer.batch.ui.adapters.AdminEquipmentAdapter
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel

class ChooseReportEquipmentFragment : Fragment(), AdminEquipmentAdapter.Interactions {

    private lateinit var startDate: Array<String>
    private lateinit var endDate: Array<String>
    private lateinit var reportType: String


    private val mAdapter = AdminEquipmentAdapter(this, false)
    private lateinit var viewModel: AdminActivityViewModel
    private lateinit var mBinding: FragmentChooseReportEquipmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::startDate.isInitialized) {
            requireArguments().apply {
                startDate = getStringArray(Constants.INTENT_REPORT_START_DATE) as Array<String>
                endDate = getStringArray(Constants.INTENT_REPORT_START_DATE) as Array<String>
                reportType = getString(Constants.INTENT_REPORT_TYPE) as String
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AdminActivityViewModel::class.java)
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
        viewModel.equipments.observe(
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
                            viewModel.getEquipments()
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
        navigate(
            when (reportType) {
                Constants.REPORT_TYPE_FULL -> R.id.action_chooseReportEquipmentFragment_to_fullReportFragment
                Constants.REPORT_TYPE_SUMMERY -> TODO("not implemented")
                Constants.REPORT_TYPE_DRAW_ROAD -> TODO("not implemented")
                else -> throw Exception("report Type is not valid: $reportType")
            },
            bundleOf(
                Constants.INTENT_REPORT_START_DATE to startDate,
                Constants.INTENT_REPORT_END_DATE to endDate,
                Constants.INTENT_REPORT_VEHICLE to adminEquipment
            )
        )
    }

}