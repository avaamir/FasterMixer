package com.behraz.fastermixer.batch.ui.fragments.admin.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutFragmentRequestsBinding
import com.behraz.fastermixer.batch.models.Plan
import com.behraz.fastermixer.batch.models.enums.PlanType
import com.behraz.fastermixer.batch.ui.adapters.MySimpleSpinnerAdapter
import com.behraz.fastermixer.batch.ui.adapters.PlanAdapter
import com.behraz.fastermixer.batch.ui.fragments.navigate
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.getEnumById
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel

class RequestsFragment : Fragment(), PlanAdapter.Interactions {

    private lateinit var mBinding: LayoutFragmentRequestsBinding
    private lateinit var adminActivityViewModel: AdminActivityViewModel
    private val mAdapter = PlanAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminActivityViewModel =
            ViewModelProvider(requireActivity()).get(AdminActivityViewModel::class.java)
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

        mBinding.spinnerSortOrder.adapter = MySimpleSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            PlanType.values().map {
                it.nameFa
            }
        )

        mBinding.spinnerSortOrder.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != -1)
                        adminActivityViewModel.planType = getEnumById(PlanType::ordinal, position)
                    /*when (position) {
                        0 -> adminActivityViewModel.planType = PlanType.Today
                        1 -> adminActivityViewModel.planType = PlanType.All
                        2 -> adminActivityViewModel.planType = PlanType.Future
                        3 -> adminActivityViewModel.planType = PlanType.Past
                        4 -> adminActivityViewModel.planType = PlanType.NotEnded
                    }*/
                }

            }
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

    override fun onItemClicked(plan: Plan) {
        navigate(R.id.action_requestsFragment_to_serviceFragment, Bundle().apply {
            putParcelable(Constants.INTENT_SERVICE_PLAN, plan)
        })
    }

}