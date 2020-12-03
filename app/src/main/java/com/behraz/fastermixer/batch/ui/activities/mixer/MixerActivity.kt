package com.behraz.fastermixer.batch.ui.activities.mixer

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.databinding.ActivityMixerBinding
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.openweathermap.WeatherViewData
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton
import com.behraz.fastermixer.batch.ui.customs.general.TopSheetBehavior
import com.behraz.fastermixer.batch.ui.dialogs.*
import com.behraz.fastermixer.batch.ui.fragments.VehicleFragment
import com.behraz.fastermixer.batch.ui.fragments.mixer.MixerMapFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MessageListFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.MixerActivityViewModel
import kotlinx.android.synthetic.main.activity_batch.*
import kotlin.concurrent.fixedRateTimer

class MixerActivity : AppCompatActivity(),
    ApiService.InternetConnectionListener,
    ApiService.OnUnauthorizedListener,
    MixerMessageDialog.Interactions, VehicleFragment.OnUserAndDestLocRetrieved {

    private companion object {
        private const val FRAGMENT_MESSAGE_LIST_TAG = "msg-list_frag"
        private const val FRAGMENT_MAP_TAG = "map_frag"
    }

    private var weatherAnimator: ViewPropertyAnimator? = null
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
        registerMenus()


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

    private fun registerMenus() {
        registerForContextMenu(mBinding.btnMyLocation)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        if (v.id == mBinding.btnMyLocation.id) {
            menu.setHeaderTitle(
                createSpannableString(
                    "دریافت موقعیت مکانی از",
                    (application as FasterMixerApplication).iransansMedium,
                    Color.RED
                )
            )
            menuInflater.inflate(R.menu.choose_location_provider, menu)
            if (viewModel.isServerLocationProvider)
                menu.getItem(1).apply { title = " ✓  $title" }
            else
                menu.getItem(0).apply { title = " ✓  $title" }
            applyFontToMenu(menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.from_GPS -> {
                viewModel.isServerLocationProvider = false
                true
            }
            R.id.from_Server -> {
                viewModel.isServerLocationProvider = true
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onContextMenuClosed(menu: Menu) {
        super.onContextMenuClosed(menu)
        fullScreen()
    }

    private fun initViews() {
        mBinding.btnBroken.setOnClickListener {
            viewModel.insertBreakdown(BreakdownRequest.FIXED)
        }

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
            weatherAnimator = it.animate().apply {
                interpolator = LinearInterpolator()
                duration = 1000 * 10
                rotationBy(360f * 10)
                start()
            }
            if (viewModel.getUserLocationResponse.value != null) {
                viewModel.getCurrentWeather()
            } else {
                toast(getString(R.string.location_not_found_yet))
            }
        }


        mBinding.btnRouteDest.setOnClickListener {
            (supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as VehicleFragment).routeAgain()
        }

        mBinding.btnRouteHome.setOnClickListener {
            (supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as VehicleFragment).routeHomeOrDest(
                true
            )
        }

        mBinding.btnRouteProject.setOnClickListener {
            (supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as VehicleFragment).routeHomeOrDest(
                false
            )
        }


    }

    private fun subscribeObservers() {
        viewModel.user.observe(this, {
            it?.let {
                //TODO add user personal info to ui
                /*mBinding.fasterMixerUserPanel.setUsername(it.name)
                mBinding.fasterMixerUserPanel.setPersonalCode(it.personalCode)*/
            }
        })

        viewModel.currentWeather.observe(this) {
            weatherAnimator?.cancel()
            mBinding.btnWeather.rotationX = 0f
            mBinding.btnWeather.rotationY = 0f
            if (it != null) {
                if (it.isSucceed) {
                    WeatherDialog(
                        this,
                        R.style.my_alert_dialog,
                        WeatherViewData(it.entity!!)
                    ).show()
                } else {
                    toast(it.message)
                }
            } else {
                toast(Constants.SERVER_ERROR)
                println("debug:error:weatherAPI")
            }
        }

        viewModel.breakdownResponse.observe(this) {
            if (it?.isSucceed == true) {
                if (it.entity == BreakdownRequest.BREAKDOWN) {
                    mBinding.btnBroken.visibility = View.VISIBLE
                } else if (it.entity == BreakdownRequest.FIXED) {
                    mBinding.btnBroken.visibility = View.GONE
                }
                toast("پیام ارسال شد")
            } else {
                toast("خطایی به وجود آمد لطفا دوباره تلاش کنید")
            }
        }


        viewModel.logoutResponse.observe(this, {
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

        viewModel.messages.observe(this, { _messages ->
            val messageFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_MESSAGE_LIST_TAG)
            if (messageFragment?.isVisible == true) {
                mBinding.tvMessageCount.text = "0"
            } else {
                mBinding.tvMessageCount.text = _messages.filter { !it.viewed }.count().toString()
            }
        })

        viewModel.newMessage.observe(this, { event ->
            //TODO check if a message is critical and new show in dialog to user
            event.getEventIfNotHandled()?.let { _message ->
                if(false) { //todo if (_message.priority == ?)
                    mBinding.layoutNewMessage.message = _message
                    topSheetBehavior.state = TopSheetBehavior.STATE_EXPANDED
                    Handler().postDelayed({
                        topSheetBehavior.state = TopSheetBehavior.STATE_HIDDEN
                    }, 3000)
                } else {
                    NewMessageDialog(_message, this, R.style.my_alert_dialog).show()
                }
            }
        })

        viewModel.newMissionEvent.observe(this, {

            if (it.peekContent().conditionTitle.contains("سمت مقصد")) {
                viewModel.mixerTimerValue =
                    (now() - (it.peekContent().startMissionTime ?: now())).toInt()
                viewModel.mixerTimer = fixedRateTimer(period = 1000L) {
                    viewModel.mixerTimerValue++
                    val stateColor = when {
                        viewModel.mixerTimerValue < Constants.MIXER_MISSION_MAX_SECONDS_FOR_NORMAL_STATE -> {
                            ContextCompat.getColor(this@MixerActivity, R.color.material_green)
                        }
                        viewModel.mixerTimerValue > Constants.MIXER_MISSION_MAX_SECONDS_FOR_DANGER_STATE -> {
                            ContextCompat.getColor(this@MixerActivity, R.color.logout_red)
                        }
                        else -> {
                            ContextCompat.getColor(this@MixerActivity, R.color.btn_yellow)
                        }
                    }

                    val time = millisToTimeString(viewModel.mixerTimerValue * 1000L).substring(5)
                    val hours = time.substring(0, 2)
                    val minutes = time.substring(5, 7)
                    val seconds = time.substring(10, 12)

                    runOnUiThread {
                        mBinding.tvTimerHour.setTextColor(stateColor)
                        mBinding.tvTimerMinute.setTextColor(stateColor)
                        mBinding.tvTimerMiddle1.setTextColor(stateColor)
                        mBinding.tvTimerMiddle2.setTextColor(stateColor)
                        mBinding.tvTimerSeconds.setTextColor(stateColor)

                        mBinding.tvTimerHour.text = hours
                        mBinding.tvTimerMinute.text = minutes
                        mBinding.tvTimerSeconds.text = seconds
                        if (viewModel.mixerTimerValue % 2 == 0) {
                            mBinding.tvTimerMiddle1.visibility = View.INVISIBLE
                            mBinding.tvTimerMiddle2.visibility = View.INVISIBLE
                        } else {
                            mBinding.tvTimerMiddle1.visibility = View.VISIBLE
                            mBinding.tvTimerMiddle2.visibility = View.VISIBLE
                        }
                    }

                }
                mBinding.frameTimer.visibility = View.VISIBLE
            } else {
                viewModel.mixerTimer?.cancel()
                viewModel.mixerTimer?.purge()
                viewModel.mixerTimer = null
                runOnUiThread { mBinding.frameTimer.visibility = View.GONE }
            }
        })

        viewModel.isDamaged.observe(this) { isDamaged ->
            if (isDamaged) {
                if (mBinding.btnBroken.visibility != View.VISIBLE) {
                    mBinding.btnBroken.visibility = View.VISIBLE
                }
            } else {
                if (mBinding.btnBroken.visibility == View.VISIBLE) {
                    mBinding.btnBroken.visibility = View.GONE
                }
            }
        }
    }


    private fun initFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(
                R.id.mapContainer,
                MessageListFragment().also { hide(it) },
                FRAGMENT_MESSAGE_LIST_TAG
            )
            add(
                R.id.mapContainer,
                MixerMapFragment.newInstance(mBinding.btnMyLocation.id, mBinding.btnRouteDest.id),
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
                viewModel.seenAllMessages()
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

    override fun onBrokenClicked() {
        viewModel.insertBreakdown(BreakdownRequest.BREAKDOWN)
        mBinding.btnBroken.visibility = View.VISIBLE
    }

    override fun onUnauthorizedAction(event: Event<Unit>) {
        toast("شما نیاز به ورود مجدد دارید")
        finish()
    }

    override fun onInternetUnavailable() {
        NoNetworkDialog(this, R.style.my_alert_dialog).show()
    }

    override fun onShowButtons(shouldShow: Boolean) {
        if (shouldShow) {
            mBinding.btnRouteHome.visibility = View.VISIBLE
            mBinding.btnRouteProject.visibility = View.VISIBLE
            //mBinding.btnRouteDest.visibility = View.VISIBLE //todo chun 2ta btn dg ezafe shod in ro bardashtam
        } else {
            mBinding.btnRouteHome.visibility = View.GONE
            mBinding.btnRouteProject.visibility = View.GONE
            //mBinding.btnRouteDest.visibility = View.GONE //todo chun 2ta btn dg ezafe shod in ro bardashtam
        }
    }


}