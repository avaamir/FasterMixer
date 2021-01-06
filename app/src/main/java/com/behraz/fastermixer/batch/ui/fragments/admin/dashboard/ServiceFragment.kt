package com.behraz.fastermixer.batch.ui.fragments.admin.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Service
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType.NetworkError
import com.behraz.fastermixer.batch.ui.adapters.ServiceAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ServiceFragmentViewModel
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceFragment : Fragment(), ServiceAdapter.Interaction {
    private lateinit var viewModel: ServiceFragmentViewModel
    private val mAdapter = ServiceAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ServiceFragmentViewModel::class.java)
        if (viewModel.plan == null) {
            arguments?.let {
                viewModel.plan = it.getParcelable(Constants.INTENT_SERVICE_PLAN)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeObservers()
    }

    private fun initViews() {
        recyclerService.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerService.adapter = mAdapter
    }


    private fun subscribeObservers() {
        viewModel.activeServices.observe(viewLifecycleOwner) {
            if (it.isSucceed) {

                mAdapter.submitList(it.entity)
            } else {
                when (it.errorType) {
                    NetworkError -> snack(it.message, onAction = {
                        viewModel.getServices()
                    })
                    else -> toast(it.message)
                }
            }
        }
    }

    override fun onServiceHistoryClicked(item: Service) {
        toast(item.toString())
    }
}