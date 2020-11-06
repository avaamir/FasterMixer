package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemTransactionHistoryBinding
import com.behraz.fastermixer.batch.models.TransactionHistory


class TransactionAdapter : ListAdapter<TransactionHistory, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionHistory>() {
            override fun areItemsTheSame(oldItem: TransactionHistory, newItem: TransactionHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TransactionHistory, newItem: TransactionHistory): Boolean {
                return oldItem == newItem //TODO mitavan location(lat, lng) ra az ghias hazf kard ta hey cheshmak nazanad vali dar ui chizi taghir nakonad
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PackageViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_transaction_history,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PackageViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    inner class PackageViewHolder(
        private val mBinding: ItemTransactionHistoryBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: TransactionHistory) {
            mBinding.data = item
            mBinding.executePendingBindings()

            if (item.paymentResult.contains("نا")) {
                mBinding.tvPaymentResult.setTextColor(ContextCompat.getColor(mBinding.tvPaymentResult.context, R.color.red))
            } else {
                mBinding.tvPaymentResult.setTextColor(ContextCompat.getColor(mBinding.tvPaymentResult.context, R.color.green))
            }

        }
    }

}