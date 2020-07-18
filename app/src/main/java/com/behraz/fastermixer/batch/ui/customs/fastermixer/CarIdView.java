package com.behraz.fastermixer.batch.ui.customs.fastermixer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.behraz.fastermixer.batch.R;

public class CarIdView extends LinearLayout {
    private TextView tvFirst;
    private TextView tvSecond;
    private TextView tvThird;
    private TextView tvForth;


    public CarIdView(Context context) {
        super(context);
    }

    public CarIdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CarIdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CarIdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_layout_car_id, this);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CarIdView);

        tvFirst = view.findViewById(R.id.tvFirst);
        tvSecond = view.findViewById(R.id.tvSecond);
        tvThird = view.findViewById(R.id.tvThird);
        tvForth = view.findViewById(R.id.tvForth);


        tvFirst.setText(attributes.getText(R.styleable.CarIdView_firstText));
        tvSecond.setText(attributes.getText(R.styleable.CarIdView_secondText));
        tvThird.setText(attributes.getText(R.styleable.CarIdView_thirdText));
        tvForth.setText(attributes.getText(R.styleable.CarIdView_forthText));


        attributes.recycle();

    }



    public void setText(String carId) {
        String[] parts = carId.split(",");
        tvFirst.setText(parts[0]);
        tvSecond.setText(parts[1]);
        tvThird.setText(parts[2]);
        tvForth.setText(parts[3]);
    }

    public void setText(String first, String second, String third, String forth) {
        tvFirst.setText(first);
        tvSecond.setText(second);
        tvThird.setText(third);
        tvForth.setText(forth);
    }

}
