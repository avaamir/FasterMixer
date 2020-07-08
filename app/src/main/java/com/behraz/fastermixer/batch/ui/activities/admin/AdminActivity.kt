package com.behraz.fastermixer.batch.ui.activities.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityAdminBinding
import com.behraz.fastermixer.batch.ui.fragments.admin.AdminPanelFragment
import com.behraz.fastermixer.batch.ui.fragments.admin.EquipmentsFragment
import com.behraz.fastermixer.batch.ui.fragments.admin.ManageAccountFragment

class AdminActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityAdminBinding

    private var currentFragmentTag = HOME_TAG

    private companion object {
        const val EQUIPMENT_TAG = "eq-panel"
        const val HOME_TAG = "home-panel"
        const val ACCOUNT_TAG = "account-panel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_admin)
        initViews()
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
                    addOrRetainFragment(ManageAccountFragment::class.java, ACCOUNT_TAG)
                }
                R.id.nav_equipments -> {
                    addOrRetainFragment(EquipmentsFragment::class.java, EQUIPMENT_TAG)
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

}