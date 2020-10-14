package com.behraz.fastermixer.batch.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentEquipmentsBinding
import com.behraz.fastermixer.batch.ui.adapters.AdminEquipmentAdapter
import com.behraz.fastermixer.batch.ui.adapters.MySimpleSpinnerAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel

class EquipmentsFragment : Fragment() {

    private lateinit var mBinding: FragmentEquipmentsBinding
    private lateinit var viewModel: AdminActivityViewModel
    private val mAdapter = AdminEquipmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_equipments, container, false)
        viewModel = ViewModelProvider(activity!!).get(AdminActivityViewModel::class.java)

        initViews()
        subscribeObservers()

        return mBinding.root

    }

    private fun subscribeObservers() {
        viewModel.equipments.observe(viewLifecycleOwner, Observer {
            if (it?.isSucceed == true) {
                mAdapter.submitList(it.entity)
            } else {
                toast(it?.message ?: Constants.SERVER_ERROR)
            }
        })
    }

    private fun initViews() {
        mBinding.recyclerEquipments.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerEquipments.adapter = mAdapter
        mBinding.recyclerEquipments.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )

        mBinding.spinnerSortOrder.adapter = MySimpleSpinnerAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "بر اساس وضعیت تجهیز",
                "بر اساس نوع تجهیز"
            )
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
                    if (!viewModel.sortEquipments(position == 0)) {
                        toast("در حال دریافت اطلاعات از سرور..")
                    }
                }

            }
    }
}