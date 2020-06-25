package com.behraz.fastermixer.batch.ui.customs.general;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.behraz.fastermixer.batch.R;
import com.nineoldandroids.view.animation.AnimatorProxy;


public class MyFloatLabeledEditText extends FrameLayout {

    private static final int DEFAULT_PADDING_LEFT = 2;

    private TextView mHintTextView;
    private EditText mEditText;

    private Context mContext;

    public MyFloatLabeledEditText(Context context) {
        super(context);
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyFloatLabeledEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setAttributes(attrs);
    }

    public MyFloatLabeledEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setAttributes(attrs);
    }


    private void setAttributes(AttributeSet attrs) {

        mHintTextView = new TextView(mContext);

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MyFloatLabeledEditText);

        final int padding = a.getDimensionPixelSize(R.styleable.MyFloatLabeledEditText_fletPadding, 0);
        final int defaultPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PADDING_LEFT, getResources().getDisplayMetrics());
        final int paddingLeft = a.getDimensionPixelSize(R.styleable.MyFloatLabeledEditText_fletPaddingLeft, defaultPadding);
        final int paddingTop = a.getDimensionPixelSize(R.styleable.MyFloatLabeledEditText_fletPaddingTop, 0);
        final int paddingRight = a.getDimensionPixelSize(R.styleable.MyFloatLabeledEditText_fletPaddingRight, 0);
        final int paddingBottom = a.getDimensionPixelSize(R.styleable.MyFloatLabeledEditText_fletPaddingBottom, 0);

        final float textSize = a.getDimension(R.styleable.MyFloatLabeledEditText_fletTextSize, 14);


        int primaryDarkColor = getResources().getColor(R.color.primary_dark);
        mHintTextView.setTextColor(a.getColor(R.styleable.MyFloatLabeledEditText_fletTextColor, primaryDarkColor));

        Drawable background = a.getDrawable(R.styleable.MyFloatLabeledEditText_fletBackground);



        if (padding != 0) {
            mHintTextView.setPadding(padding, padding, padding, padding);
        } else {
            mHintTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }

        if (background != null) {
            setHintBackground(background);
        }


        mHintTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        mHintTextView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iransans));

        //Start hidden
        mHintTextView.setVisibility(INVISIBLE);
        AnimatorProxy.wrap(mHintTextView).setAlpha(0);



        //mHintTextView.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

        addView(mHintTextView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        a.recycle();
    }

    @SuppressLint("NewApi")
    private void setHintBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mHintTextView.setBackground(background);
        } else {
            mHintTextView.setBackgroundDrawable(background);
        }
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            if (mEditText != null) {
                throw new IllegalArgumentException("Can only have one Edittext subview");
            }

            final LayoutParams lp = new LayoutParams(params);
            lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            lp.topMargin = (int) (mHintTextView.getTextSize() + mHintTextView.getPaddingBottom() + mHintTextView.getPaddingTop());
            params = lp;

            setEditText((EditText) child);
        }

        super.addView(child, index, params);
    }

    private void setEditText(EditText editText) {
        mEditText = editText;

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                setShowHint(!TextUtils.isEmpty(s));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean gotFocus) {
                onFocusChanged(gotFocus);
            }
        });

        mHintTextView.setText(mEditText.getHint());

        if (!TextUtils.isEmpty(mEditText.getText())) {
            mHintTextView.setVisibility(VISIBLE);
        }
    }

    private void onFocusChanged(boolean gotFocus) {
        if (gotFocus && mHintTextView.getVisibility() == VISIBLE) {
            ObjectAnimator.ofFloat(mHintTextView, "alpha", 0.33f, 1f).start();
        } else if (mHintTextView.getVisibility() == VISIBLE) {
            AnimatorProxy.wrap(mHintTextView).setAlpha(1f);  //Need this for compat reasons
            ObjectAnimator.ofFloat(mHintTextView, "alpha", 1f, 0.33f).start();
        }
    }

    private void setShowHint(final boolean show) {
        AnimatorSet animation = null;
        if ((mHintTextView.getVisibility() == VISIBLE) && !show) {
            animation = new AnimatorSet();
            ObjectAnimator move = ObjectAnimator.ofFloat(mHintTextView, "translationY", 0, mHintTextView.getHeight() / 8);
            ObjectAnimator fade = ObjectAnimator.ofFloat(mHintTextView, "alpha", 1, 0);
            animation.playTogether(move, fade);
        } else if ((mHintTextView.getVisibility() != VISIBLE) && show) {
            animation = new AnimatorSet();
            ObjectAnimator move = ObjectAnimator.ofFloat(mHintTextView, "translationY", mHintTextView.getHeight() / 8, 0);
            ObjectAnimator fade;
            if (mEditText.isFocused()) {
                fade = ObjectAnimator.ofFloat(mHintTextView, "alpha", 0, 1);
            } else {
                fade = ObjectAnimator.ofFloat(mHintTextView, "alpha", 0, 0.33f);
            }
            animation.playTogether(move, fade);
        }

        if (animation != null) {
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mHintTextView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mHintTextView.setVisibility(show ? VISIBLE : INVISIBLE);
                    AnimatorProxy.wrap(mHintTextView).setAlpha(show ? 1 : 0);
                }
            });
            animation.start();
        }
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
        mHintTextView.setText(hint);
    }

    public CharSequence getHint() {
        return mHintTextView.getHint();
    }

}
