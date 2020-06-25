package com.behraz.fastermixer.batch.ui.activities.batch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Batch
import com.behraz.fastermixer.batch.ui.adapters.BatchAdapter
import com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel
import com.behraz.fastermixer.batch.utils.fastermixer.fakeBatches
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeGpsStateChangeListener
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeNetworkStateChangeListener
import com.behraz.fastermixer.batch.utils.general.toast
import kotlinx.android.synthetic.main.activity_choose_batch.*

class ChooseBatchActivity : AppCompatActivity(), FasterMixerUserPanel.Interactions,
    BatchAdapter.Interaction {

    private lateinit var mAdapter: BatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_batch)



       initViews()

        subscribeNetworkStateChangeListener { userPanel.setInternetState(it) }
        subscribeGpsStateChangeListener { userPanel.setGPSState(it) }
    }

    private fun initViews() {
        userPanel.setInteractions(this)


        mAdapter = BatchAdapter(this)
        batchRecycler.adapter = mAdapter
        batchRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        //TODO ui test purpose
        mAdapter.submitList(fakeBatches())

    }

    override fun onLogoutClicked(view: View) {
        println("logout")
    }

    override fun onHideBarClicked(view: View) {
        println("onHideBarClicked")
    }

    override fun onCallClicked(view: View) {
        println("onCallClicked")
    }

    override fun onItemClicked(item: Batch) {
        if(!item.isAvailable) {
            toast("این بچ درحال استفاده توسط کاربر دیگری میباشد. در صورت وجود مشکل با هماهنگ کننده تماس بگیرید")
        } else {
            startActivity(Intent(this, BatchActivity::class.java))
        }

    }
}