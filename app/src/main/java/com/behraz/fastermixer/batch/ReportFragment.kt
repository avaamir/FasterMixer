package com.behraz.fastermixer.batch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.behraz.fastermixer.batch.databinding.FragmentReportBinding
import com.behraz.fastermixer.batch.utils.general.toast

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
        findNavController().apply {
            when (view.id) {
                mBinding.frameWorkFullReport.id -> navigate(R.id.action_reportFragment_to_chooseReportDateFragment)
                mBinding.frameDrawRoad.id -> toast("not yet implemented")
                mBinding.frameWorkSummeryReport.id -> toast("not yet implemented")
            }
        }
    }

}