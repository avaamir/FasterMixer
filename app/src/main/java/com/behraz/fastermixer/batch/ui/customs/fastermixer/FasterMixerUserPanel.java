package com.behraz.fastermixer.batch.ui.customs.fastermixer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.behraz.fastermixer.batch.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FasterMixerUserPanel extends LinearLayout {

    private Interactions interactions;
    private ImageView ivNetState;
    private ImageView ivGpsState;
    private ImageView ivVoipState;
    private ImageView ivProfile;
    //private View btnBottomHideBar;
    // private View btnTopHideBar;
    private View btnCall;
    private View btnLogout;
    private TextView tvUsername;
    private TextView tvPersonalCode;
    private View frameVOIP;
    private FloatingActionButton btnRecord;


    public FasterMixerUserPanel(Context context) {
        super(context);
    }

    public FasterMixerUserPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FasterMixerUserPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FasterMixerUserPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View root = inflate(getContext(), R.layout.view_faster_mixer_user_panel, this);


        HorizontalScrollView scrollUserPanel = root.findViewById(R.id.scrollUserPanel);
        scrollUserPanel.post(() -> scrollUserPanel.fullScroll(HorizontalScrollView.FOCUS_RIGHT));


        ivProfile = root.findViewById(R.id.iv_profile);
        //btnBottomHideBar = root.findViewById(R.id.btnHideBar);
        // btnTopHideBar = root.findViewById(R.id.btnTopHideBar);
        ivNetState = root.findViewById(R.id.ivInternet);
        ivGpsState = root.findViewById(R.id.ivGPS);
        ivVoipState = root.findViewById(R.id.ivVoip);
        btnLogout = root.findViewById(R.id.btnLogout);
        btnCall = root.findViewById(R.id.btnCall);
        tvUsername = root.findViewById(R.id.tv_username);
        tvPersonalCode = root.findViewById(R.id.tv_personal_code);
        frameVOIP = root.findViewById(R.id.frame_voip);
        btnRecord = root.findViewById(R.id.btnRecord);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.FasterMixerUserPanel);

        /*boolean shouldShowBottomHideButton = attributes.getBoolean(R.styleable.FasterMixerUserPanel_showBottomHideBtn, false);
        if (shouldShowBottomHideButton) {
            btnBottomHideBar.setVisibility(View.VISIBLE);
        } else {
            btnBottomHideBar.setVisibility(View.GONE);
        }
        boolean shouldShowTopHideButton = attributes.getBoolean(R.styleable.FasterMixerUserPanel_showTopHideBtn, false);
        if (shouldShowTopHideButton) {
            btnTopHideBar.setVisibility(View.VISIBLE);
        } else {
            btnTopHideBar.setVisibility(View.GONE);
        }*/
        boolean shouldShowFrameVoip = attributes.getBoolean(R.styleable.FasterMixerUserPanel_showFrameVoip, false);
        if (shouldShowFrameVoip) {
            frameVOIP.setVisibility(View.VISIBLE);
        } else {
            frameVOIP.setVisibility(View.GONE);
        }

        String username = attributes.getString(R.styleable.FasterMixerUserPanel_username);
        if (username != null)
            tvUsername.setText(username);
        String personalCode = attributes.getString(R.styleable.FasterMixerUserPanel_personalCode);
        if (personalCode != null)
            tvPersonalCode.setText(personalCode);


        btnCall.setOnClickListener(btnCall -> {
            if (interactions != null) {
                interactions.onCallClicked(btnCall);
            }
        });
        btnLogout.setOnClickListener(btnLogout -> {
            if (interactions != null) {
                interactions.onLogoutClicked(btnLogout);
            }
        });

        btnRecord.setOnClickListener(btnRecord -> {
            if (interactions != null)
                interactions.onRecordClicked(this.btnRecord);
        });

        /*btnTopHideBar.setOnClickListener(btnHideBar -> {
            if (interactions != null) {
                interactions.onHideBarClicked(btnHideBar);
            }
        });*/


        attributes.recycle();
    }

    /*public void showBtnHideBar(boolean shouldShow) {
        if (shouldShow)
            btnTopHideBar.setVisibility(View.VISIBLE);
        else
            btnTopHideBar.setVisibility(View.GONE);
    }*/

    public void setInternetState(boolean isOk) {
        if (isOk) {
            ivNetState.setImageResource(R.drawable.ic_check);
        } else {
            ivNetState.setImageResource(R.drawable.ic_error);
        }
    }

    public void setGPSState(boolean isOk) {
        if (isOk) {
            ivGpsState.setImageResource(R.drawable.ic_check);
        } else {
            ivGpsState.setImageResource(R.drawable.ic_error);
        }
    }


    public void setVoipState(boolean isOk) {
        if (isOk) {
            ivVoipState.setImageResource(R.drawable.ic_check);
        } else {
            ivVoipState.setImageResource(R.drawable.ic_error);
        }
    }


    public ImageView getIvProfile() {
        return ivProfile;
    }

    public void setInteractions(Interactions interactions) {
        this.interactions = interactions;
    }

    public void setUsername(String username) {
        tvUsername.setText(username);
    }

    public void setPersonalCode(String personalCode) {
        tvPersonalCode.setText(personalCode);
    }

    public interface Interactions {

        void onCallClicked(View view);

        void onLogoutClicked(View view);

        void onRecordClicked(FloatingActionButton btnRecord);
    }

}
