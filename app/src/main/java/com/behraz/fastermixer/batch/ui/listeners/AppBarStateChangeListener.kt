package com.behraz.fastermixer.batch.ui.listeners

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

abstract class AppBarStateChangeListener : OnOffsetChangedListener {
    private var mCurrentState = AppbarState.STATE_IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        mCurrentState = when {
            offset == 0 -> {
                if (mCurrentState != AppbarState.STATE_EXPANDED) {
                    onStateChanged(appBarLayout, AppbarState.STATE_EXPANDED)
                }
                AppbarState.STATE_EXPANDED
            }
            Math.abs(offset) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != AppbarState.STATE_COLLAPSED) {
                    onStateChanged(appBarLayout, AppbarState.STATE_COLLAPSED)
                }
                AppbarState.STATE_COLLAPSED
            }
            else -> {
                if (mCurrentState != AppbarState.STATE_IDLE) {
                    onStateChanged(appBarLayout, AppbarState.STATE_IDLE)
                }
                AppbarState.STATE_IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: AppbarState)

    enum class AppbarState{
        STATE_EXPANDED,
        STATE_IDLE,
        STATE_COLLAPSED
    }
}