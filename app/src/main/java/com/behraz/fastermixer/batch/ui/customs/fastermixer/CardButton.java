package com.behraz.fastermixer.batch.ui.customs.fastermixer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.behraz.fastermixer.batch.R;


public class CardButton extends LinearLayout {
    private ImageView iv;
    private TextView tvCaption;

    public CardButton(Context context) {
        super(context);
    }

    public CardButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CardButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CardButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_card_button, this);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CardButton);


       // float height = attributes.getDimension(R.styleable.CardButton_android_layout_height, 120f);
       // float width = attributes.getDimension(R.styleable.CardButton_android_layout_width, 120f);

       // setMeasuredDimension((int) width, (int) height);


        iv = view.findViewById(R.id.iv_btn_image);
        tvCaption = view.findViewById(R.id.tvCaption);

        int resId = attributes.getResourceId(R.styleable.CardButton_btn_image, 0);
        if (resId == 0) {
            iv.setVisibility(View.GONE);
        } else {
            iv.setImageResource(resId);
        }
        tvCaption.setText(attributes.getString(R.styleable.CardButton_caption));


        attributes.recycle();


    }
}
