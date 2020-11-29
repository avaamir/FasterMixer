package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemSummeryFullBinding
import com.behraz.fastermixer.batch.models.SummeryReport

class SummeryReportAdapter :
    ListAdapter<SummeryReport, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SummeryReport>() {
            override fun areItemsTheSame(oldItem: SummeryReport, newItem: SummeryReport) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: SummeryReport, newItem: SummeryReport) =
                oldItem == newItem
        }
    }

    private lateinit var layoutInflater: LayoutInflater


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::layoutInflater.isInitialized) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ItemSummeryFullBinding>(
            layoutInflater,
            R.layout.item_summery_full,
            parent,
            false
        )
        return SummeryReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SummeryReportViewHolder -> {
                holder.bindSummeryReport(currentList[position])
            }
        }
    }

    inner class SummeryReportViewHolder(private val mBinding: ItemSummeryFullBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bindSummeryReport(item: SummeryReport) {
            mBinding.data = item
            mBinding.executePendingBindings()
        }
    }
}

