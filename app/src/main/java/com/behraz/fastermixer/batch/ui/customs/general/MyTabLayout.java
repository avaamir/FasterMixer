package com.behraz.fastermixer.batch.ui.customs.general;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.behraz.fastermixer.batch.R;
import com.google.android.material.tabs.TabLayout;


/*this tab layout is responsive according to screen size it will set tabMode to fixed or scrollable*/

public class MyTabLayout extends TabLayout implements TabLayout.OnTabSelectedListener {

    private Typeface selectedTabFace;
    private Typeface unSelectedTabFace;


    public MyTabLayout(Context context) {
        super(context);
        init();
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //default font
        selectedTabFace = ResourcesCompat.getFont(getContext(), R.font.iransans_bold);
        unSelectedTabFace = ResourcesCompat.getFont(getContext(), R.font.iransans);

        addOnTabSelectedListener(this);
        changeTabsFont(this);
    }


    /*private boolean isFirst = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (isFirst) {
            isFirst = false;

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();


            View lastTab = ((ViewGroup) this.getChildAt(0)).getChildAt(getTabCount() - 1);

            if(lastTab != null) {
                float lastTabRightDpi = lastTab.getRight() / displayMetrics.density;


                if (displayMetrics.xdpi - lastTabRightDpi > 10) {
                    getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    setTabMode(TabLayout.MODE_FIXED);
                    setTabGravity(TabLayout.GRAVITY_FILL);
                } else {
                    setTabMode(TabLayout.MODE_SCROLLABLE);
                }


                invalidate();
            }
        }
    }*/

    private void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(unSelectedTabFace);
                }
            }
        }
    }


    public void setTypeface(Typeface selectedTabFace, Typeface unSelectedTabFace) {
        this.selectedTabFace = selectedTabFace;
        this.unSelectedTabFace = unSelectedTabFace;

        changeTabsFont(this);
    }

    @Override
    public void onTabSelected(Tab tab) {
        LinearLayout linearLayout = (LinearLayout) ((ViewGroup) this.getChildAt(0)).getChildAt(tab.getPosition());
        TextView tabTextView = (TextView) linearLayout.getChildAt(1);
        tabTextView.setTypeface(selectedTabFace);
    }

    @Override
    public void onTabUnselected(Tab tab) {
        LinearLayout linearLayout = (LinearLayout) ((ViewGroup) this.getChildAt(0)).getChildAt(tab.getPosition());
        TextView tabTextView = (TextView) linearLayout.getChildAt(1);
        tabTextView.setTypeface(unSelectedTabFace);
    }

    @Override
    public void onTabReselected(Tab tab) {

    }
}
