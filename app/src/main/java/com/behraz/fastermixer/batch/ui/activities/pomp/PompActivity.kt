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
import com.behraz.fastermixer.batch.models.Progress
import com.behraz.fastermixer.batch.models.ProgressState
import com.behraz.fastermixer.batch.ui.animations.closeReveal
import com.behraz.fastermixer.batch.ui.animations.crossfade
import com.behraz.fastermixer.batch.ui.animations.startReveal
import com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel
import com.behraz.fastermixer.batch.ui.customs.fastermixer.progressview.FasterMixerProgressView
import com.behraz.fastermixer.batch.ui.customs.general.LockableBottomSheetBehavior
import com.behraz.fastermixer.batch.ui.customs.general.TopSheetBehavior
import com.behraz.fastermixer.batch.ui.dialogs.MessageDialog
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.fragments.CustomerListFragment
import com.behraz.fastermixer.batch.ui.fragments.MapFragment
import com.behraz.fastermixer.batch.ui.fragments.MixerListFragment
import com.behraz.fastermixer.batch.utils.fastermixer.*
import com.behraz.fastermixer.batch.utils.general.snack
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.PompActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior


class PompActivity : AppCompatActivity(),
    FasterMixerProgressView.OnStateChangedListener,
    MessageDialog.Interactions, FasterMixerUserPanel.Interactions {

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
            add(R.id.mapContainer, MapFragment.newInstance(mBinding.btnMyLocation.id), "frag-map")
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

        mBinding.layoutMixer.btnShowOnMap.setOnClickListener { toast("not yet implemented") }
        mBinding.layoutMixer.btnMixerList.setOnClickListener {
            if (isTopExpanded) {
                mBinding.btnArrow.callOnClick()
                mBinding.btnArrow.visibility = View.GONE
            }
            supportFragmentManager.beginTransaction().apply {
                add(R.id.mapContainer, MixerListFragment(), "frag-mixer-list")
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
                add(R.id.mapContainer, CustomerListFragment(), "frag-mixer-list")
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
                    Uri.parse("tel:" + viewModel.currentMixer.value!!.phone)
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


        /*todo viewModel.message.observe {
            mBinding.tvMessageCount.text = "${it.count}"
        }*/
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

    override fun onCallClicked(view: View?) {
        TODO("Not yet implemented")
    }
}