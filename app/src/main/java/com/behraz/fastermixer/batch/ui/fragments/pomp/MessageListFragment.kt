package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentMessageListBinding
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.requests.behraz.Entity
import com.behraz.fastermixer.batch.ui.activities.mixer.MixerActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import kotlinx.android.synthetic.main.activity_pomp.*
import java.lang.IllegalStateException

class MessageListFragment : Fragment(), MessageAdapter.Interaction {

    private val mAdapter = MessageAdapter(true, this)
    private lateinit var mBinding: FragmentMessageListBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = when (activity) {
            is MixerActivity -> {
                ViewModelProvider(activity!!).get(MixerActivityViewModel::class.java)
            }
            is PompActivity -> {
                ViewModelProvider(activity!!).get(PompActivityViewModel::class.java)
            }
            else -> {
                throw IllegalStateException("PompActivity or MixerActivity is valid")
            }
        }
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_message_list, container, false)
        initViews()
        subscribeObservers()
        return mBinding.root
    }

    private fun subscribeObservers() {
        val observer = Observer<Entity<List<Message>>?> {
            if (it != null) {
                if (it.isSucceed) {
                    mBinding.tvMessageCount.text = (it.entity?.size ?: 0).toString()
                    mAdapter.submitList(it.entity)
                } else {
                    //TODo
                }
            } else {
                //TODo
            }
        }
        if (this.viewModel is MixerActivityViewModel) {
            (viewModel as MixerActivityViewModel).messages.observe(viewLifecycleOwner, observer)
        } else {
            (viewModel as PompActivityViewModel).messages.observe(viewLifecycleOwner, observer)
        }
    }

    private fun initViews() {
        mBinding.tvMessageCount.text = "0"
        mBinding.messageRecycler.adapter = mAdapter
        mBinding.messageRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
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
        toast("not yet implemented")
    }
}