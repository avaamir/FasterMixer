package com.behraz.fastermixer.batch.ui.fragments.admin.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentFullReportBinding
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType.NetworkError
import com.behraz.fastermixer.batch.ui.adapters.FullReportAdapter
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.log
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.FullReportViewModel
import com.behraz.fastermixer.batch.viewmodels.ReportViewModel

class FullReportFragment : Fragment(), FullReportAdapter.Interactions {

    private lateinit var viewModel: FullReportViewModel
    private lateinit var reportViewModel: ReportViewModel
    private lateinit var mBinding: FragmentFullReportBinding

    private val mAdapter = FullReportAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reportViewModel = ViewModelProvider(requireActivity()).get(ReportViewModel::class.java)
        viewModel = ViewModelProvider(this).get(FullReportViewModel::class.java)
        viewModel.request = reportViewModel.request
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_full_report, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()

        viewModel.nextPage()
    }

    private fun subscribeObservers() {
        viewModel.fullReport.observe(viewLifecycleOwner) {
            crossfade(mBinding.recyclerView, mBinding.progressBar)
            if (it.isSucceed) {
                log("onRequestSucceed")
                mAdapter.hideProgressBar()
                val data = it.entity!!
                if (data.isEmpty()) {
                    toast("در این بازه برای این خودرو هیچ دیتایی ثبت نشده است")
                    findNavController().navigateUp()
                } else {
                    mAdapter.submitList(it.entity)
                }
            } else {
                when (it.errorType) {
                    NetworkError -> {
                        snack(Constants.SERVER_ERROR, onAction = {
                            viewModel.tryGetFullReportAgain()
                            mBinding.progressBar.visibility = View.VISIBLE
                            mBinding.recyclerView.visibility = View.INVISIBLE
                        })
                    }
                    else -> toast(it.message)
                }
            }
        }
    }

    private fun initViews() {
        mBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mBinding.recyclerView.adapter = mAdapter
    }

    override fun onListEnd() {
        log("onListEnd: isPageEnd:${viewModel.isPageEnd}")
        if (!viewModel.isPageEnd) {
            viewModel.nextPage()
            mAdapter.showProgressBar()
        }
    }
}