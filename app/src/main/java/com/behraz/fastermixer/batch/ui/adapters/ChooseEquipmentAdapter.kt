package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemChooseEquipmentBinding
import com.behraz.fastermixer.batch.models.Equipment

class ChooseEquipmentAdapter(private val interaction: Interaction? = null) :
    ListAdapter<Equipment, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Equipment>() {

            override fun areItemsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
                return oldItem.isAvailable == newItem.isAvailable && oldItem.name == newItem.name && oldItem.availabilityMessage == newItem.availabilityMessage
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return EquipmentViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_choose_equipment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EquipmentViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class EquipmentViewHolder(
        private val mBinding: ItemChooseEquipmentBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: Equipment) {
            itemView.setOnClickListener {
                interaction?.onItemClicked(item)
            }
            mBinding.batch = item
            mBinding.executePendingBindings()

            if(item.isAvailable) {
                mBinding.tvState.setBackgroundResource(R.color.btn_yellow)
            } else {
                mBinding.tvState.setBackgroundResource(R.color.btn_blue)
            }

        }
    }

    interface Interaction {
        fun onItemClicked(item: Equipment)
    }
}