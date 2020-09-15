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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.FragmentMessageListBinding
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.ui.activities.mixer.MixerActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel

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
        val observer = Observer<List<Message>> {
            mBinding.tvMessageCount.text = (it.size).toString()
            mAdapter.submitList(it)
            if (mAdapter.currentList.isNotEmpty())
                mBinding.gpAnimationView.visibility = View.GONE
        }
        if (this.viewModel is MixerActivityViewModel) {
            (viewModel as MixerActivityViewModel).messages.observe(viewLifecycleOwner, observer)
        } else {
            (viewModel as PompActivityViewModel).messages.observe(viewLifecycleOwner, observer)
        }
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
        //toast("not yet implemented")
    }
}