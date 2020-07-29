package com.behraz.fastermixer.batch.ui.activities.pomp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityPompBinding
import com.behraz.fastermixer.batch.models.Customer
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.models.Progress
import com.behraz.fastermixer.batch.models.ProgressState
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.animations.closeReveal
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.ui.animations.startReveal
import com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel
import com.behraz.fastermixer.batch.ui.customs.fastermixer.progressview.FasterMixerProgressView
import com.behraz.fastermixer.batch.ui.customs.general.LockableBottomSheetBehavior
import com.behraz.fastermixer.batch.ui.customs.general.TopSheetBehavior
import com.behraz.fastermixer.batch.ui.dialogs.MessageDialog
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.ui.dialogs.RecordingDialogFragment
import com.behraz.fastermixer.batch.ui.fragments.MapFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.CustomerListFragment
import com.behraz.fastermixer.batch.ui.fragments.pomp.MixerListFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.fakeProgresses
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_batch.*


class PompActivity : AppCompatActivity(),
    FasterMixerProgressView.OnStateChangedListener, ApiService.InternetConnectionListener,
    MessageDialog.Interactions, FasterMixerUserPanel.Interactions,
    ApiService.OnUnauthorizedListener {

    private companion object {
        private const val FRAGMENT_MIXER_LIST_TAG = "mixer-list_frag"
        private const val FRAGMENT_CUSTOMER_LIST_TAG = "customer-list_frag"
        private const val FRAGMENT_MESSAGE_LIST_TAG = "msg-list_frag"
        private const val FRAGMENT_MAP_TAG = "map_frag"
    }

    private val blankMixer = Mixer(
        id = "0",
        carName = "نامشخص",
        phone = null,
        carId = "--,-,---,--",
        state = "ندارد",
        driverName = "نامشخص",
        owner = "نامشخص",
        lat = "0",
        lng = "0",
        amount = 0f,
        capacity = 0f,
        ended = false,
        productTypeName = "-,-",
        totalAmount = 0f
    )

    private val blankCustomer = Customer(
        id = "0",
        name = "نامشخص",
        startTime = "نامشخص",
        address = "نامشخص",
        _slump = 0,
        _amount = 0,
        _density = 0,
        _mixerCount = 0,
        jobType = "نامشخص",
        areaStr = "CIRCLE (0, 0)"
    )

    private val progressDialog by lazy {
        MyProgressDialog(this, R.style.my_alert_dialog)
    }

    private lateinit var viewModel: PompActivityViewModel
    private lateinit var bottomSheetBehavior: LockableBottomSheetBehavior<View>
    private lateinit var topSheetBehavior: TopSheetBehavior<View>
    private var isBottomExpanded = false
    private var isTopExpanded = false
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

        subscribeNetworkStateChangeListener { mBinding.fasterMixerUserPanel.setInternetState(it) }
        subscribeGpsStateChangeListener { mBinding.fasterMixerUserPanel.setGPSState(it) }
    }

    private fun initViews() {

        supportFragmentManager.beginTransaction().apply {
            add(
                R.id.mapContainer,
                MapFragment.newInstance(mBinding.btnMyLocation.id),
                FRAGMENT_MAP_TAG
            )
            commit()
        }

        topSheetBehavior = TopSheetBehavior.from(mBinding.frameTop)
        topSheetBehavior.setSwipedEnabled(false)


        mBinding.btnArrow.setOnClickListener {
            if (isTopExpanded) {
                topSheetBehavior.state = TopSheetBehavior.STATE_HIDDEN
            } else {
                topSheetBehavior.state = TopSheetBehavior.STATE_EXPANDED
            }
            it.animate().apply {
                duration = 500
                rotationBy(if (isTopExpanded) -180f else 180f)
            }.start()
            isTopExpanded = !isTopExpanded
        }

        mBinding.layoutMixer.btnShowOnMap.setOnClickListener {
            val mapFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as MapFragment
            viewModel.mixers.value?.entity?.let { mixers ->
                if (mixers.isNotEmpty()) {
                    mixers[0].latLng.let {
                        mapFragment.moveCamera(it)
                    }
                }
            }
        }
        mBinding.layoutMixer.btnMixerList.setOnClickListener {
            if (isTopExpanded) {
                mBinding.btnArrow.callOnClick()
                mBinding.btnArrow.visibility = View.GONE
            }
            supportFragmentManager.beginTransaction().apply {
                add(
                    R.id.mapContainer,
                    MixerListFragment(), FRAGMENT_MIXER_LIST_TAG
                )
                addToBackStack(null)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                commit()
            }
            closeReveal(mBinding.btnMyLocation) { mBinding.btnMyLocation.visibility = View.GONE }
        }

        mBinding.layoutCustomer.btnCustomerList.setOnClickListener {
            if (isTopExpanded) {
                mBinding.btnArrow.callOnClick()
                mBinding.btnArrow.visibility = View.GONE
            }
            supportFragmentManager.beginTransaction().apply {
                add(
                    R.id.mapContainer,
                    CustomerListFragment(), FRAGMENT_CUSTOMER_LIST_TAG
                )
                addToBackStack(null)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                commit()
            }
            closeReveal(mBinding.btnMyLocation) { mBinding.btnMyLocation.visibility = View.GONE }
        }

        mBinding.layoutMixer.btnCall.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + viewModel.mixers.value!!.entity!![0].phone) //current mixer
                )
            )
        }

        mBinding.btnMessage.setOnClickListener {
            MessageDialog(
                this,
                R.style.my_alert_dialog,
                this
            ).show()
        }

        bottomSheetBehavior = LockableBottomSheetBehavior.from(mBinding.bottomSheet)
        bottomSheetBehavior.setSwipeEnabled(false)

        mBinding.btnHideUserPanel.setOnClickListener {
            if (isBottomExpanded) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            it.animate().apply {
                interpolator = OvershootInterpolator()
                duration = 500
                rotationBy(if (isBottomExpanded) -180f else 180f)
            }.start()
            isBottomExpanded = !isBottomExpanded
        }


        mBinding.btnWeather.setOnClickListener {
            it.animate().apply {
                interpolator = LinearInterpolator()
                duration = 500
                rotationBy(360f)
            }.start()
        }


        mBinding.fasterMixerUserPanel.setInteractions(this)


        mBinding.jobProgressView.addOnStateChangedListener(this)
        mBinding.jobProgressView.setProgressItems(
            fakeProgresses(
                false
            )
        ) //todo ui test purpose get it from viewModel

        mBinding.layoutMixer.mixer = blankMixer
        mBinding.layoutMixer.carId.setText(blankMixer.carId)
        mBinding.layoutCustomer.customer = blankCustomer
    }

    private fun subscribeObservers() {
        viewModel.user.observe(this, Observer {
            it?.let {
                mBinding.fasterMixerUserPanel.setUsername(it.name)
                mBinding.fasterMixerUserPanel.setPersonalCode(it.personalCode)
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


        viewModel.mixers.observe(this, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    it.entity?.let { mixers ->
                        if (mixers.isNotEmpty()) {
                            mBinding.layoutMixer.mixer = mixers[0]
                            if (mixers[0].carId.isNotBlank()) {
                                mBinding.layoutMixer.carId.setText(mixers[0].carId)
                            }
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


        viewModel.customers.observe(this, Observer {
            if (it != null) {
                if (it.isSucceed) {
                    it.entity?.let { customers ->
                        //TODO customers[0] hamishegi nist va vaghti darkhast aval tamum shod bayad customers[1] ro neshun bede
                        if (customers.isNotEmpty()) {
                            mBinding.layoutCustomer.customer = customers[0]
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


    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 1) {
            mBinding.btnArrow.visibility = View.VISIBLE
            mBinding.btnMyLocation.visibility = View.VISIBLE
            startReveal(mBinding.btnArrow) {}
            startReveal(mBinding.btnMyLocation) {}
        }

        super.onBackPressed()

    }

    override fun onStateChanged(progress: Progress) {
        //TODO if topBar is not expanded expand it for some seconds then hide it
        toast(
            progress.name
        )
        if (progress.id == 1) { //get Customer Info
            crossfade(mBinding.frameCustomer, mBinding.frameMixer)
        } else if (progress.id == 3) { //beginning of progress
            crossfade(mBinding.frameMixer, mBinding.frameCustomer)
        } else if (progress.id == 4) { //finishing state
            if (progress.state == ProgressState.InProgress) {
                //TODO
            } else if (progress.state == ProgressState.Done) {
                mBinding.jobProgressView.resetProgress()
            }
        }
    }

    override fun onMessageClicked() {
        toast("Not yet implemented")
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

    override fun onLogoutClicked(view: View) {
        logoutAlertMessage {
            viewModel.logout()
            progressDialog.show()
        }
    }

    override fun onRecordClicked(btnRecord: FloatingActionButton?) {
        RecordingDialogFragment().show(supportFragmentManager, null)
    }

    override fun onCallClicked(view: View) {
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