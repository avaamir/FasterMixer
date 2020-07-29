package com.behraz.fastermixer.batch.ui.customs.fastermixer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.behraz.fastermixer.batch.R;

public class NumericKeyboard extends FrameLayout {
    private Interactions interactions;

    private TextView btnNext;
    private TextView btnPrev;
    private State state = State.FactoryCode;

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
        getRootView().setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View root = inflate(getContext(), R.layout.view_numeric_keyboard, this);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.NumericKeyboard);

        root.findViewById(R.id.btn_num_0).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("0");
        });
        root.findViewById(R.id.btn_num_1).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("1");
        });
        root.findViewById(R.id.btn_num_2).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("2");
        });
        root.findViewById(R.id.btn_num_3).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("3");
        });
        root.findViewById(R.id.btn_num_4).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("4");
        });
        root.findViewById(R.id.btn_num_5).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("5");
        });
        root.findViewById(R.id.btn_num_6).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("6");
        });
        root.findViewById(R.id.btn_num_7).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("7");
        });
        root.findViewById(R.id.btn_num_8).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("8");
        });
        root.findViewById(R.id.btn_num_9).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("9");
        });
        root.findViewById(R.id.btn_backspace).setOnClickListener(view -> {
            if (interactions != null) interactions.onNumClicked("backspace");
        });

        btnNext = root.findViewById(R.id.btn_next);
        btnPrev = root.findViewById(R.id.btn_prev);

        btnPrev.setOnClickListener(view -> prevState());
        btnNext.setOnClickListener(view -> nextState());
        attributes.recycle();

    }

    private void nextState() {
        switch (state) {
            case FactoryCode:
                state = State.Username;
            break;
            case Username:
                state = State.Password;
                break;
        }
        setState(state);
    }

    private void prevState() {
        switch (state) {
            case Username:
                state = State.FactoryCode;
                break;
            case Password:
                state = State.Username;
                break;
        }
        setState(state);
    }

    public void setState(State state) {
        this.state = state;
        switch (state) {
            case FactoryCode:
                btnNext.setEnabled(true);
                btnNext.setTextColor(getResources().getColor(R.color.btn_yellow));
                btnPrev.setEnabled(false);
                btnPrev.setTextColor(getResources().getColor(R.color.gray500));

                if (interactions != null)
                    interactions.onNumClicked(state.name());
                break;
            case Username:
                btnNext.setEnabled(true);
                btnNext.setTextColor(getResources().getColor(R.color.btn_yellow));
                btnPrev.setEnabled(true);
                btnPrev.setTextColor(getResources().getColor(R.color.btn_blue));

                if (interactions != null)
                    interactions.onNumClicked(state.name());
                break;

            case Password:
                btnNext.setEnabled(false);
                btnNext.setTextColor(getResources().getColor(R.color.gray500));
                btnPrev.setEnabled(true);
                btnPrev.setTextColor(getResources().getColor(R.color.btn_blue));

                if (interactions != null)
                    interactions.onNumClicked(state.name());
                break;
        }
    }




    public void setInteractions(Interactions interactions) {
        this.interactions = interactions;
    }

    public enum State {
        FactoryCode,
        Username,
        Password
    }

    public interface Interactions {
        void onNumClicked(String num);
    }
}
