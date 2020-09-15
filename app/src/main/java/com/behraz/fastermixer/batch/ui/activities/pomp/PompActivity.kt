package com.behraz.fastermixer.batch.ui.activities.pomp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.databinding.ActivityPompBinding
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.activities.mixer.MixerActivity
import com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.ui.dialogs.PompMessageDialog
import com.behraz.fastermixer.batch.ui.dialogs.RecordingDialogFragment
import com.behraz.fastermixer.batch.ui.fragments.mixer.MixerMapFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.CustomerListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MessageListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MixerListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.PompMapFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import kotlinx.android.synthetic.main.activity_batch.*
import kotlinx.android.synthetic.main.activity_test.*


class PompActivity : AppCompatActivity(), ApiService.InternetConnectionListener,
    PompMessageDialog.Interactions, ApiService.OnUnauthorizedListener {

    private var projectCount = -1 // this variable work like a flag for `onNewProjectIncome`

    private val locateMixerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Constants.ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP) {
                //TODO put mixer or mixerId in intent from MixerListFragment
                val mixerId =
                    intent.getStringExtra(Constants.ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP_MIXER_ID)
                val mixer = viewModel.allMixers.value?.entity?.find { it.id == mixerId }
                    ?: viewModel.requestMixers.value?.entity?.find { it.id == mixerId }
                if (mixer != null) {
                    onFasterMixerMenuButtonsClicked(mBinding.btnMap)
                    (supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as? PompMapFragment)?.focusOnMixer(
                        mixer
                    )
                } else {
                    toast("خطایی به وجود آمده است")
                }
            }
        }
    }

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


        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) { //age land bud bayad baghiye code ejra beshe ta 2ta fragment ijad nashe va code be moshkel nakhore
            return
        }


        viewModel = ViewModelProvider(this).get(PompActivityViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pomp)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = viewModel

        initViews()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            locateMixerReceiver,
            IntentFilter(Constants.ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP)
        )

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

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locateMixerReceiver)
    }

    private fun initViews() {

        /*TODO:: make this visible after feature added to server*/
        mBinding.btnVoiceMessage.visibility = View.GONE

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

        mBinding.btnShowAllMixersToggle.text =
            if (viewModel.shouldShowAllMixers.value!!)
                getString(R.string.pomp_mixers_on_map_toggle_all)
            else
                getString(R.string.pomp_mixers_on_map_toggle_request)


        mBinding.btnRoute.setOnClickListener {
            (supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as PompMapFragment).routeAgain()
        }

    }


    fun toggleBtnShowAllMixers(v: View) {
        viewModel.shouldShowAllMixers.value = !viewModel.shouldShowAllMixers.value!!
        if (viewModel.shouldShowAllMixers.value!!) {
            btnShowAllMixersToggle.text = getString(R.string.pomp_mixers_on_map_toggle_all)
        } else {
            btnShowAllMixersToggle.text = getString(R.string.pomp_mixers_on_map_toggle_request)
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
                    val customers = it.entity
                    if (projectCount != (customers?.size ?: 0)) {
                        projectCount = customers?.size ?: 0
                        if (customers.isNullOrEmpty()) {
                            if (viewModel.shouldShowAllMixers.value == false) {//age proje tarif nashode bud mixer haye koli ro neshun bede
                                mBinding.btnShowAllMixersToggle.callOnClick()
                            }
                        } else {
                            if (viewModel.shouldShowAllMixers.value == true) {//age prje tarif shode bud mixer proje ro neshun bede
                                mBinding.btnShowAllMixersToggle.callOnClick()
                                toast("پروژه جدید تعریف شده است")
                            }
                        }
                    }
                }
            } else {
                //todo Server Error chekar konam??
            }
        })

        viewModel.messages.observe(this, Observer { messages ->
            tvMessageCount.text = messages.size.toString()
            //TODO show like notification for some seconds then hidden it
            //TODO check if a message is critical and new show in dialog to user
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
                PompMapFragment.newInstance(mBinding.btnMyLocation.id, mBinding.btnRoute.id),
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
        mBinding.gpBtns.visibility = View.GONE
        mBinding.btnMessage.visibility = View.GONE


        when (myRaisedButton.id) {
            mBinding.btnMap.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG)!!)
                mBinding.btnMessage.visibility = View.VISIBLE
                mBinding.gpBtns.visibility = View.VISIBLE
                mBinding.btnShowAllMixersToggle.setTextColor(Color.BLACK)
                mBinding.btnShowAllMixersToggle.backgroundTintList =
                    ColorStateList.valueOf(0x190090ff)
            }
            mBinding.btnProjects.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_CUSTOMER_LIST_TAG)!!)
            }
            mBinding.btnMixers.id -> {
                mBinding.btnShowAllMixersToggle.setTextColor(Color.WHITE)
                mBinding.btnShowAllMixersToggle.visibility = View.VISIBLE
                mBinding.btnShowAllMixersToggle.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_blue))
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
        val gray = ContextCompat.getColor(this, R.color.primary_dark)
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