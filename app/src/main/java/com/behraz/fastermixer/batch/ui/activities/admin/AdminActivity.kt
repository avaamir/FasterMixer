package com.behraz.fastermixer.batch.ui.activities.admin

import android.content.res.Configuration
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.viewpager.widget.ViewPager
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityAdminBinding
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.Plan
import com.behraz.fastermixer.batch.ui.adapters.ViewPagerAdapter
import com.behraz.fastermixer.batch.ui.fragments.BaseNavFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.AdminActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.shared_toolbar.view.*

class AdminActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemReselectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener,
    BaseNavFragment.OnNavigationChangedListener {


    private val currentDest get() = fragments[mBinding.mainPager.currentItem].currentDestination

    private lateinit var mBinding: ActivityAdminBinding
    private lateinit var viewModel: AdminActivityViewModel

    // map of navigation_id to container index
    private val pageIndexToNavMenuId = mapOf(
        0 to R.id.menu_nav_account,
        1 to R.id.menu_nav_report,
        2 to R.id.menu_nav_map,
        3 to R.id.menu_nav_equipments,
        4 to R.id.menu_nav_dashboard,
    )

    // list of base destination containers
    private val fragments = listOf(
        BaseNavFragment.newInstance(R.layout.layout_base_nav_admin_account, R.id.nav_host_admin_account),
        BaseNavFragment.newInstance(R.layout.layout_base_nav_report, R.id.nav_host_report),
        BaseNavFragment.newInstance(R.layout.layout_base_nav_map, R.id.nav_host_admin_map),
        BaseNavFragment.newInstance(R.layout.layout_base_nav_equipments, R.id.nav_host_equipments),
        BaseNavFragment.newInstance(R.layout.layout_base_nav_dashboard, R.id.nav_host_dashboard)
    )


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
        viewModel.eventOnRouteToCarClicked.observe(this, { event ->
            if (!event.hasBeenHandled) {
                mBinding.bottomNav.selectedItemId = R.id.menu_nav_map
            }
        })

        viewModel.eventOnVehicleSelectedToShowOnMap.observe(this, {
            if (!it.hasBeenHandled) {
                mBinding.bottomNav.selectedItemId = R.id.menu_nav_map
            }
        })

        viewModel.eventOnShowEquipmentsDetails.observe(this, {
            it.getEventIfNotHandled()?.let {
                mBinding.bottomNav.selectedItemId = R.id.menu_nav_equipments
            }
        })

        viewModel.plans.observe(this, {
            it?.entity?.size?.let { planCount ->
                val dest = currentDest!!
                if (dest.id == R.id.requestsFragment) {
                    setToolbarTitle("${dest.label} ($planCount)")
                }
            }
        })

    }

    // control the backStack when back button is pressed
    override fun onBackPressed() {
        // get the current page
        val currentPage = mBinding.mainPager.currentItem
        val fragment = fragments[currentPage]        // check if the page navigates up
        val navigatedUp = fragment.onBackPressed()        // if no fragments were popped
        if (!navigatedUp) {
            if (currentPage != 4) { //dashboard
                setItem(4, "onBackPressed")
            } else {
                super.onBackPressed()
            }
        }
    }


    private fun initViews() {
        mBinding.mainPager.offscreenPageLimit = fragments.size
        mBinding.bottomNav.setOnNavigationItemReselectedListener(this)
        mBinding.bottomNav.setOnNavigationItemSelectedListener(this)
        mBinding.mainPager.addOnPageChangeListener(this)
        mBinding.mainPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)

        mBinding.toolbar.ivBack.setOnClickListener { onBackPressed() }
        mBinding.toolbar.frameMessage.setOnClickListener { toast(getString(R.string.msg_not_impl)) }


        //First is Dashboard so
        mBinding.mainPager.currentItem = 4
        mBinding.toolbar.ivBack.visibility = View.GONE
        setToolbarTitle("داشبورد مدیریت")
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        val position = pageIndexToNavMenuId.values.indexOf(item.itemId)
        fragments[position].popToRoot()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val position = pageIndexToNavMenuId.values.indexOf(menuItem.itemId)
        println("debux: onNavigationItemSelected -> $position, curr:${mBinding.mainPager.currentItem}")
        if (mBinding.mainPager.currentItem != position)
            setItem(position, "onNavigationItemSelected")
        return true
    }

    override fun onPageSelected(position: Int) {
        val itemId = pageIndexToNavMenuId[position] ?: R.id.menu_nav_dashboard
        println("debux: onPageSelected $position")
        if (mBinding.bottomNav.selectedItemId != itemId) {
            println("debux: onPageSelected bottomNav.selectedItemId = $itemId")
            mBinding.bottomNav.selectedItemId = itemId
        }
    }

    private fun setItem(position: Int, caller: String) {
        println("debux: $caller -> SetItem: $position")
        mBinding.mainPager.currentItem = position
        setToolbarTitle()
        setToolbarBackButton()
    }


    private fun setToolbarBackButton(destination: NavDestination? = null) {
        val rootDestinations = listOf(
            /*R.id.adminMapFragment,
            R.id.adminManageAccountFragment,
            R.id.adminEquipmentsFragment,
            R.id.reportFragment,*/
            R.id.dashboardFragment
        )

        val position = mBinding.mainPager.currentItem
        val dest = destination ?: fragments[position].currentDestination ?: return

        if (dest.id in rootDestinations) {
            if (mBinding.toolbar.ivBack.visibility != View.GONE) {
                mBinding.toolbar.ivBack.visibility = View.GONE
                TransitionManager.beginDelayedTransition(
                    mBinding.toolbar.frame_toolbar_buttons,
                    AutoTransition()
                )
            }
        } else {
            if (mBinding.toolbar.ivBack.visibility != View.VISIBLE) {
                mBinding.toolbar.ivBack.visibility = View.VISIBLE
                /*TransitionManager.beginDelayedTransition(
                    mBinding.toolbar.frame_toolbar_buttons,
                    AutoTransition()
                )*/
            }
        }

    }

    private fun setToolbarTitle(toolbarTitle: String? = null) {
        if (toolbarTitle == null) {
            val position = mBinding.mainPager.currentItem
            mBinding.toolbar.tvTitle.text = fragments[position].toolbarTitle
        } else {
            mBinding.toolbar.tvTitle.text = toolbarTitle
        }
    }


    override fun notifyNavigationChanged(destination: NavDestination, arguments: Bundle?): String {
        var toolbarTitle = destination.label

        setToolbarBackButton(destination)


        //Halat khas ke alave bar label bayad chiz haye dg ham append beshe tush
        when (destination.id) {
            R.id.requestsFragment -> {
                viewModel.plans.value?.entity?.size?.let { planCount ->
                    if (planCount != 0) {
                        toolbarTitle = "$toolbarTitle (${planCount})"
                    }
                }
            }
            R.id.fullReportFragment -> {
                val vehicle = arguments?.getParcelable<AdminEquipment>(Constants.INTENT_REPORT_VEHICLE)
                toolbarTitle = "$toolbarTitle ${vehicle?.name ?: ""}"
            }
            R.id.serviceFragment -> {
                val plan = arguments?.getParcelable<Plan>(Constants.INTENT_SERVICE_PLAN)
                toolbarTitle = "$toolbarTitle ${plan?.customerName ?: ""}"
            }
        }

        return toolbarTitle.toString().also { setToolbarTitle(it) }
    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageScrollStateChanged(state: Int) {}
}