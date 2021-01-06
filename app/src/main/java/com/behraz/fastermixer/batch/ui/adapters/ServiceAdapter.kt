package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemServiceBinding
import com.behraz.fastermixer.batch.models.Service
import com.behraz.fastermixer.batch.models.enums.ServiceState.*
import com.behraz.fastermixer.batch.utils.general.exhaustive
import java.lang.IllegalStateException

class ServiceAdapter(private val interaction: Interaction? = null) :
    ListAdapter<Service, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Service, newItem: Service) =
                oldItem == newItem
        }
    }

    private lateinit var layoutInflater: LayoutInflater


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

            when (item.serviceState) {
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
                    throw IllegalStateException("state should not be ${item.serviceState} in this api")
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

