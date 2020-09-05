package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemContactBinding
import com.behraz.fastermixer.batch.models.Contact

class ContactAdapter(private val interactions: Interactions) :
    ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {

            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.mobileNumber == newItem.mobileNumber
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem && oldItem.isChecked == newItem.isChecked
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_contact,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ContactViewHolder(private val mBinding: ItemContactBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: Contact) {
            mBinding.contact = item

            mBinding.checkBox.isChecked = item.isChecked
            mBinding.root.setOnClickListener {
                item.isChecked = !item.isChecked
                mBinding.checkBox.isChecked = item.isChecked
                interactions.onItemSelected(item)
            }

            mBinding.checkBox.setOnClickListener {
                mBinding.root.callOnClick()
            }
            mBinding.executePendingBindings()
        }
    }

    interface Interactions {
        fun onItemSelected(contact: Contact)
    }

}