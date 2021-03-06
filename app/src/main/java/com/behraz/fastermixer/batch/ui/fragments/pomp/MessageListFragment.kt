package com.behraz.fastermixer.batch.ui.fragments.pomp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.ui.activities.admin.AdminActivity
import com.behraz.fastermixer.batch.ui.activities.admin.AdminMessagesActivity
import com.behraz.fastermixer.batch.ui.activities.batch.BatchActivity
import com.behraz.fastermixer.batch.ui.activities.mixer.MixerActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.utils.general.log
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.*

class MessageListFragment : Fragment(), MessageAdapter.Interaction {


    private lateinit var gpAnimationView: View
    private val mAdapter = MessageAdapter(true, this)
    private lateinit var viewModel: EquipmentViewModel

    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        log("init")

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
            is AdminMessagesActivity -> {
                isAdmin = true
                val ref = AdminActivity.reference?.get()
                ViewModelProvider(ref!!).get(AdminActivityViewModel::class.java)
            }
            else -> {
                throw IllegalStateException("PompActivity or MixerActivity is valid")
            }
        }
        val view = inflater.inflate(
            if (isAdmin) R.layout.fragment_admin_message_list else R.layout.fragment_message_list,
            container,
            false
        )
        initViews(view)
        subscribeObservers()
        return view
    }

    private fun subscribeObservers() {
        viewModel.messages.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
            if (it.isEmpty())
                gpAnimationView.visibility = View.VISIBLE
            else
                gpAnimationView.visibility = View.GONE
        }
    }

    private fun initViews(view: View) {
        if(isAdmin) {
            view.findViewById<View>(R.id.ivBack).setOnClickListener {
                requireActivity().onBackPressed()
            }
            view.findViewById<TextView>(R.id.tvTitle).text = "پیامها و رویدادها"
        }

        gpAnimationView = view.findViewById(R.id.gpAnimationView)
        val messageRecycler = view.findViewById<RecyclerView>(R.id.messageRecycler)

        if (!isAdmin) {
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
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

            }).attachToRecyclerView(messageRecycler)
        }

        messageRecycler.adapter = mAdapter
        messageRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        messageRecycler.addItemDecoration(
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