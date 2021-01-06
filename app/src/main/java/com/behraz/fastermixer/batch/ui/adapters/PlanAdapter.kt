package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemPlanBinding
import com.behraz.fastermixer.batch.models.Plan

class PlanAdapter(private val interactions: Interactions) : ListAdapter<Plan, PlanAdapter.PlanViewHolder>(DIFF_CALLBACK) {


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Plan>() {

            override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        return PlanViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_plan,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class PlanViewHolder(val mBinding: ItemPlanBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: Plan) {
            mBinding.plan = item
            mBinding.executePendingBindings()

            itemView.setOnClickListener {
                interactions.onItemClicked(item)
            }
        }

    }

    interface Interactions {
        fun onItemClicked(plan: Plan)
    }


}