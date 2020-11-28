package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemReportFullBinding
import com.behraz.fastermixer.batch.models.FullReport

class FullReportAdapter :
    ListAdapter<FullReport, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FullReport>() {
            override fun areItemsTheSame(oldItem: FullReport, newItem: FullReport) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FullReport, newItem: FullReport) =
                oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemReportFullBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_report_full,
            parent,
            false
        )
        return FullReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FullReportViewHolder -> {
                holder.bindFullReport(currentList[position])
            }
        }
    }

    inner class FullReportViewHolder(private val mBinding: ItemReportFullBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bindFullReport(item: FullReport) {
            mBinding.data = item
            mBinding.executePendingBindings()
        }
    }
}

