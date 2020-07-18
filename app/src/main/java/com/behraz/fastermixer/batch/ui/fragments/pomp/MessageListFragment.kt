package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentMessageListBinding
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel

class MessageListFragment : Fragment(), MessageAdapter.Interaction {

    private val mAdapter = MessageAdapter(true, this)
    private lateinit var mBinding: FragmentMessageListBinding
    private lateinit var viewModel: PompActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_list, container, false)
        initViews()
        subscribeObservers()
        return mBinding.root
    }

    private fun subscribeObservers() {
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    mAdapter.submitList(it.entity)
                } else {
                    //TODo
                }
            } else {
                //TODo
            }
        })
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
}