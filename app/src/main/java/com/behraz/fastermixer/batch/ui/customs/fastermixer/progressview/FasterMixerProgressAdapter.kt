package com.behraz.fastermixer.batch.ui.customs.fastermixer.progressview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ViewItemProgressBinding
import com.behraz.fastermixer.batch.models.Progress
import com.behraz.fastermixer.batch.models.ProgressState
import com.behraz.fastermixer.batch.utils.general.exhaustive

class FasterMixerProgressAdapter(private val interaction: Interaction? = null) :
    ListAdapter<Progress, RecyclerView.ViewHolder>(DIFF_CALLBACK) {


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Progress>() {

            override fun areItemsTheSame(oldItem: Progress, newItem: Progress): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Progress, newItem: Progress): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProgressViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.view_item_progress,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProgressViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }


    fun getCurrentStatePosition(): Int {
        for (i in 0 until currentList.size) {
            if (currentList[i].state == ProgressState.InProgress)
                return i
        }
        return currentList.lastIndex
    }

    fun getCurrentState(): Progress {
        return currentList[getCurrentStatePosition()]
    }

    inner class ProgressViewHolder(
        private val mBinding: ViewItemProgressBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: Progress) {


            mBinding.progress = item
            mBinding.executePendingBindings()

            fun hideDoOnManual() {
                mBinding.ivBlinker.visibility = View.VISIBLE
                mBinding.btnDoManual.visibility = View.GONE
                mBinding.tvState.visibility = View.VISIBLE
            }

            when (item.state) {
                ProgressState.NotStarted -> {
                    mBinding.ivBlinker.setImageResource(R.color.primary)
                    hideDoOnManual()
                }
                ProgressState.InProgress -> {
                    if (true) {
                        mBinding.ivBlinker.visibility = View.GONE
                        mBinding.btnDoManual.visibility = View.VISIBLE
                        mBinding.tvState.visibility = View.GONE
                    } else {
                        hideDoOnManual()
                    }
                    mBinding.ivBlinker.setImageResource(R.color.yellow)
                }
                ProgressState.Done -> {
                    mBinding.ivBlinker.setImageResource(R.color.material_green)
                    hideDoOnManual()
                }
            }.exhaustive()


            mBinding.btnDoManual.setOnClickListener {
                interaction?.onDoManualClicked(item)
            }

        }
    }

    interface Interaction {
        fun onDoManualClicked(item: Progress)
    }
}