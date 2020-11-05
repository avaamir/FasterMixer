package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemMixerBinding
import com.behraz.fastermixer.batch.databinding.ItemPompMixerBinding
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.estimateTime

class MixerAdapter(private val isForPomp: Boolean, interaction: Interaction) :
    ListAdapter<Mixer, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var interaction: Interaction

    init {
        when (interaction) {
            is PompAdapterInteraction -> this.interaction = interaction
            is BatchAdapterInteraction -> this.interaction = interaction
            else -> throw IllegalArgumentException("can not use Interaction, use PompAdapterInteraction or BatchAdapterInteraction")
        }

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
                    mixer.pelak.split(",").run {
                        mBinding.carId.setText(get(0), get(1), get(2), get(3))
                    }
                }


                if (mixer.speed == null) {
                    mBinding.tvSpeedState.text = ""
                } else {
                    mBinding.tvSpeedState.text = if (mixer.speed > 0) "(در حرکت)" else "(ایستاده)"
                }

                if (mixer.lastDataTimeDiff != null) {
                    if (
                        (mixer.lastDataTimeDiff > Constants.VALID_DURATION_TIME_FOR_LAST_DATA_WHEN_VEHICLE_STOP && mixer.speed == 0f) ||
                        (mixer.lastDataTimeDiff > Constants.VALID_DURATION_TIME_FOR_LAST_DATA_WHEN_VEHICLE_MOVING && mixer.speed != 0f)
                    ) {
                        mBinding.frame.setCardBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.btn_yellow
                            )
                        )
                    } else if (
                        (mixer.lastDataTimeDiff < Constants.VALID_DURATION_TIME_FOR_LAST_DATA_WHEN_VEHICLE_STOP && mixer.speed == 0f) ||
                        (mixer.lastDataTimeDiff < Constants.VALID_DURATION_TIME_FOR_LAST_DATA_WHEN_VEHICLE_MOVING && mixer.speed != 0f)
                    ) {
                        mBinding.frame.setCardBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.gray50
                            )
                        )
                    }

                    mBinding.tvLastDataTime.text = estimateTime(mixer.lastDataTimeDiff.toLong())
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