package com.behraz.fastermixer.batch.ui.activities.pomp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.databinding.ActivityPompBinding
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.openweathermap.WeatherViewData
import com.behraz.fastermixer.batch.ui.customs.general.MyRaisedButton
import com.behraz.fastermixer.batch.ui.customs.general.TopSheetBehavior
import com.behraz.fastermixer.batch.ui.dialogs.*
import com.behraz.fastermixer.batch.ui.fragments.VehicleFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.CustomerListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MessageListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MixerListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.PompMapFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import kotlinx.android.synthetic.main.activity_batch.*


class PompActivity : AppCompatActivity(),
    PompMessageDialog.Interactions,
    VehicleFragment.OnUserAndDestLocRetrieved {

    private var weatherAnimator: ViewPropertyAnimator? = null
    private lateinit var topSheetBehavior: TopSheetBehavior<View>
    private var projectCount = -1 // this variable work like a flag for `onNewProjectIncome`

    private val locateMixerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Constants.ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP) {
                //TODO put mixer or mixerId in intent from MixerListFragment
                val mixerId =
                    intent.getIntExtra(Constants.ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP_MIXER_ID, 0)
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
        registerMenus()

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

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locateMixerReceiver)
    }

    private fun initViews() {
        topSheetBehavior = TopSheetBehavior.from(mBinding.frameTop)
        topSheetBehavior.state = TopSheetBehavior.STATE_HIDDEN
        topSheetBehavior.setSwipedEnabled(false)

        mBinding.layoutNewMessage.root.setOnClickListener {
            onFasterMixerMenuButtonsClicked(mBinding.btnMessages)
        }

        mBinding.btnBroken.setOnClickListener {
            viewModel.insertBreakdown(BreakdownRequest.FIXED)
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

        mBinding.btnShowAllMixersToggle.text =
            if (viewModel.shouldShowAllMixers.value!!)
                getString(R.string.pomp_mixers_on_map_toggle_all)
            else
                getString(R.string.pomp_mixers_on_map_toggle_request)


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


    fun toggleBtnShowAllMixers(v: View) {
        viewModel.shouldShowAllMixers.value = !viewModel.shouldShowAllMixers.value!!
        if (viewModel.shouldShowAllMixers.value!!) {
            mBinding.btnShowAllMixersToggle.text = getString(R.string.pomp_mixers_on_map_toggle_all)
        } else {
            mBinding.btnShowAllMixersToggle.text =
                getString(R.string.pomp_mixers_on_map_toggle_request)
        }
    }

    private fun subscribeObservers() {

        viewModel.user.observe(this) {
            it?.let {
                //TODO add to ui
            }
        }

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

        viewModel.logoutResponse.observe(this) {
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
        }

        viewModel.customers.observe(this) {
            if (it != null) {
                if (it.isSucceed) {
                    val customers = it.entity
                    if (projectCount != (customers?.size
                            ?: 0)
                    ) { //age pishfarz projectCount, -1 nabashe age tedad proje haye tarif shode 0 bashe aslan vared in if nemishe
                        projectCount = customers?.size ?: 0
                        if (customers.isNullOrEmpty()) {
                            if (viewModel.shouldShowAllMixers.value == false) { //age proje tarif nashode bud mixer haye koli ro neshun bede
                                mBinding.btnShowAllMixersToggle.callOnClick()
                            }
                        } else {
                            if (viewModel.shouldShowAllMixers.value == true) { //age prje tarif shode bud mixer proje ro neshun bede
                                mBinding.btnShowAllMixersToggle.callOnClick()
                                toast("پروژه جدید تعریف شده است")
                            }
                        }
                    }
                }
            } else {
                //todo Server Error chekar konam??
            }
        }

        viewModel.messages.observe(this) { _messages ->
            val messageFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_MESSAGE_LIST_TAG)
            if (messageFragment != null && messageFragment.isVisible) {
                mBinding.tvMessageCount.text = "0"
            } else {
                mBinding.tvMessageCount.text = _messages.filter { !it.viewed }.count().toString()
            }
        }

        viewModel.newMessage.observe(this) { event ->
            //TODO check if a message is critical and new show in dialog to user
            event.getEventIfNotHandled()?.let { _message ->
                if (false) { //todo if (_message.priority == ?)
                    mBinding.layoutNewMessage.message = _message
                    topSheetBehavior.state = TopSheetBehavior.STATE_EXPANDED
                    Handler().postDelayed({
                        topSheetBehavior.state = TopSheetBehavior.STATE_HIDDEN
                    }, 3000)
                } else {
                    NewMessageDialog(_message, this, R.style.my_alert_dialog).show()
                }
            }
        }

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
                MixerListFragment().also { hide(it) }, FRAGMENT_MIXER_LIST_TAG
            )
            //addToBackStack(null)
            //setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(
                R.id.mapContainer,
                CustomerListFragment().also { hide(it) }, FRAGMENT_CUSTOMER_LIST_TAG
            )
            add(
                R.id.mapContainer,
                MessageListFragment().also { hide(it) }, FRAGMENT_MESSAGE_LIST_TAG
            )
            //addToBackStack(null)
            //setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            add(
                R.id.mapContainer,
                PompMapFragment.newInstance(mBinding.btnMyLocation.id, mBinding.btnRouteDest.id),
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
                val mixerListFragment =
                    supportFragmentManager.findFragmentByTag(FRAGMENT_MIXER_LIST_TAG) as MixerListFragment
                transaction.show(mixerListFragment)
                mixerListFragment.scrollToTop()
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
        viewModel.insertBreakdown(BreakdownRequest.STOP)
    }

    override fun onLabClicked() {
        viewModel.insertBreakdown(BreakdownRequest.LAB)
    }

    override fun onBrokenClicked() {
        viewModel.insertBreakdown(BreakdownRequest.BREAKDOWN)
        mBinding.btnBroken.visibility = View.VISIBLE
    }

    override fun onShowButtons(shouldShow: Boolean) {
        /*TODO Add Animation*/
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