package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
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

class MixerAdapter(private val isForPomp: Boolean, interaction: Interaction) :
    ListAdapter<Mixer, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var interaction: Interaction

    init {
        if (interaction is PompAdapterInteraction)
            this.interaction = interaction
        else if (interaction is BatchAdapterInteraction)
            this.interaction = interaction
        else
            throw IllegalArgumentException("can not use Interaction, use PompAdapterInteraction or BatchAdapterInteraction")

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Mixer>() {

            override fun areItemsTheSame(oldItem: Mixer, newItem: Mixer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Mixer, newItem: Mixer): Boolean {
                return oldItem == newItem //TODO mitavan location(lat, lng) ra az ghias hazf kard ta hey cheshmak nazanad vali dar ui chizi taghir nakonad
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
                mBinding.btnShowMixerOnMap.setOnClickListener {
                    (interaction as PompAdapterInteraction).onShowOnMapClicked(mixer)
                }
                mBinding.btnCall.setOnClickListener { interaction.onCallClicked(mixer) }
                if (mixer.pelak.isNotBlank()) {
                    mixer.pelak.split(",")
                        .run { mBinding.carId.setText(get(0), get(1), get(2), get(3)) }
                }

            } else {
                (mBinding as ItemMixerBinding).mixer = mixer
                //todo addThis When btnOnLoacEnding added:: btnOnLoacEnding added:: mBinding.btnEndLoading.setOnClickListener { (interaction as BatchAdapterInteraction).onEndLoadingClicked(mixer) }
                mBinding.btnCall.setOnClickListener { interaction.onCallClicked(mixer) }
                if (mixer.pelak.isNotBlank()) {
                    mixer.pelak.split(",")
                        .run { mBinding.carId.setText(get(0), get(1), get(2), get(3)) }
                }
            }
            mBinding.executePendingBindings()

        }
    }

    interface Interaction {
        fun onCallClicked(mixer: Mixer)
    }

    interface PompAdapterInteraction : Interaction {
        fun onShowOnMapClicked(mixer: Mixer)
    }

    interface BatchAdapterInteraction : Interaction {
        fun onEndLoadingClicked(mixer: Mixer)
    }

}