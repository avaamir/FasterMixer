package com.behraz.fastermixer.batch.ui.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemBatchBinding
import com.behraz.fastermixer.batch.models.Batch
import retrofit2.Response.error

class BatchAdapter(private val interaction: Interaction? = null) :
    ListAdapter<Batch, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Batch>() {

            override fun areItemsTheSame(oldItem: Batch, newItem: Batch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Batch, newItem: Batch): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BatchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_batch,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BatchViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class BatchViewHolder(
        private val mBinding: ItemBatchBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: Batch) {
            itemView.setOnClickListener {
                interaction?.onItemClicked(item)
            }
            mBinding.batch = item
            mBinding.executePendingBindings()

            if(item.isAvailable) {
                mBinding.tvState.setBackgroundResource(R.color.material_green)
            } else {
                mBinding.tvState.setBackgroundResource(R.color.red)
            }

        }
    }

    interface Interaction {
        fun onItemClicked(item: Batch)
    }
}