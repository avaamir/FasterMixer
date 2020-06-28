package com.behraz.fastermixer.batch.ui.activities.batch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.ui.adapters.MixerAdapter
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMessages
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMixers
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeGpsStateChangeListener
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeNetworkStateChangeListener
import com.behraz.fastermixer.batch.utils.general.toast
import kotlinx.android.synthetic.main.activity_batch.*

class BatchActivity : AppCompatActivity(), MessageAdapter.Interaction, MixerAdapter.Interaction {
    private val messageAdapter = MessageAdapter(false, this)
    private val mixerAdapter = MixerAdapter(false, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch)

        initViews()

        subscribeNetworkStateChangeListener { fasterMixerUserPanel.setInternetState(it) }
        subscribeGpsStateChangeListener { fasterMixerUserPanel.setGPSState(it) }
    }



    private fun initViews() {
        //todo ui test get it from viewModel
        fasterMixerUserPanel.setPersonalCode("9441973")
        fasterMixerUserPanel.setUsername("امیرحسین مهدی پور")
        tvMessageCount.text = "12"


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

        //TODO ui test purpose
        messageAdapter.submitList(fakeMessages())
        mixerAdapter.submitList(fakeMixers())
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

}