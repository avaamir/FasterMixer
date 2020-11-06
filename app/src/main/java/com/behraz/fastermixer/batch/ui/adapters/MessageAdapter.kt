package com.behraz.fastermixer.batch.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemMessageBinding
import com.behraz.fastermixer.batch.databinding.ItemMessageVerticalBinding
import com.behraz.fastermixer.batch.models.Message

class MessageAdapter(
    private val isVertical: Boolean,
    private val interaction: Interaction? = null
) :
    ListAdapter<Message, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Message>() {

            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                if (isVertical) R.layout.item_message_vertical else R.layout.item_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                holder.bind(currentList[position])
            }
        }
    }

    fun getMessageAt(position: Int): Message = currentList[position]


    inner class MessageViewHolder(
        private val mBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(message: Message) {
            if (isVertical) {
                (mBinding as ItemMessageVerticalBinding).message = message
            } else {
                (mBinding as ItemMessageBinding).message = message
            }
            mBinding.executePendingBindings()
            itemView.setOnClickListener {
                interaction?.onItemClicked(message)
            }
            /*if (message.viewed) {
                (mBinding.root as CardView).setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                (mBinding.root as CardView).setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray500))
            }*/


        }
    }

    interface Interaction {
        fun onItemClicked(message: Message)
    }
}