package com.example.frameworkfeaturetest.view.textinputlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class TextInputLayout extends LinearLayout {
    private int mAnimationDuration;
    private CollapsingTextHelper mCollapsingTextHelper;
    private int mDefaultCusorDrawableRes;
    private int mErrorPaddingTop;
    private int mLabelTextHeight;
    private boolean mLabelEnable;

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        mAnimationDuration = 300;
        mCollapsingTextHelper = new CollapsingTextHelper(this);
        mErrorPaddingTop = 6;
        mLabelTextHeight = 0;
        mLabelEnable = true;
        mDefaultCusorDrawableRes = -1;
        setOrientation(VERTICAL);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);
        mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        mCollapsingTextHelper.setPositionInterpolator(new AccelerateInterpolator());
        mCollapsingTextHelper.setCollapsedTextGravity(Gravity.START | Gravity.TOP);
    }
}
