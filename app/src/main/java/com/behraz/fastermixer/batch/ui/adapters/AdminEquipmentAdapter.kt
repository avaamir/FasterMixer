package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemAdminEquipmentBinding
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.enums.EquipmentState
import com.behraz.fastermixer.batch.models.enums.EquipmentType
import com.behraz.fastermixer.batch.utils.general.estimateTime
import com.behraz.fastermixer.batch.utils.general.exhaustive
import com.behraz.fastermixer.batch.utils.general.minus
import com.behraz.fastermixer.batch.utils.general.now
import java.util.concurrent.TimeUnit

class AdminEquipmentAdapter(private val interactions: Interactions) :
    ListAdapter<AdminEquipment, AdminEquipmentAdapter.EquipmentViewHolder>(DIFF_CALLBACK) {

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AdminEquipment>() {

            override fun areItemsTheSame(
                oldItem: AdminEquipment,
                newItem: AdminEquipment
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AdminEquipment,
                newItem: AdminEquipment
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentViewHolder {
        return EquipmentViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_admin_equipment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    inner class EquipmentViewHolder(private val mBinding: ItemAdminEquipmentBinding) :
        RecyclerView.ViewHolder(mBinding.root) {


        fun bind(item: AdminEquipment) {
            mBinding.equipment = item
            mBinding.executePendingBindings()

            when (item.state) {
                EquipmentState.Fixing -> {
                    mBinding.tvState.text = "خراب"
                    mBinding.tvState.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.red
                        )
                    )
                }
                EquipmentState.Off -> {
                    mBinding.tvState.text = "خاموش"
                    mBinding.tvState.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.gray700
                        )
                    )
                }
                EquipmentState.Using -> {
                    mBinding.tvState.text = "روشن"
                    mBinding.tvState.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.material_green
                        )
                    )
                }
                EquipmentState.Other -> {
                    mBinding.tvState.text = "نامشخص"
                    mBinding.tvState.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.orange
                        )
                    )
                }
            }.exhaustive()

            mBinding.btnShowMixerOnMap.setOnClickListener {
                interactions.onBtnShowOnMapClicked(item)
            }
            mBinding.root.setOnClickListener { interactions.onEquipmentClicked(item) }

            mBinding.tvLastDataTime.text =
                if (item.dateTime != null) {
                    estimateTime(now() - item.dateTime, TimeUnit.SECONDS)
                } else {
                    "نامشخص"
                }

            when (item.type) {
                EquipmentType.Mixer -> mBinding.ivEquipment.setImageResource(R.drawable.ic_mixer)
                EquipmentType.Loader -> mBinding.ivEquipment.setImageResource(R.drawable.ic_loader)
                EquipmentType.Pomp -> mBinding.ivEquipment.setImageResource(R.drawable.ic_pomp)
                EquipmentType.Other -> mBinding.ivEquipment.setImageResource(R.drawable.ic_equipment_not_defined)
            }.exhaustive()


            item.carIdStr.split(",").run {
                mBinding.pelakView.setText(get(0), get(1), get(2), get(3))
            }
        }
    }

    interface Interactions {
        fun onBtnShowOnMapClicked(adminEquipment: AdminEquipment)
        fun onEquipmentClicked(adminEquipment: AdminEquipment)
    }

}