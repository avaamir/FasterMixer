package com.behraz.fastermixer.batch.ui.activities.batch

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.databinding.ActivityBatchNewBinding
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.adapters.MessageAdapter
import com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel
import com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton
import com.behraz.fastermixer.batch.ui.customs.general.TopSheetBehavior
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.ui.dialogs.RecordingDialogFragment
import com.behraz.fastermixer.batch.ui.fragments.batch.BatchFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MessageListFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.BatchActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_batch.*

class BatchActivity : AppCompatActivity(), MessageAdapter.Interaction,
    FasterMixerUserPanel.Interactions, ApiService.InternetConnectionListener,
    ApiService.OnUnauthorizedListener {

    private companion object {
        private const val FRAGMENT_MESSAGE_LIST_TAG = "msg-list_frag"
        private const val FRAGMENT_BATCH_TAG = "batch_frag"
    }

    private lateinit var mBinding: ActivityBatchNewBinding
    private lateinit var viewModel: BatchActivityViewModel

    private val progressDialog by lazy {
        MyProgressDialog(this, R.style.my_alert_dialog)
    }

    private lateinit var topSheetBehavior: TopSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_new)


        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) { //age land bud bayad baghiye code ejra beshe ta 2ta fragment ijad nashe va code be moshkel nakhore
            return
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_batch_new)
        viewModel = ViewModelProvider(this).get(BatchActivityViewModel::class.java)

        initViews()
        subscribeObservers()


        subscribeNetworkStateChangeListener {
            if (it) {
                mBinding.ivInternet.setImageResource(R.drawable.ic_check)
            } else {
                mBinding.ivInternet.setImageResource(R.drawable.ic_error)
            }
        }

        subscribeGpsStateChangeListener {
            if (it) {
                mBinding.ivGPS.setImageResource(R.drawable.ic_check)
            } else {
                mBinding.ivGPS.setImageResource(R.drawable.ic_error)
            }
        }

        if (FasterMixerApplication.isDemo) {
            mBinding.layoutDemo.visibility = View.VISIBLE
        }

    }


    private fun subscribeObservers() {
        viewModel.user.observe(this, Observer {
            it?.let {
                //TODO add view to UI
                /*fasterMixerUserPanel.setUsername(it.name)
                fasterMixerUserPanel.setPersonalCode(it.personalCode)*/
            }
        })



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


        viewModel.messages.observe(this, Observer { _messages ->
            val messageFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_MESSAGE_LIST_TAG)
            if (messageFragment != null && messageFragment.isVisible) {
                mBinding.tvMessageCount.text = "0"
            } else {
                mBinding.tvMessageCount.text = _messages.filter { !it.viewed }.count().toString()
            }
        })

        viewModel.newMessage.observe(this, Observer { event ->
            event.getEventIfNotHandled()?.let { _message ->
                mBinding.layoutNewMessage.message = _message
                topSheetBehavior.state = TopSheetBehavior.STATE_EXPANDED
                Handler().postDelayed({
                    topSheetBehavior.state = TopSheetBehavior.STATE_HIDDEN
                }, 3000)
            }
        })

    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(
                R.id.container,
                MessageListFragment().also { hide(it) },
                FRAGMENT_MESSAGE_LIST_TAG
            )

            add(
                R.id.container,
                BatchFragment(),
                FRAGMENT_BATCH_TAG
            )
            commit()
        }
    }

    private fun initViews() {
        mBinding.layoutNewMessage.root.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMessages)
        }
        mBinding.tvMessageCount.text = "0"
        topSheetBehavior = TopSheetBehavior.from(mBinding.frameTop)
        topSheetBehavior.state = TopSheetBehavior.STATE_HIDDEN
        topSheetBehavior.setSwipedEnabled(false)


        /*TODO:: make this visible after feature added to server*/
        mBinding.btnVoiceMessage.visibility = View.GONE

        tvMessageCount.text = "0"
        initFragments()

        mBinding.btnMixers.isEnabled = false

        //TODO too much dependencies, make MyRasiedButton OnClick Listener Ok And remove below lines, and add onClick in XML
        mBinding.btnMixers.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMixers)
        }

        mBinding.btnMessages.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMessages)
        }



        mBinding.btnLogout.setOnClickListener {
            logoutAlertMessage {
                viewModel.logout()
                progressDialog.show()
            }
        }

        mBinding.btnVoiceMessage.setOnClickListener {
            RecordingDialogFragment().show(
                supportFragmentManager,
                null
            )
        }
    }


    private fun onFasterMixerMenuButtonsClicked(myRaisedButton: MyRaisedButton) {

        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach {
            transaction.hide(it)
        }
        mBinding.frameGPSState.visibility = View.INVISIBLE

        when (myRaisedButton.id) {
            mBinding.btnMixers.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_BATCH_TAG)!!)
                mBinding.frameGPSState.visibility = View.VISIBLE
            }
            mBinding.btnMessages.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_MESSAGE_LIST_TAG)!!)
                viewModel.seenAllMessages()
            }
        }
        transaction.commit()

        refreshUserPanelButtonsOnClick(myRaisedButton)
    }

    private fun refreshUserPanelButtonsOnClick(view: View) {
        val gray = ContextCompat.getColor(this, R.color.primary_dark)
        val yellow = ContextCompat.getColor(this, R.color.btn_yellow)

        mBinding.btnMixers.setBackgroundColor(yellow)
        mBinding.btnMessages.setBackgroundColor(yellow)

        mBinding.btnMixers.isEnabled = true
        mBinding.btnMessages.isEnabled = true

        view.isEnabled = false
        view.setBackgroundColor(gray)
    }

    override fun onItemClicked(message: Message) {
        toast(message.sender + ":" + message.content)
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
        toast("Not yet implemented")
    }


    override fun onInternetUnavailable() {
        NoNetworkDialog(this, R.style.my_alert_dialog).show()
    }

    override fun onUnauthorizedAction(event: Event<Unit>) {
        toast("شما نیاز به ورود مجدد دارید")
        finish()
    }

}