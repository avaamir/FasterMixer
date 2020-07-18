package com.behraz.fastermixer.batch.ui.customs.general;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.behraz.fastermixer.batch.R;


public class MyRaisedButton extends LinearLayout {

    private ConstraintLayout btnFrame;
    private ProgressBar progressBar;
    private TextView tvCaption;
    private ImageView ivPic;
    private boolean hasPic;

    private OnClickListener listener;

    public MyRaisedButton(Context context) {
        super(context);
    }

    public MyRaisedButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyRaisedButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_my_raised_button, this);

        CardView card = view.findViewById(R.id.cart_view);
        ivPic = view.findViewById(R.id.iv_btn_pic);
        tvCaption = view.findViewById(R.id.tv_caption);
        btnFrame = view.findViewById(R.id.btn_frame);
        progressBar = view.findViewById(R.id.progressbar_btn);


        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.MyRaisedButton);

        int primaryColor = getContext().getResources().getColor(R.color.primary);
        card.setCardBackgroundColor(attributes.getColor(R.styleable.MyRaisedButton_backgroundColor, primaryColor));
        card.setRadius(attributes.getDimension(R.styleable.MyRaisedButton_radius, 2));


        progressBar.getIndeterminateDrawable()
                .setColorFilter(
                        attributes.getColor(
                                R.styleable.MyRaisedButton_progressTint,
                                ContextCompat.getColor(getContext(), R.color.white)
                        )
                        , android.graphics.PorterDuff.Mode.MULTIPLY
                );

        card.setUseCompatPadding(!attributes.getBoolean(R.styleable.MyRaisedButton_isFlat, false));

        //Drawable ivBtnPicDrawable = attributes.getDrawable(R.styleable.MyRaisedButton_image);
        int drawableResId = attributes.getResourceId(R.styleable.MyRaisedButton_image, -1);
        Drawable ivBtnPicDrawable = null;
        if (drawableResId != -1) {
            ivBtnPicDrawable = AppCompatResources.getDrawable(getContext(), drawableResId);
        }

        if (ivBtnPicDrawable != null) {
            ivPic.setImageDrawable(ivBtnPicDrawable);
            hasPic = true;
        } else {
            ivPic.setVisibility(GONE);
            hasPic = false;
        }

        CharSequence text = attributes.getText(R.styleable.MyRaisedButton_text);
        if (text != null && text.length() != 0) {
            tvCaption.setText(text);
        } else {
            tvCaption.setVisibility(GONE);
        }
        tvCaption.setTextColor(attributes.getColor(R.styleable.MyRaisedButton_textColor, Color.WHITE));
        tvCaption.setTextSize(TypedValue.COMPLEX_UNIT_SP, attributes.getDimension(R.styleable.MyRaisedButton_textSize, 16));

        attributes.recycle();

    }

    public void showProgressBar(boolean shouldShow) { //age progress bar show beshe listener button disable mishe
        if (shouldShow) {
            progressBar.setVisibility(VISIBLE);
            ivPic.setVisibility(INVISIBLE);
            tvCaption.setVisibility(INVISIBLE);
            btnFrame.setOnClickListener(null);
        } else {
            progressBar.setVisibility(GONE);
            if (hasPic) {
                ivPic.setVisibility(VISIBLE);
            } else {
                ivPic.setVisibility(INVISIBLE);
            }
            tvCaption.setVisibility(VISIBLE);
            btnFrame.setOnClickListener(listener);
        }
    }

    public void setImage(int imageResource) {
        ivPic.setImageResource(imageResource);
        if (ivPic.getVisibility() != View.VISIBLE) {
            ivPic.setVisibility(VISIBLE);
        }
    }

    public void setImageVisibility(boolean isVisible) {
        if (isVisible)
            ivPic.setVisibility(View.VISIBLE);
        else
            ivPic.setVisibility(View.GONE);
    }

    public void setTextVisibility(boolean isVisible) {
        if (isVisible)
            tvCaption.setVisibility(View.VISIBLE);
        else
            tvCaption.setVisibility(View.GONE);
    }

    public void setText(String text) {
        tvCaption.setText(text);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.listener = l;
        btnFrame.setOnClickListener(l);
    }

}
