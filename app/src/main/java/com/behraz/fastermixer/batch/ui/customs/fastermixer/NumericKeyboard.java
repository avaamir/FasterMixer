package com.behraz.fastermixer.batch.ui.customs.fastermixer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.behraz.fastermixer.batch.R;
import com.behraz.fastermixer.batch.ui.adapters.ChooseEquipmentAdapter;

public class NumericKeyboard extends FrameLayout {
    private Interactions interactions;

    public NumericKeyboard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NumericKeyboard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public NumericKeyboard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        View root = inflate(getContext(), R.layout.view_numeric_keyboard, this);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.NumericKeyboard);

        root.findViewById(R.id.btn_num_0).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(0);
        });
        root.findViewById(R.id.btn_num_1).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(1);
        });
        root.findViewById(R.id.btn_num_2).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(2);
        });
        root.findViewById(R.id.btn_num_3).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(3);
        });
        root.findViewById(R.id.btn_num_4).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(4);
        });
        root.findViewById(R.id.btn_num_5).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(5);
        });
        root.findViewById(R.id.btn_num_6).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(6);
        });
        root.findViewById(R.id.btn_num_7).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(7);
        });
        root.findViewById(R.id.btn_num_8).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(8);
        });
        root.findViewById(R.id.btn_num_9).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked(9);
        });

        attributes.recycle();

    }



    public void setInteractions(Interactions interactions) {
        this.interactions = interactions;
    }

    public interface Interactions {
        void onNumClicked(int num);
    }
}
