package com.behraz.fastermixer.batch.ui.fragments.pomp

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
import com.behraz.fastermixer.batch.databinding.FragmentCustomerListBinding
import com.behraz.fastermixer.batch.ui.adapters.CustomerAdapter
import com.behraz.fastermixer.batch.viewmodels.CustomerListFragmentViewModel
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel

class CustomerListFragment: Fragment() {
    private lateinit var viewModel: PompActivityViewModel
    private lateinit var mBinding: FragmentCustomerListBinding
    private val mAdapter = CustomerAdapter( )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_list, container, false)
        initViews()
        subscribeObservers()
        return mBinding.root
    }


    private fun subscribeObservers() {
        viewModel.customers.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    mAdapter.submitList(it.entity)
                } else {
                    //TODo
                }
            } else {
                //TODo
            }
        })
    }

    private fun initViews() {
        mBinding.btnMap.setOnClickListener { activity!!.onBackPressed() }

        mBinding.mixerRecycler.adapter = mAdapter
        mBinding.mixerRecycler.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        mBinding.mixerRecycler.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }


}