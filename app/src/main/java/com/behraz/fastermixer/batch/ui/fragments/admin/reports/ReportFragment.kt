package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentReportBinding
import com.behraz.fastermixer.batch.models.enums.ReportType
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel

class ReportFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: ReportViewModel
    private lateinit var activityViewModel: AdminActivityViewModel
    private lateinit var mBinding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activityViewModel = ViewModelProvider(requireActivity()).get(AdminActivityViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        mBinding.frameWorkFullReport.setOnClickListener(this)
        mBinding.frameDrawRoad.setOnClickListener(this)
        mBinding.frameWorkSummeryReport.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            mBinding.frameWorkFullReport.id -> viewModel.request.reportType = ReportType.Full
            mBinding.frameDrawRoad.id -> viewModel.request.reportType = ReportType.DrawRoad
            mBinding.frameWorkSummeryReport.id -> viewModel.request.reportType = ReportType.Summery
            else -> throw Exception("Illegal State")
        }
        navigate(R.id.action_reportFragment_to_chooseReportDateFragment)
    }

}