package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemMixerBinding
import com.behraz.fastermixer.batch.databinding.ItemPompMixerBinding
import com.behraz.fastermixer.batch.models.Mixer

class MixerAdapter(private val isForPomp: Boolean, private val interaction: Interaction? = null) :
    ListAdapter<Mixer, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Mixer>() {

            override fun areItemsTheSame(oldItem: Mixer, newItem: Mixer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Mixer, newItem: Mixer): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MixerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                if (isForPomp) R.layout.item_pomp_mixer else R.layout.item_mixer,
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MixerViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }


    inner class MixerViewHolder
        (
        private val mBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(mixer: Mixer) {
            if (isForPomp) {
                (mBinding as ItemPompMixerBinding).mixer = mixer
                mBinding.btnCall.setOnClickListener { interaction?.onCallClicked(mixer) }
                if(mixer.carId.isNotBlank()) {
                    mixer.carId.split(",")
                        .run { mBinding.carId.setText(get(0), get(1), get(2), get(3)) }
                }
            } else {
                (mBinding as ItemMixerBinding).mixer = mixer
                mBinding.btnEndLoading.setOnClickListener { interaction?.onEndLoadingClicked(mixer) }
                mBinding.btnCall.setOnClickListener { interaction?.onCallClicked(mixer) }
                if(mixer.carId.isNotBlank()) {
                    mixer.carId.split(",")
                        .run { mBinding.carId.setText(get(0), get(1), get(2), get(3)) }
                }
            }
            mBinding.executePendingBindings()

        }
    }

    interface Interaction {
        fun onCallClicked(mixer: Mixer)
        fun onEndLoadingClicked(mixer: Mixer)
    }
}