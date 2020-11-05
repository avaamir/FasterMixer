package com.behraz.fastermixer.batch.ui.activities.admin

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityAdminBinding
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.ui.fragments.admin.AdminMapFragment
import com.behraz.fastermixer.batch.ui.fragments.admin.AdminPanelFragment
import com.behraz.fastermixer.batch.ui.fragments.admin.AdminEquipmentsFragment
import com.behraz.fastermixer.batch.ui.fragments.admin.AdminManageAccountFragment
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel

class AdminActivity : AppCompatActivity(), ApiService.OnUnauthorizedListener, ApiService.InternetConnectionListener {
    private lateinit var mBinding: ActivityAdminBinding
    private lateinit var viewModel: AdminActivityViewModel

    private var currentFragmentTag = HOME_TAG

    private companion object {
        const val EQUIPMENT_TAG = "eq-panel"
        const val HOME_TAG = "home-panel"
        const val ACCOUNT_TAG = "account-panel"
        const val MAP_TAG = "map-panel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) { //age portrait bud bayad baghiye code ejra beshe ta 2ta fragment ijad nashe va code be moshkel nakhore
            return
        }

        viewModel = ViewModelProvider(this).get(AdminActivityViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_admin)
        initViews()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.onVehicleSelectedToShowOnMap.observe(this, {
            //mBinding.bottomNav.selectedItemId = R.id.nav_map
            mBinding.bottomNav.findViewById<View>(R.id.nav_map)
                .performClick()
        })
    }


    override fun onBackPressed() {
        if (currentFragmentTag != HOME_TAG)
            mBinding.bottomNav.selectedItemId = R.id.nav_home
        else
            super.onBackPressed()
    }

    private fun initViews() {
        mBinding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    addOrRetainFragment(AdminPanelFragment::class.java, HOME_TAG)
                }
                R.id.nav_account -> {
                    addOrRetainFragment(AdminManageAccountFragment::class.java, ACCOUNT_TAG)
                }
                R.id.nav_equipments -> {
                    addOrRetainFragment(AdminEquipmentsFragment::class.java, EQUIPMENT_TAG)
                }
                R.id.nav_map -> {
                    addOrRetainFragment(AdminMapFragment::class.java, MAP_TAG)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        mBinding.bottomNav.selectedItemId = R.id.nav_home
    }


    private fun <T : Fragment> addOrRetainFragment(fragment: Class<T>, tag: String) {
        val retained = supportFragmentManager.findFragmentByTag(tag)
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach {//if it != retained
            if (it.isAdded) transaction.hide(it)
        }
        if (retained == null) {
            transaction
                .add(R.id.container, fragment.newInstance(), tag)
        } else {
            transaction.show(retained)
        }

        transaction.commit()
    }

    override fun onUnauthorizedAction(event: Event<Unit>) {
        toast("شما نیاز به ورود مجدد دارید")
        finish()
    }

    override fun onInternetUnavailable() {
        NoNetworkDialog(this, R.style.my_alert_dialog).show()
    }

}