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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentMessageListBinding
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.ui.activities.batch.BatchActivity
import com.behraz.fastermixer.batch.ui.activities.mixer.MixerActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.BatchActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.ParentViewModel
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel

class MessageListFragment : Fragment(), MessageAdapter.Interaction {

    private val mAdapter = MessageAdapter(true, this)
    private lateinit var mBinding: FragmentMessageListBinding
    private lateinit var viewModel: ParentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = when (activity) {
            is MixerActivity -> {
                ViewModelProvider(requireActivity()).get(MixerActivityViewModel::class.java)
            }
            is PompActivity -> {
                ViewModelProvider(requireActivity()).get(PompActivityViewModel::class.java)
            }
            is BatchActivity -> {
                ViewModelProvider(requireActivity()).get(BatchActivityViewModel::class.java)
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
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            if (it.isEmpty())
                mBinding.gpAnimationView.visibility = View.VISIBLE
            else
                mBinding.gpAnimationView.visibility = View.GONE
        })
    }

    private fun initViews() {

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val message = mAdapter.getMessageAt(viewHolder.absoluteAdapterPosition)
                MessageRepo.delete(message)
                toast("پیام حذف شد")
            }

        }).attachToRecyclerView(mBinding.messageRecycler)


        mBinding.messageRecycler.adapter = mAdapter
        mBinding.messageRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.messageRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )
    }


    override fun onItemClicked(message: Message) {
        //viewModel.seenMessage(message)
    }
}