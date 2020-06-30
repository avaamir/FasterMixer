package com.behraz.fastermixer.batch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentMessageListBinding
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter

class MessageListFragment : Fragment(), MessageAdapter.Interaction {
    private val mAdapter = MessageAdapter(true, this)
    private lateinit var mBinding: FragmentMessageListBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_list, container, false)
        initViews()
        return mBinding.root
    }

    private fun initViews() {
        mBinding.messageRecycler.adapter = mAdapter
        mBinding.messageRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.messageRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )

        mBinding.btnMap.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    override fun onItemClicked(message: Message) {
        TODO("Not yet implemented")
    }

    fun submitMessages(messages: List<Message>) {
        mAdapter.submitList(messages)
    }
}