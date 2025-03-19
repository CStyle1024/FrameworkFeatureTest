package com.example.frameworkfeaturetest.view.textinputlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.EditText;
import android.widget.Editor;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.example.frameworkfeaturetest.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class TextInputLayout extends LinearLayout {
    private static Field mCursorDrawableArray;
    private static Field mCursorDrawableRes;
    private static Field mTextViewEditor;
    private static Method mUpdateCursorPosition;
    private boolean mAlignEditContent;
    private int mAnimationDuration;
    private ValueAnimatorCompat mAnimator;
    private CollapsingTextHelper mCollapsingTextHelper;
    private int mDefaultCursorDrawableRes;
    private ColorStateList mDefaultTextColor;
    private EditText mEditText;

    /*错误信息*/
    private Drawable mErrorBackground;
    private boolean mErrorEnabled;
    private int mErrorPaddingTop;
    private int mErrorPaddingHorizontal;
    private int mErrorTextAppearance;
    private TextView mErrorView;

    private ColorStateList mFocusedTextColor;

    /*提示信息*/
    private CharSequence mHint;
    private boolean mHintAnimationEnabled;
    /*label*/
    private boolean mLabelEnable;
    private int mLabelPaddingHorizontal;
    private int mLabelTextHeight;
    private Drawable mOriginBackground;
    private Paint mTmpPaint;

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
        mDefaultCursorDrawableRes = -1;
        setOrientation(VERTICAL);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);
        mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        mCollapsingTextHelper.setPositionInterpolator(new AccelerateInterpolator());
        mCollapsingTextHelper.setCollapsedTextGravity(Gravity.START | Gravity.TOP);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MzTextInputLayout, defStyleAttr, /*R.style.MzTextInputLayoutTextAppearance*/0);
        mHint = a.getText(R.styleable.MzTextInputLayout_android_hint);
        mHintAnimationEnabled = a.getBoolean(R.styleable.MzTextInputLayout_hintAnimationEnabled, true);
        if (a.hasValue(R.styleable.MzTextInputLayout_android_textColorHint)) {
            mDefaultTextColor = mFocusedTextColor = a.getColorStateList(R.styleable.MzTextInputLayout_android_textColorHint);
        }
        int hintAppearance = a.getResourceId(R.styleable.MzTextInputLayout_hintTextAppearance, -1);
        if (hintAppearance != -1) {
            setHintTextAppearance(a.getResourceId(R.styleable.MzTextInputLayout_hintTextAppearance, 0));
        }
        mErrorTextAppearance = a.getResourceId(R.styleable.MzTextInputLayout_errorTextAppearance, 0);
        boolean errorEnabled = a.getBoolean(R.styleable.MzTextInputLayout_errorEnabled, false);
        mErrorBackground = a.getDrawable(R.styleable.MzTextInputLayout_errorBackground);
        mAlignEditContent = a.getBoolean(R.styleable.MzTextInputLayout_alignEditContent, true);
        mLabelPaddingHorizontal = a.getDimensionPixelSize(R.styleable.MzTextInputLayout_labelPaddingHorizontal, 0);
        mErrorPaddingHorizontal = a.getDimensionPixelSize(R.styleable.MzTextInputLayout_errorPaddingHorizontal, 0);
        a.recycle();
        mErrorPaddingTop = context.getResources().getDimensionPixelSize(R.dimen.mz_text_input_layout_default_margin_top);
        setErrorEnabled(errorEnabled); // 设置是否支持错误提示
        if (ViewCompat.getImportantForAccessibility(this) == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        ViewCompat.setAccessibilityDelegate(this, new TextInputAccessibilityDelegate());
    }

    private Interpolator getInterpolator() {
        return new PathInterpolator(0.1f, 0.0f, 0.1f, 1.0f);
    }

    private static class AnimInterpolator implements  Interpolator {
        private AnimInterpolator() {
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (1.0f - Math.pow(1.0f - input, 2.0d));
        }
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            setEditText((EditText) child);
            super.addView(child, 0, updateEditTextMargin(params));
            return;
        }
        super.addView(child, index, params);
    }

    public void setTypeface(Typeface typeface) {
        mCollapsingTextHelper.setTypeface(typeface);
    }

    public void setEditText(EditText editText) {
        if (mEditText != null) {
            throw new IllegalStateException("We already have an EditText, can only have one");
        }
        mEditText = editText;
        if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL) {
            mEditText.setTextDirection(View.TEXT_DIRECTION_RTL);
        }
        mCollapsingTextHelper.setTypeface(mEditText.getTypeface());
        mCollapsingTextHelper.setExpandedTextSize(mEditText.getTextSize());
        mCollapsingTextHelper.setExpandedTextGravity(mEditText.getGravity());
        // 向编辑框添加文本监听器
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditText != null && mEditText.getText().length() == 0) {
                    // 编辑框无文本，去掉错误提示
                    setErrorEnabled(false);
                    // 编辑框无文本，清除编辑框背景着色
                    mEditText.setBackgroundTintList(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文本改变后，更新提示信息显示
                updateLabelVisibility(true);
            }
        });
        if (mDefaultTextColor == null) {
            mDefaultTextColor = mEditText.getHintTextColors();
        }
        if (TextUtils.isEmpty(mHint)) {
            // 获取并设置内部提示信息
            setHint(mEditText.getHint());
            mEditText.setHint((CharSequence) null);
        }
        // 设置错误信息视图布局参数
        if (mErrorView != null) {
            if (mAlignEditContent) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mEditText.getLayoutParams();
                ViewCompat.setPaddingRelative(mErrorView, ViewCompat.getPaddingStart(mEditText) + mErrorPaddingHorizontal,
                        mErrorPaddingTop, ViewCompat.getPaddingEnd(mEditText) + mErrorPaddingHorizontal, 0);
                LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) mErrorView.getLayoutParams();
                llp.leftMargin = lp.leftMargin;
                llp.rightMargin = lp.rightMargin;
                mErrorView.setLayoutParams(llp);
            } else {
                ViewCompat.setPaddingRelative(mErrorView, mErrorPaddingHorizontal, mErrorPaddingTop, mErrorPaddingHorizontal, 0);
            }
        }
        // 更新提示信息显示
        updateLabelVisibility(false);
    }

    private LinearLayout.LayoutParams updateEditTextMargin(ViewGroup.LayoutParams lp) {
        LinearLayout.LayoutParams llp = lp instanceof LinearLayout.LayoutParams ?
                (LinearLayout.LayoutParams) lp : new LinearLayout.LayoutParams(lp);
        if (mTmpPaint == null) {
            mTmpPaint = new Paint();
        }
        mTmpPaint.setTypeface(mCollapsingTextHelper.getTypeface());
        mTmpPaint.setTextSize(mCollapsingTextHelper.getCollapsedTextSize());
        llp.topMargin = 0;
        mLabelTextHeight = llp.topMargin;
        return llp;
    }

    private void updateLabelVisibility(boolean animate) {
        int defaultColor;
        boolean hasText = (mEditText == null || TextUtils.isEmpty(mEditText.getText())) ? false : true;
        boolean isFocused = arrayContains(getDrawableState(), android.R.attr.state_focused);
        if (mDefaultTextColor != null && mFocusedTextColor != null) {
            mCollapsingTextHelper.setExpandedTextColor(mDefaultTextColor.getDefaultColor());
            if (isFocused) {
                defaultColor = mFocusedTextColor.getDefaultColor(); // 获取有焦点文本的色值
            } else {
                defaultColor = mDefaultTextColor.getDefaultColor(); // 获取无焦点（默认）文本的色值
            }
            mCollapsingTextHelper.setCollapsedTextColor(defaultColor); // 设置收起文本的色值
            if (hasText || isFocused) {
                collapseHint(animate); // 有文本或有焦点，收起提示文本
            } else {
                expandHint(animate); // 无文本且没有焦点，扩展提示文本
            }
        }
    }

    public void setErrorBackground(Drawable drawable) {
        mErrorBackground = drawable;
        if (isErrorShow() && mEditText != null) {
            mEditText.setBackground(mErrorBackground);
        }
    }

    public void setErrorBackgroundResource(int resId) {
        if (resId == 0) {
            return;
        }
        mErrorBackground = ContextCompat.getDrawable(getContext(), resId);
        if (isErrorShow() && mEditText != null) {
            mEditText.setBackground(mErrorBackground);
        }
    }

    public void setOriginBackground(Drawable drawable) {
        mOriginBackground = drawable;
        if (!isErrorShow() && mEditText != null) {
            mEditText.setBackground(mOriginBackground);
        }
    }

    public void SetOriginBackgroundResource(int resId) {
        if (resId == 0) {
            return;
        }
        mOriginBackground = ContextCompat.getDrawable(getContext(), resId);
        if (!isErrorShow() && mEditText != null) {
            mEditText.setBackground(mOriginBackground);
        }
    }

    public void setAlignEditContent(boolean align) {
        mAlignEditContent = align;
    }

    public void setLabelPaddingHorizontal(int padding) {
        mLabelPaddingHorizontal = padding;
    }

    public EditText getEditText() {
        return mEditText;
    }

    private void setHint(CharSequence hint) {
        mHint = hint;
        mCollapsingTextHelper.setText(hint);
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
    }

    public CharSequence getHint() {
        return mHint;
    }

    private void setHintTextAppearance(int resId) {
        mCollapsingTextHelper.setCollapsedTextAppearance(resId);
        mFocusedTextColor = ColorStateList.valueOf(mCollapsingTextHelper.getCollapsedTextColor());
        if (mEditText != null) {
            updateLabelVisibility(false);
            LinearLayout.LayoutParams lp = updateEditTextMargin(mEditText.getLayoutParams());
            mEditText.setLayoutParams(lp);
            mEditText.requestLayout();
        }
    }

    public void setErrorEnabled(boolean enabled) {
        if (mErrorEnabled != enabled) {
            // 如果正在执行动画先暂停动画
            if (mErrorView != null) {
                ViewCompat.animate(mErrorView).cancel();
            }
            if (enabled) {
                mErrorView = new TextView(getContext());
                mErrorView.setTextAppearance(getContext(), mErrorTextAppearance);
                // 先设置错误信息视图不可见
                mErrorView.setVisibility(View.INVISIBLE);
                if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL) {
                    mErrorView.setGravity(GravityCompat.END);
                }
                addView(mErrorView);
                if (mEditText != null) {
                    if (mAlignEditContent) {
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mEditText.getLayoutParams();
                        ViewCompat.setPaddingRelative(mErrorView, ViewCompat.getPaddingStart(mEditText) + mErrorPaddingHorizontal,
                                mErrorPaddingTop, ViewCompat.getPaddingEnd(mEditText) + mErrorPaddingHorizontal, 0);
                        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) mErrorView.getLayoutParams();
                        llp.leftMargin = lp.leftMargin;
                        llp.rightMargin = lp.rightMargin;
                        mErrorView.setLayoutParams(llp);
                    } else {
                        ViewCompat.setPaddingRelative(mErrorView, mErrorPaddingHorizontal, mErrorPaddingTop, mErrorPaddingHorizontal, 0);
                    }
                }
            } else {
                // 还原编辑框背景
                if (mOriginBackground != null) {
                    mEditText.setBackground(mOriginBackground);
                } else {
                    ViewCompat.setBackgroundTintList(mEditText, null);
                }
                setCursorDrawable(mEditText, mDefaultCursorDrawableRes); // 设置游标为默认样式
                removeView(mErrorView); // 移除错误信息视图
                mErrorView = null;
            }
            mErrorEnabled = enabled;
        }
    }

    public boolean isErrorEnabled() {
        return mErrorEnabled;
    }

    public boolean isErrorShow() {
        return mErrorView != null && mErrorView.getVisibility() == View.VISIBLE;
    }

    public void setError(CharSequence error) {
        boolean needChangeBg = false;
        if (!mErrorEnabled) {
            if (TextUtils.isEmpty(error)) {
                return;
            }
            setErrorEnabled(true);
            needChangeBg = true;
        }
        if (!TextUtils.isEmpty(error)) {
            // 错误信息显示动画
            ViewCompat.setAlpha(mErrorView, 0.0f);
            ViewCompat.animate(mErrorView).alpha(1.0f)
                    .setDuration(mAnimationDuration)
                    .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(@NonNull View view) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }).start();
            if (needChangeBg) {
                if (mErrorBackground != null) {
                    // 保存编辑框原有的背景，设置错误信息背景
                    mOriginBackground = mEditText.getBackground();
                    mEditText.setBackground(mErrorBackground);
                } else {
                    int mFocusedColor = mErrorView.getCurrentTextColor();
                    int mNormalColor = getContext().getResources().getColor(R.color.mz_text_input_normal_color);
                    int[] colors = {mFocusedColor, mNormalColor};
                    int[][] states = {new int[]{16842910, 16842908}, new int[0]};
                    ColorStateList colorList = new ColorStateList(states, colors);
                    ViewCompat.setBackgroundTintList(mEditText, colorList);
                    setCursorDrawable(mEditText, R.drawable.mz_text_cursor_error_color);
                }
            }
        } else if (mErrorView.getVisibility() == View.VISIBLE) {
            // 隐藏错误信息
            ViewCompat.animate(mErrorView).alpha(0.0f)
                    .setDuration(mAnimationDuration)
                    .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(@NonNull View view) {
                            view.setVisibility(View.INVISIBLE);
                        }
                    }).start();
            if (mOriginBackground != null) {
                mEditText.setBackground(mOriginBackground);
            } else {
                ViewCompat.setBackgroundTintList(mErrorView, null);
            }
            setCursorDrawable(mEditText, mDefaultCursorDrawableRes);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
    }

    private void setCursorDrawable(EditText editText, int res) {
        if (res < 0) {
            return;
        }
        try {
            if (mCursorDrawableRes == null) {
                mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
                mDefaultCursorDrawableRes = mCursorDrawableRes.getInt(editText);
            }
            mCursorDrawableRes.setInt(editText, 0);
            if (mTextViewEditor == null) {
                mTextViewEditor = TextView.class.getDeclaredField("mTextViewEditor");
                mTextViewEditor.setAccessible(true);
            }
            Object mEditor = mTextViewEditor.get(editText);
            Class<?> clz = Class.forName("android.widget.Editor");
            if (Build.VERSION.SDK_INT < 28) {
                if (mCursorDrawableArray == null) {
                    mCursorDrawableArray = clz.getDeclaredField("mCursorDrawableArray");
                    mCursorDrawableArray.setAccessible(true);
                }
                Drawable[] mCursorDrawableArrayTemp = {null, null};
                mCursorDrawableArray.set(mEditor, mCursorDrawableArrayTemp);
            }
            if (mUpdateCursorPosition == null) {
                if (Build.VERSION.SDK_INT < 28) {
                    mUpdateCursorPosition = clz.getDeclaredMethod("updateCursorPositions", new Class[0]);
                } else {
                    mUpdateCursorPosition = clz.getDeclaredMethod("updateCursorPosition", new Class[0]);
                }
                mUpdateCursorPosition.setAccessible(true);
            }
            mUpdateCursorPosition.invoke(mEditor, new Object[0]);
            mCursorDrawableRes.setInt(editText, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void collapseHint(boolean animate) {
        ValueAnimatorCompat valueAnimatorCompat = mAnimator;
        if (valueAnimatorCompat != null && valueAnimatorCompat.isRunning()) {
            mAnimator.cancel();
        }
        if (!animate || !mHintAnimationEnabled) {
            mCollapsingTextHelper.setExpansionFraction(1.0f);
        } else {
            animateToExpansionFraction(1.0f);
        }
    }

    private void expandHint(boolean animate) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (!animate || !mHintAnimationEnabled) {
            mCollapsingTextHelper.setExpansionFraction(0.0f);
        } else {
            animateToExpansionFraction(0.0f);
        }
    }

    private void animateToExpansionFraction(float target) {
        if (mCollapsingTextHelper.getExpansionFraction() == target) {
            return;
        }
        if (mAnimator == null) {
            mAnimator = ViewUtils.createAnimator();
            mAnimator.setInterpolator(getInterpolator());
            mAnimator.setDuration(mAnimationDuration);
            mAnimator.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimatorCompat animator) {
                    mCollapsingTextHelper.setExpansionFraction(animator.getAnimatedFraction());
                }
            });
            mAnimator.setFloatValues(mCollapsingTextHelper.getExpansionFraction(), target);
            mAnimator.start();
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int v : array) {
            if (v == value) {
               return true;
            }
        }
        return false;
    }

    private class TextInputAccessibilityDelegate extends AccessibilityDelegateCompat {
        private TextInputAccessibilityDelegate() {
        }

        @Override
        public void onInitializeAccessibilityEvent(@NonNull View host, @NonNull AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(TextInputLayout.class.getSimpleName());
        }

        @Override
        public void onPopulateAccessibilityEvent(@NonNull View host, @NonNull AccessibilityEvent event) {
            super.onPopulateAccessibilityEvent(host, event);
            CharSequence text = mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                event.getText().add(text);
            }
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(@NonNull View host, @NonNull AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(TextInputLayout.class.getSimpleName());
            CharSequence text = mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                info.setText(text);
            }
            if (TextInputLayout.this.mEditText != null) {
                info.setLabelFor(mEditText);
            }
            CharSequence error = mErrorView != null ? mErrorView.getText() : null;
            if (!TextUtils.isEmpty(error)) {
                info.setContentInvalid(true);
                info.setError(error);
            }
        }
    }

}
