package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemServiceBinding
import com.behraz.fastermixer.batch.models.Service
import com.behraz.fastermixer.batch.models.enums.ServiceState.*
import com.behraz.fastermixer.batch.utils.general.exhaustive

class ServiceAdapter(
    private val interaction: Interaction? = null,
    private val isServiceHistory: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater

    private val currentList = ArrayList<Service>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::layoutInflater.isInitialized) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ItemServiceBinding>(
            layoutInflater,
            R.layout.item_service,
            parent,
            false
        )
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ServiceViewHolder -> {
                holder.bindService(currentList[position])
            }
        }
    }

    override fun getItemCount() = currentList.size

    fun submitList(items: List<Service>) {
        currentList.clear()
        currentList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ServiceViewHolder(private val mBinding: ItemServiceBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        private val bgCurrentState by lazy {
            ContextCompat.getDrawable(itemView.context, R.drawable.bg_service_filled_current)
        }
        private val bgFinishedState by lazy {
            ContextCompat.getDrawable(itemView.context, R.drawable.bg_service_filled_done)
        }
        private val bgNotStartedState by lazy {
            ContextCompat.getDrawable(itemView.context, R.drawable.bg_service_not_filled)
        }


        fun bindService(item: Service) {
            mBinding.frameHistory.setOnClickListener {
                interaction?.onServiceHistoryClicked(item)
            }

            if (isServiceHistory) {
                mBinding.gpHistory.visibility = View.GONE
            } else {
                mBinding.gpHistory.visibility = View.VISIBLE
            }

            when (item.serviceState) {
                Created, ToBatch -> {
                    mBinding.tvLoading.background = bgNotStartedState
                    mBinding.tvToDest.background = bgNotStartedState
                    mBinding.tvUnLoading.background = bgNotStartedState
                    mBinding.tvFinished.background = bgNotStartedState
                }
                Loading -> {
                    mBinding.tvLoading.background = bgCurrentState
                    mBinding.tvToDest.background = bgNotStartedState
                    mBinding.tvUnLoading.background = bgNotStartedState
                    mBinding.tvFinished.background = bgNotStartedState
                }
                ToDest -> {
                    mBinding.tvLoading.background = bgFinishedState
                    mBinding.tvToDest.background = bgCurrentState
                    mBinding.tvUnLoading.background = bgNotStartedState
                    mBinding.tvFinished.background = bgNotStartedState
                }
                UnLoading -> {
                    mBinding.tvLoading.background = bgFinishedState
                    mBinding.tvToDest.background = bgFinishedState
                    mBinding.tvUnLoading.background = bgCurrentState
                    mBinding.tvFinished.background = bgNotStartedState
                }
                ServiceFinished -> {
                    mBinding.tvLoading.background = bgFinishedState
                    mBinding.tvToDest.background = bgFinishedState
                    mBinding.tvUnLoading.background = bgFinishedState
                    mBinding.tvFinished.background = bgFinishedState
                }
                else -> {
                    throw IllegalStateException("state should not be `${item.serviceState}` in this api")
                }
            }.exhaustive()

            mBinding.service = item
            mBinding.executePendingBindings()
        }
    }

    interface Interaction {
        fun onServiceHistoryClicked(item: Service)
    }


}

