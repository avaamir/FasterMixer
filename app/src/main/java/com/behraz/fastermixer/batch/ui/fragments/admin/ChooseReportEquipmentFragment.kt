package com.behraz.fastermixer.batch.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportEquipmentBinding
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.ui.adapters.AdminEquipmentAdapter
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel

class ChooseReportEquipmentFragment : Fragment(), AdminEquipmentAdapter.Interactions {

    private val mAdapter = AdminEquipmentAdapter(this, false)
    private lateinit var viewModel: AdminActivityViewModel
    private lateinit var mBinding: FragmentChooseReportEquipmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AdminActivityViewModel::class.java)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choose_report_equipment,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
    }

    private fun initViews() {
        mBinding.recycler.adapter = mAdapter
        mBinding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun subscribeObservers() {
        viewModel.equipments.observe(
            viewLifecycleOwner,
            object : Observer<Entity<List<AdminEquipment>>?> {
                override fun onChanged(it: Entity<List<AdminEquipment>>?) {
                    if (mAdapter.currentList.isEmpty())
                        viewModel.equipments.removeObserver(this)
                    crossfade(mBinding.recycler, mBinding.progressBar)
                    val items = it?.entity
                    if (items != null) {
                        if (items.isEmpty())
                            mBinding.tvNoEquipmentMessage.visibility = View.VISIBLE
                        else
                            mAdapter.submitList(items)
                    } else {
                        snack(it?.message ?: Constants.SERVER_ERROR, {
                            mBinding.progressBar.visibility = View.VISIBLE
                            viewModel.getEquipments()
                        })
                    }
                }
            })

    }

    override fun onBtnShowOnMapClicked(adminEquipment: AdminEquipment) {}

    override fun onEquipmentClicked(adminEquipment: AdminEquipment) {
        toast("findNavController().navigate()")
    }

}