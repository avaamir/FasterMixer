package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemReportFullBinding
import com.behraz.fastermixer.batch.models.FullReport

class FullReportAdapter(private val interaction: Interactions) :
    ListAdapter<FullReport, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var progressBar: ProgressBar? = null

    companion object {
        const val TYPE_PROGRESS_BAR: Int = 1
        const val TYPE_CONTENT: Int = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FullReport>() {
            override fun areItemsTheSame(oldItem: FullReport, newItem: FullReport) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FullReport, newItem: FullReport) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_CONTENT -> {
                val binding = DataBindingUtil.inflate<ItemReportFullBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_report_full,
                    parent,
                    false
                )
                return FullReportViewHolder(binding)
            }
            TYPE_PROGRESS_BAR -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_progress_bar, parent, false)
                return ProgressBarViewHolder(view)
            }
            else -> {
                throw IllegalStateException("this view type is not defined: $viewType")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size)
            TYPE_PROGRESS_BAR
        else
            TYPE_CONTENT
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FullReportViewHolder -> {
                holder.bindFullReport(currentList[position])
            }
            is ProgressBarViewHolder -> {
                progressBar = holder.progressBar
            }
        }

        if (position == currentList.size - 3) {
            interaction.onListEnd()
        }
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.GONE
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }


    class FullReportViewHolder(private val mBinding: ItemReportFullBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bindFullReport(item: FullReport) {
            mBinding.data = item
            mBinding.executePendingBindings()
        }
    }

    class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var _progressBar: ProgressBar? = null
        val progressBar: ProgressBar
            get() = _progressBar ?: itemView.findViewById<ProgressBar>(R.id.progressBar)
                .also {
                    _progressBar = it
                }
    }

    interface Interactions {
        fun onListEnd()
    }
}

