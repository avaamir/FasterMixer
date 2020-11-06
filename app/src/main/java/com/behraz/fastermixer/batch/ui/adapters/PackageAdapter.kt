package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemPackageBinding
import com.behraz.fastermixer.batch.models.Package

class PackageAdapter(private val interactions: Interactions) :
    ListAdapter<Package, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Package>() {
            override fun areItemsTheSame(oldItem: Package, newItem: Package): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Package, newItem: Package): Boolean {
                return oldItem == newItem //TODO mitavan location(lat, lng) ra az ghias hazf kard ta hey cheshmak nazanad vali dar ui chizi taghir nakonad
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PackageViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_package,
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
        private val mBinding: ItemPackageBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: Package) {
            mBinding.data = item
            mBinding.executePendingBindings()

            mBinding.root.setOnClickListener { interactions.onPackageClicked(item) }

            if (item.isApplied || item.isReserved) {
                mBinding.gpDate.visibility = View.INVISIBLE
            } else {
                mBinding.gpDate.visibility = View.VISIBLE
            }

            if (!item.isApplied) {
                if (item.isReserved) {
                    mBinding.btnApply.visibility = View.VISIBLE
                    mBinding.btnReserve.text = "رزرو شد"
                    mBinding.btnReserve.setBackgroundColor(
                        ContextCompat.getColor(
                            mBinding.root.context,
                            R.color.green
                        )
                    )
                    mBinding.btnReserve.setOnClickListener(null)
                } else {
                    mBinding.btnApply.visibility = View.INVISIBLE
                    mBinding.btnReserve.text = "رزرو"
                    mBinding.btnReserve.setBackgroundColor(
                        ContextCompat.getColor(
                            mBinding.root.context,
                            R.color.btn_yellow
                        )
                    )
                    mBinding.btnReserve.setOnClickListener {
                        interactions.onReservedClicked(item)
                    }
                }
            } else {
                mBinding.btnApply.visibility = View.INVISIBLE
                mBinding.btnReserve.text = "اشتراک فعلی"
                mBinding.btnReserve.setBackgroundColor(
                    ContextCompat.getColor(
                        mBinding.root.context,
                        R.color.gray
                    )
                )
            }
        }
    }

    interface Interactions {
        fun onPackageClicked(item: Package)
        fun onReservedClicked(item: Package)
    }

}