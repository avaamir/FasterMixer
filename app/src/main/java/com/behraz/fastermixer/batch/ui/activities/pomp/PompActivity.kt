package com.behraz.fastermixer.batch.ui.activities.pomp

import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityPompBinding
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.ui.dialogs.PompMessageDialog
import com.behraz.fastermixer.batch.ui.dialogs.RecordingDialogFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.CustomerListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MessageListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MixerListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.PompMapFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import kotlinx.android.synthetic.main.activity_batch.*


class PompActivity : AppCompatActivity(), ApiService.InternetConnectionListener,
    PompMessageDialog.Interactions, ApiService.OnUnauthorizedListener {

    private companion object {
        private const val FRAGMENT_MIXER_LIST_TAG = "mixer-list_frag"
        private const val FRAGMENT_CUSTOMER_LIST_TAG = "customer-list_frag"
        private const val FRAGMENT_MESSAGE_LIST_TAG = "msg-list_frag"
        private const val FRAGMENT_MAP_TAG = "map_frag"
    }

    private val progressDialog by lazy {
        MyProgressDialog(this, R.style.my_alert_dialog)
    }

    private lateinit var viewModel: PompActivityViewModel
    private lateinit var mBinding: ActivityPompBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomp)

        viewModel = ViewModelProvider(this).get(PompActivityViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pomp)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = viewModel

        initViews()
        subscribeObservers()

        subscribeNetworkStateChangeListener {
            if (it) {
                mBinding.ivInternet.setImageResource(R.drawable.ic_check);
            } else {
                mBinding.ivInternet.setImageResource(R.drawable.ic_error);
            }
        }
        subscribeGpsStateChangeListener {
            if (it) {
                mBinding.ivGPS.setImageResource(R.drawable.ic_check);
            } else {
                mBinding.ivGPS.setImageResource(R.drawable.ic_error);
            }
        }
    }

    private fun initViews() {
        tvMessageCount.text = "0"
        initFragments()

        mBinding.btnMap.isEnabled = false

        //TODO too much dependencies, make MyRasiedButton OnClick Listener Ok And remove below lines, and add onClick in XML
        mBinding.btnMap.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMap)
        }
        mBinding.btnMixers.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMixers)
        }
        mBinding.btnProjects.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnProjects)
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

        mBinding.btnMessage.setOnClickListener {
            PompMessageDialog(
                this,
                R.style.my_alert_dialog,
                this
            ).show()
        }


        mBinding.btnWeather.setOnClickListener {
            it.animate().apply {
                interpolator = LinearInterpolator()
                duration = 500
                rotationBy(360f)
            }.start()
        }


    }

    private fun subscribeObservers() {
        viewModel.user.observe(this, Observer {
            it?.let {
                //TODO add to ui
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

        viewModel.customers.observe(this, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    it.entity?.let { customers ->
                        //TODO customers[0] hamishegi nist va vaghti darkhast aval tamum shod bayad customers[1] ro neshun bede
                        if (customers.isNotEmpty()) {
                            ////TODO add to ui:: customers[0] Info
                        }
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

        viewModel.messages.observe(this, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    it.entity?.let { messages ->
                        tvMessageCount.text = messages.size.toString()
                        //TODO show like notification for some seconds then hidden it
                        //TODO check if a message is critical and new show in dialog to user
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
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(
                R.id.mapContainer,
                MixerListFragment(), FRAGMENT_MIXER_LIST_TAG
            )
            //addToBackStack(null)
            //setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(
                R.id.mapContainer,
                CustomerListFragment(), FRAGMENT_CUSTOMER_LIST_TAG
            )
            add(
                R.id.mapContainer,
                MessageListFragment(), FRAGMENT_MESSAGE_LIST_TAG
            )
            //addToBackStack(null)
            //setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(
                R.id.mapContainer,
                PompMapFragment.newInstance(mBinding.btnMyLocation.id),
                FRAGMENT_MAP_TAG
            )
            commit()
        }
    }

    private fun onFasterMixerMenuButtonsClicked(myRaisedButton: MyRaisedButton) {

        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach {
            transaction.hide(it)
        }


        when (myRaisedButton.id) {
            mBinding.btnMap.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG)!!)
            }
            mBinding.btnProjects.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_CUSTOMER_LIST_TAG)!!)
            }
            mBinding.btnMixers.id -> {
                supportFragmentManager.beginTransaction()
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_MIXER_LIST_TAG)!!)
            }
            mBinding.btnMessages.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_MESSAGE_LIST_TAG)!!)
            }
        }
        transaction.commit()

        refreshUserPanelButtonsOnClick(myRaisedButton)
    }

    private fun refreshUserPanelButtonsOnClick(view: View) {
        val gray = ContextCompat.getColor(this, R.color.gray500)
        val yellow = ContextCompat.getColor(this, R.color.btn_yellow)

        mBinding.btnMap.setBackgroundColor(yellow)
        mBinding.btnProjects.setBackgroundColor(yellow)
        mBinding.btnMixers.setBackgroundColor(yellow)
        mBinding.btnMessages.setBackgroundColor(yellow)

        mBinding.btnMap.isEnabled = true
        mBinding.btnProjects.isEnabled = true
        mBinding.btnMixers.isEnabled = true
        mBinding.btnMessages.isEnabled = true

        view.isEnabled = false
        view.setBackgroundColor(gray)
    }


    override fun onBackPressed() {

        /*if (supportFragmentManager.backStackEntryCount == 1) {
            mBinding.btnMyLocation.visibility = View.VISIBLE
            startReveal(mBinding.btnMyLocation) {}
        }*/

        val mapFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG)!!
        if (mapFragment.isHidden) {
            onFasterMixerMenuButtonsClicked(mBinding.btnMap)
        } else {
            super.onBackPressed()
        }
    }

    override fun onMessageClicked() {
        onFasterMixerMenuButtonsClicked(mBinding.btnMessages)
    }

    override fun onStopClicked() {
        toast("Not yet implemented")
    }

    override fun onLabClicked() {
        toast("Not yet implemented")
    }

    override fun onRepairClicked() {
        toast("Not yet implemented")
    }

    override fun onUnauthorizedAction(event: Event<Unit>) {
        toast("شما نیاز به ورود مجدد دارید")
        finish()
    }

    override fun onInternetUnavailable() {
        NoNetworkDialog(this, R.style.my_alert_dialog).show()
    }

}