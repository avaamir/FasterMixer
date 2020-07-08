package com.behraz.fastermixer.batch.ui.activities.batch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.dialogs.RecordingDialogFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.subscribeGpsStateChangeListener
import com.behraz.fastermixer.batch.utils.general.subscribeNetworkStateChangeListener
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.BatchActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_batch.*

class BatchActivity : AppCompatActivity(), MessageAdapter.Interaction, MixerAdapter.Interaction,
    FasterMixerUserPanel.Interactions {


    private val progressDialog by lazy {
        MyProgressDialog(this, R.style.my_alert_dialog)
    }
    private lateinit var viewModel: BatchActivityViewModel

    private val messageAdapter = MessageAdapter(false, this)
    private val mixerAdapter = MixerAdapter(false, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch)

        viewModel = ViewModelProvider(this).get(BatchActivityViewModel::class.java)

        initViews()
        subscribeObservers()

        subscribeNetworkStateChangeListener { fasterMixerUserPanel.setInternetState(it) }
        subscribeGpsStateChangeListener { fasterMixerUserPanel.setGPSState(it) }
    }

    private fun subscribeObservers() {
        viewModel.logoutResponse.observe(this, Observer {
            progressDialog.dismiss()
            if (it != null) {
                if (it.isSucceed) {
                    toast("خروج موفقیت آمیز بود")
                    finish()
                } else {
                    toast(it.message)
                }
            } else {
                snack(Constants.SERVER_ERROR) {
                    progressDialog.show()
                    viewModel.logout()
                }
            }
        })


        viewModel.messages.observe(this, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    it.entity?.let { messages ->
                        messageAdapter.submitList(messages)
                        tvMessageCount.text = messages.size.toString()
                    }
                } else {
                    //TODO is not succeed what should i do??
                    println("debug: ${it.message}")
                }
            } else {

                println("debug: getMessages() -> Server Error: returning `null`")
                //todo Server Error chekar konam??
            }
        })


        viewModel.mixers.observe(this, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    mixerAdapter.submitList(it.entity)
                    println("debug:mixers:${it.entity}")
                } else {
                    toast(it.message)
                }
            } else {
                snack(Constants.SERVER_ERROR) {
                    viewModel.refreshMixers()
                }
            }
        })

    }


    private fun initViews() {
        //todo ui test get it from viewModel
        fasterMixerUserPanel.setPersonalCode("9441973")
        fasterMixerUserPanel.setUsername("امیرحسین مهدی پور")

        fasterMixerUserPanel.setInteractions(this)


        frameNewMessage.setOnClickListener {
            toast("not implemented")
        }

        messageRecycler.adapter = messageAdapter
        messageRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, true)

        mixerRecycler.adapter = mixerAdapter
        mixerRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mixerRecycler.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )
    }

    override fun onItemClicked(message: Message) {
        toast(message.sender + ":" + message.content)
    }

    override fun onCallClicked(mixer: Mixer) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mixer.phone))
        startActivity(intent)
    }

    override fun onEndLoadingClicked(mixer: Mixer) {
        toast(mixer.carName + " onEnd")
    }

    override fun onLogoutClicked(view: View?) {
        logoutAlertMessage {
            viewModel.logout()
            progressDialog.show()
        }
    }

    override fun onRecordClicked(btnRecord: FloatingActionButton) {
        RecordingDialogFragment().show(supportFragmentManager, null)
    }

    override fun onCallClicked(view: View?) {
        TODO("Not yet implemented")
    }

}