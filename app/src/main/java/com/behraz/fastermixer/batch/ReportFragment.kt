package com.behraz.fastermixer.batch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.databinding.FragmentReportBinding
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.utils.fastermixer.Constants

class ReportFragment : Fragment(), View.OnClickListener {

    private lateinit var mBinding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.frameWorkFullReport.setOnClickListener(this)
        mBinding.frameDrawRoad.setOnClickListener(this)
        mBinding.frameWorkSummeryReport.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        navigate(
            R.id.action_reportFragment_to_chooseReportDateFragment,
            bundleOf(
                Constants.INTENT_REPORT_TYPE to when (view.id) {
                    mBinding.frameWorkFullReport.id -> Constants.REPORT_TYPE_FULL
                    mBinding.frameDrawRoad.id -> Constants.REPORT_TYPE_DRAW_ROAD
                    mBinding.frameWorkSummeryReport.id -> Constants.REPORT_TYPE_SUMMERY
                    else -> throw Exception("Illegal State")
                }
            )
        )
    }

}