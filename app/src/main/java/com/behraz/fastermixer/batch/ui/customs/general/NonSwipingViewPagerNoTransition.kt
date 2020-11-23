package com.behraz.fastermixer.batch.ui.customs.general

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager

class NonSwipingViewPagerNoTransition : ViewPager {

    constructor(context: Context) : super(context) {
        setMyScroller()
    }

    constructor(context: Context, attrs: AttributeSet) :
            super(context, attrs) {
        setMyScroller()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean
    {
        // Never allow swiping to switch between pages
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        return false
    }


    private fun setMyScroller() {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, MyScroller(context))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class MyScroller(context: Context) : Scroller(context, DecelerateInterpolator()) {

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, 0 /* secs */)
        }
    }
}