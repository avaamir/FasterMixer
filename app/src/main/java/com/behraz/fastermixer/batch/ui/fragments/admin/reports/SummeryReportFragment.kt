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
import com.behraz.fastermixer.batch.ui.adapters.SummeryReportAdapter
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel
import com.behraz.fastermixer.batch.viewmodels.SummeryReportViewModel

class SummeryReportFragment : Fragment() {

    private val mAdapter = SummeryReportAdapter()
    private lateinit var mBinding: FragmentSummeryReportBinding
    private lateinit var reportViewModel: ReportViewModel
    private lateinit var viewModel: SummeryReportViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SummeryReportViewModel::class.java)
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        viewModel.request = reportViewModel.request
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_summery_report, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
        viewModel.getSummeryReport()
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