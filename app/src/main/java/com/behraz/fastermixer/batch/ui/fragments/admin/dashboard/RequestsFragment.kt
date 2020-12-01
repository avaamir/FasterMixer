package com.behraz.fastermixer.batch.ui.fragments.admin.dashboard

import android.annotation.SuppressLint
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
import com.behraz.fastermixer.batch.databinding.LayoutFragmentRequestsBinding
import com.behraz.fastermixer.batch.ui.adapters.PlanAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel

class RequestsFragment : Fragment() {

    private lateinit var mBinding: LayoutFragmentRequestsBinding
    private lateinit var adminActivityViewModel: AdminActivityViewModel
    private val mAdapter = PlanAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminActivityViewModel = ViewModelProvider(requireActivity()).get(AdminActivityViewModel::class.java)
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_fragment_requests, container, false)
        initViews()
        subscribeObservers()
        return mBinding.root
    }

    private fun initViews() {
        mBinding.recyclerPlans.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerPlans.adapter = mAdapter
        /*mBinding.recyclerPlans.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )*/
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeObservers() {
        adminActivityViewModel.plans.observe(viewLifecycleOwner, {
            if (it?.isSucceed == true) {
                mAdapter.submitList(it.entity)
            } else {
                toast(it?.message ?: Constants.SERVER_ERROR)
            }
        })
    }

}