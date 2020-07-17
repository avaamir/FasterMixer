package com.behraz.fastermixer.batch.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentAdminPanelBinding
import com.behraz.fastermixer.batch.ui.adapters.PlanAdapter
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminPanelFragmentViewModel

class AdminPanelFragment : Fragment() {

    private lateinit var mBinding: FragmentAdminPanelBinding
    private lateinit var viewModel: AdminPanelFragmentViewModel
    private val mAdapter = PlanAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AdminPanelFragmentViewModel::class.java)
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin_panel, container, false)
        initViews()
        subscribeObservers()
        return mBinding.root
    }

    private fun initViews() {
        mBinding.recyclerPlans.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerPlans.adapter = mAdapter
        mBinding.recyclerPlans.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )

        mBinding.tvAdminName.text = viewModel.userAndCompanyName
        mBinding.btnCalendar.setOnClickListener {
            toast("not yet implemented")
        }
    }

    private fun subscribeObservers() {
        viewModel.plans.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it) //not implemented server side, UI test purpose
        })
    }

}