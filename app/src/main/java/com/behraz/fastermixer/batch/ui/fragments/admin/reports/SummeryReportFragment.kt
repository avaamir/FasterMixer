package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentSummeryReportBinding
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.ui.adapters.SummeryReportAdapter
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel

class SummeryReportFragment : Fragment() {

    private val mAdapter = SummeryReportAdapter()
    private lateinit var mBinding: FragmentSummeryReportBinding
    private lateinit var viewModel: ReportViewModel


    private lateinit var vehicle: AdminEquipment
    private lateinit var request: GetReportRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::request.isInitialized) {
            requireArguments().apply {
                vehicle = getParcelable(Constants.INTENT_REPORT_VEHICLE)!!
                request = getParcelable(Constants.INTENT_REPORT_GET_REPORT_REQ)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_summery_report, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
        viewModel.getSummeryReport(request)
    }

    private fun subscribeObservers() {
        viewModel.summeryReport.observe(viewLifecycleOwner) {
            crossfade(mBinding.recyclerView, mBinding.progressBar)
            if (it != null) {
                if (it.isSucceed) {
                    mAdapter.submitList(it.entity)
                } else {
                    toast(it.message)
                }
            } else {
                snack(Constants.SERVER_ERROR, onAction = {
                    viewModel.tryGetSummeryReportAgain()
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.recyclerView.visibility = View.INVISIBLE
                })
            }
        }
    }

    private fun initViews() {
        mBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mBinding.recyclerView.adapter = mAdapter
    }

}