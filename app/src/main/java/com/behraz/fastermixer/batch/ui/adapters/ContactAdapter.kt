package com.behraz.fastermixer.batch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ItemContactBinding
import com.behraz.fastermixer.batch.models.Contact

class ContactAdapter(private val interactions: Interactions) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var currentList: List<Contact> = ArrayList()


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
            mBinding.executePendingBindings()

            mBinding.checkBox.isChecked = item.isChecked
            mBinding.root.setOnClickListener {
                item.isChecked = !item.isChecked
                mBinding.checkBox.isChecked = item.isChecked
            }
            mBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                interactions.onItemSelected(item)
            }
        }
    }

    fun submitList(contacts: List<Contact>) {
        this.currentList = contacts
        notifyDataSetChanged()
    }


    interface Interactions {
        fun onItemSelected(contact: Contact)
    }

    override fun getItemCount() = currentList.size


}