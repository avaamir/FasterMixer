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
import com.behraz.fastermixer.batch.models.Plan
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType.NetworkError
import com.behraz.fastermixer.batch.ui.adapters.ServiceAdapter
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ServiceHistoryFragmentViewModel
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceHistoryFragment : Fragment() {
    private lateinit var viewModel: ServiceHistoryFragmentViewModel
    private val mAdapter = ServiceAdapter(null, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ServiceHistoryFragmentViewModel::class.java)
        if (viewModel.plan == null) {
            arguments?.let {
                val plan: Plan = it.getParcelable(Constants.INTENT_SERVICE_PLAN)!!
                val vehicleId = it.getInt(Constants.INTENT_VEHICLE_ID, 0)
                viewModel.setData(vehicleId, plan)
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
        viewModel.serviceHistory.observe(viewLifecycleOwner) {
            if (progressBar.visibility == View.VISIBLE) {
                crossfade(recyclerService, progressBar)
            }
            if (it.isSucceed) {
                if (it.entity!!.isEmpty()) {
                    tvMessage.visibility = View.VISIBLE
                } else {
                    mAdapter.submitList(it.entity)
                    tvMessage.visibility = View.GONE
                }
            } else {
                when (it.errorType) {
                    NetworkError -> snack(it.message, onAction = {
                        progressBar.visibility = View.VISIBLE
                        viewModel.getServices()
                    })
                    else -> toast(it.message)
                }
            }
        }
    }
}