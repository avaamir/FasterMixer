package com.behraz.fastermixer.batch.ui.activities.mixer

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.databinding.ActivityMixerBinding
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton
import com.behraz.fastermixer.batch.ui.customs.general.TopSheetBehavior
import com.behraz.fastermixer.batch.ui.dialogs.MixerMessageDialog
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.ui.dialogs.RecordingDialogFragment
import com.behraz.fastermixer.batch.ui.fragments.mixer.MixerMapFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MessageListFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import kotlinx.android.synthetic.main.activity_batch.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class MixerActivity : AppCompatActivity(),
    ApiService.InternetConnectionListener,
    ApiService.OnUnauthorizedListener,
    MixerMessageDialog.Interactions {

    private companion object {
        private const val FRAGMENT_MESSAGE_LIST_TAG = "msg-list_frag"
        private const val FRAGMENT_MAP_TAG = "map_frag"
    }

    private val progressDialog by lazy {
        MyProgressDialog(this, R.style.my_alert_dialog)
    }

    private lateinit var viewModel: MixerActivityViewModel
    private lateinit var mBinding: ActivityMixerBinding

    private lateinit var topSheetBehavior: TopSheetBehavior<View>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mixer)


        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) { //age land bud bayad baghiye code ejra beshe ta 2ta fragment ijad nashe va code be moshkel nakhore
            return
        }

        viewModel = ViewModelProvider(this).get(MixerActivityViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mixer)

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


    private fun initViews() {
        mBinding.layoutNewMessage.root.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMessages)
        }


        mBinding.tvMessageCount.text = "0"

        topSheetBehavior = TopSheetBehavior.from(mBinding.frameTop)
        topSheetBehavior.state = TopSheetBehavior.STATE_HIDDEN
        topSheetBehavior.setSwipedEnabled(false)



        mBinding.btnMessage.setOnClickListener {
            MixerMessageDialog(
                this,
                R.style.my_alert_dialog,
                this
            ).show()
        }


        /*TODO:: make this visible after feature added to server*/
        mBinding.btnVoiceMessage.visibility = View.GONE

        tvMessageCount.text = "0"
        initFragments()

        mBinding.btnMap.isEnabled = false

        //TODO too much dependencies, make MyRasiedButton OnClick Listener Ok And remove below lines, and add onClick in XML
        mBinding.btnMap.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMap)
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


        mBinding.btnWeather.setOnClickListener {
            it.animate().apply {
                interpolator = LinearInterpolator()
                duration = 500
                rotationBy(360f)
            }.start()
        }


        mBinding.btnRoute.setOnClickListener {
            (supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as MixerMapFragment).routeAgain()
        }

    }

    private fun subscribeObservers() {
        viewModel.user.observe(this, Observer {
            it?.let {
                //TODO add user personal info to ui
                /*mBinding.fasterMixerUserPanel.setUsername(it.name)
                mBinding.fasterMixerUserPanel.setPersonalCode(it.personalCode)*/
            }
        })

        viewModel.breakdownResponse.observe(this, Observer {
            if (it?.isSucceed == true) {
                toast("پیام ارسال شد")
            } else {
                toast("خطایی به وجود آمد لطفا دوباره تلاش کنید")
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
            mBinding.tvMessageCount.text = _messages.size.toString()
            //TODO show like notification for some seconds then hidden it
            //TODO check if a message is critical and new show in dialog to user
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

        viewModel.newMissionEvent.observe(this, Observer {
            if (it.peekContent().conditionTitle.contains("پروژه")) {
                mBinding.frameTimer.visibility = View.VISIBLE
                viewModel.mixerTimerValue = 0
                viewModel.mixerTimer = fixedRateTimer(period = 1000L) {
                    viewModel.mixerTimerValue++
                    CoroutineScope(Main).launch {
                        //TODO rang avaz shavad

                        val time =
                            millisToTimeString(viewModel.mixerTimerValue * 1000L).substring(10)
                        val minutes = time.substring(0, 2)
                        val seconds = time.substring(5)

                        mBinding.tvTimerMinute.text = minutes
                        mBinding.tvTimerSeconds.text = seconds
                        if (viewModel.mixerTimerValue % 2 == 0)
                            mBinding.tvTimerMiddle.visibility = View.INVISIBLE
                        else
                            mBinding.tvTimerMiddle.visibility = View.VISIBLE
                    }
                }
            } else {
                viewModel.mixerTimer?.cancel()
                viewModel.mixerTimer?.purge()
                viewModel.mixerTimer = null
                mBinding.frameTimer.visibility = View.GONE
            }
        })

    }


    private fun initFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(
                R.id.mapContainer,
                MessageListFragment(), FRAGMENT_MESSAGE_LIST_TAG
            )
            add(
                R.id.mapContainer,
                MixerMapFragment.newInstance(mBinding.btnMyLocation.id, mBinding.btnRoute.id),
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
        mBinding.frameGPSState.visibility = View.INVISIBLE
        mBinding.frameBottomButtons.visibility = View.INVISIBLE

        when (myRaisedButton.id) {
            mBinding.btnMap.id -> {
                transaction.show(supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG)!!)
                mBinding.frameGPSState.visibility = View.VISIBLE
                mBinding.frameBottomButtons.visibility = View.VISIBLE
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
        mBinding.btnMessages.setBackgroundColor(yellow)

        mBinding.btnMap.isEnabled = true
        mBinding.btnMessages.isEnabled = true

        view.isEnabled = false
        view.setBackgroundColor(gray)
    }

    override fun onBackPressed() {

        val mapFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG)!!
        if (mapFragment.isHidden) {
            onFasterMixerMenuButtonsClicked(mBinding.btnMap)
        } else {
            super.onBackPressed()
        }
        super.onBackPressed()

    }


    override fun onMessageClicked() {
        onFasterMixerMenuButtonsClicked(mBinding.btnMessages)
    }

    override fun onStopClicked() {
        viewModel.insertBreakdown(BreakdownRequest.STOP)
    }

    override fun onSOSClicked() {
        viewModel.insertBreakdown(BreakdownRequest.SOS)
    }

    override fun onRepairClicked() {
        viewModel.insertBreakdown(BreakdownRequest.BREAKDOWN)
    }

    override fun onUnauthorizedAction(event: Event<Unit>) {
        toast("شما نیاز به ورود مجدد دارید")
        finish()
    }

    override fun onInternetUnavailable() {
        NoNetworkDialog(this, R.style.my_alert_dialog).show()
    }


}