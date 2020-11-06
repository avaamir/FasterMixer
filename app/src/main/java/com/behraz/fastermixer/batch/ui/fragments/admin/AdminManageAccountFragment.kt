package com.behraz.fastermixer.batch.ui.fragments.admin

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
import com.behraz.fastermixer.batch.databinding.LayoutAdminManageAccountFragmentBinding
import com.behraz.fastermixer.batch.models.Package
import com.behraz.fastermixer.batch.ui.adapters.PackageAdapter
import com.behraz.fastermixer.batch.ui.adapters.TransactionAdapter
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel

class AdminManageAccountFragment: Fragment(), PackageAdapter.Interactions {


    private lateinit var adminActivityViewModel: AdminActivityViewModel
    private lateinit var mBinding: LayoutAdminManageAccountFragmentBinding

    private val packageAdapter = PackageAdapter(this)
    private val transactionAdapter = TransactionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminActivityViewModel = ViewModelProvider(this).get(AdminActivityViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_admin_manage_account_fragment, container, false)

        initViews()
        subscribeObservers()
        adminActivityViewModel.getAdminAccountPage()

        return mBinding.root
    }

    private fun initViews() {
        mBinding.btnEdit.setOnClickListener { toast("show bottom sheet or dialog, not Impelemted yet..") }
        mBinding.btnRevival.setOnClickListener { toast("not yet implemented") }

        mBinding.recyclerTransactionHistory.adapter = transactionAdapter
        mBinding.recyclerTransactionHistory.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)

        mBinding.recyclerPackages.adapter = packageAdapter
        mBinding.recyclerPackages.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, true)

    }

    private fun subscribeObservers() {
        adminActivityViewModel.adminAccountPageResponse.observe(viewLifecycleOwner, {
            packageAdapter.submitList(it.packages)
            transactionAdapter.submitList(it.transactions)
            mBinding.data = it
            mBinding.checkBoxSmsAlert.isChecked = it.isSMSAlertActive
            mBinding.progressBar2.max = it.currentPackage!!.days
        })
    }

    override fun onPackageClicked(item: Package) {
       toast("show a dialog with package description")
    }

    override fun onReservedClicked(item: Package) {
        toast("NOT YET IMPLEMENTED")
    }


}