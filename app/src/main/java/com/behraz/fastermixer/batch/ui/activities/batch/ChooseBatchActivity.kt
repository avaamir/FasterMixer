package com.behraz.fastermixer.batch.ui.activities.batch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Batch
import com.behraz.fastermixer.batch.ui.adapters.BatchAdapter
import com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeGpsStateChangeListener
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeNetworkStateChangeListener
import com.behraz.fastermixer.batch.utils.general.alert
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ChooseBatchActivityViewModel
import kotlinx.android.synthetic.main.activity_choose_batch.*

class ChooseBatchActivity : AppCompatActivity(), FasterMixerUserPanel.Interactions,
    BatchAdapter.Interaction {

    private lateinit var viewModel: ChooseBatchActivityViewModel
    private lateinit var mAdapter: BatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_batch)

        viewModel = ViewModelProvider(this).get(ChooseBatchActivityViewModel::class.java)


        initViews()
        subscribeObservers()
        viewModel.getBatches()

        subscribeNetworkStateChangeListener { userPanel.setInternetState(it) }
        subscribeGpsStateChangeListener { userPanel.setGPSState(it) }
    }

    private fun subscribeObservers() {
        viewModel.batches.observe(this, Observer {
            progressBar.visibility = View.GONE
            if (it != null) {
                if (it.isSucceed) {
                    mAdapter.submitList(it.entity)
                } else {
                    toast(it.message ?: Constants.SERVER_ERROR)
                }
            } else {
                snack(Constants.SERVER_ERROR) {
                    progressBar.visibility = View.VISIBLE
                    viewModel.getBatches()
                }
            }
        })


        viewModel.chooseBatchResponse.observe(this, Observer {
            startActivity(Intent(this, BatchActivity::class.java))
            finish()
        })


        viewModel.user.observe(this, Observer { _user ->
            _user?.let {
                userPanel.setUsername(it.name)
                userPanel.setPersonalCode(it.personId)
            }
        })
    }

    private fun initViews() {
        userPanel.setInteractions(this)
        mAdapter = BatchAdapter(this)
        batchRecycler.adapter = mAdapter
        batchRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onLogoutClicked(view: View) {
        alert(
            title = "خروج از حساب",
            message = "آیا از خروج اطمینان دارید؟",
            positiveButtonText = "بله، خارج میشوم",
            negativeButtonText = "انصراف"
        ) {
            viewModel.logout()
            finish()
        }
    }

    override fun onCallClicked(view: View) {
        println("onCallClicked")
    }

    override fun onItemClicked(item: Batch) {
        if (!item.isAvailable) {
            toast("این بچ درحال استفاده توسط کاربر دیگری میباشد. در صورت وجود مشکل با هماهنگ کننده تماس بگیرید")
        } else {
            viewModel.chooseBatch(item.id)
        }

    }
}