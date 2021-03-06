package com.behraz.fastermixer.batch.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemCustomerBinding
import com.behraz.fastermixer.batch.models.Customer

class CustomerAdapter(val interactions: Interactions) :
    ListAdapter<Customer, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Customer>() {

            override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return (oldItem == newItem)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CustomerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_customer,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomerViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    inner class CustomerViewHolder(
        private val mBinding: ItemCustomerBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: Customer) {

            mBinding.customer = item
            mBinding.executePendingBindings()


            (mBinding.root as CardView).setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (item.isSelected) R.color.btn_yellow else R.color.gray50
                )
            )

            mBinding.btnCustomerList.setOnClickListener {
                interactions.onSelectProject(item)
            }

        }
    }

    interface Interactions {
        fun onSelectProject(customer: Customer)
    }

}