package com.behraz.fastermixer.batch.ui.customs.general;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class NonSwipeableViewPager extends ViewPager {

    private boolean isSwipeEnabled;

    public NonSwipeableViewPager(@NonNull Context context) {
        super(context);
        this.isSwipeEnabled = false;
    }

    public NonSwipeableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.isSwipeEnabled = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.isSwipeEnabled) {
            return super.onTouchEvent(ev);
        }
        return false; // Never allow swiping to switch between pages
    }

    /*
     * This method JUST determines whether we want to intercept the motion.
     * If we return true, onMotionEvent will be called and we do the actual
     * scrolling there.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.isSwipeEnabled) {
            return super.onInterceptTouchEvent(ev);
        }

        return false; // Never allow swiping to switch between pages
    }

    //down one is added for smooth scrolling

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        this.isSwipeEnabled = swipeEnabled;
    }

    public boolean isSwipeEnabled() {
        return isSwipeEnabled;
    }
}
