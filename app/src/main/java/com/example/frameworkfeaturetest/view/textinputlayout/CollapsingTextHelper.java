package com.example.frameworkfeaturetest.view.textinputlayout;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.MathUtils;
import android.view.animation.Interpolator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public final class CollapsingTextHelper {
    private static final boolean DEBUG_DRAW = false;
    private boolean mBoundsChanged;
    private float mCollapsedDrawX;
    private float mCollapsedDrawY;
    private int mCollapsedTextColor;
    private int mExpandedTextColor;
    private final Rect mCollapsedBounds;
    private final RectF mCurrentBounds;
    private float mCurrentDrawX;
    private float mCurrentDrawY;
    private float mCurrentTextSize;
    private boolean mDrawTitle;
    private final Rect mExpandedBounds;
    private float mExpandedDrawX;
    private float mExpandedDrawY;
    private float mExpandedFraction;
    private Bitmap mExpandedTitleTexture;
    private boolean mIsRtl;
    private Interpolator mPositionInterpolator;
    private float mScale;
    private CharSequence mText;
    private final TextPaint mTextPaint;
    private Interpolator mTextSizeInterpolator;
    private CharSequence mTextToDraw;
    private float mTextureAscent;
    private float mTextureDescent;
    private Paint mTexturePaint;
    private boolean mUseTexture;
    private final View mView;
    private static final boolean USE_SCALING_TEXTURE = false;
    private static final Paint DEBUG_DRAW_PAINT = null;
    private int mExpandedTextGravity = 16;
    private int mCollapsedTextGravity = 16;
    private float mExpandedTextSize = 15.0f;
    private float mCollapsedTextSize = 15.0f;

    public CollapsingTextHelper(View view) {
        mView = view;
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mCollapsedBounds = new Rect();
        mExpandedBounds = new Rect();
        mCurrentBounds = new RectF();
    }

    public void setTextSizeInterpolator(Interpolator interpolator) {
        mTextSizeInterpolator = interpolator;
        recalculate();
    }

    public void setPositionInterpolator(Interpolator interpolator) {
        mPositionInterpolator = interpolator;
        recalculate();
    }

    public void setExpandedTextSize(float textSize) {
        if (Float.compare(mExpandedTextSize, textSize) != 0) {
            mExpandedTextSize = textSize;
            recalculate();
        }
    }

    void setCollapsedTextSize(float textSize) {
        if (Float.compare(this.mCollapsedTextSize, textSize) != 0) {
            mCollapsedTextSize = textSize;
            recalculate();
        }
    }

    public void setCollapsedTextColor(int textColor) {
        if (mCollapsedTextColor != textColor) {
            mCollapsedTextColor = textColor;
            recalculate();
        }
    }

    public void setExpandedTextColor(int textColor) {
        if (mExpandedTextColor != textColor) {
            mExpandedTextColor = textColor;
            recalculate();
        }
    }

    public void setExpandedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(mExpandedBounds, left, top, right, bottom)) {
            mExpandedBounds.set(left, top, right, bottom);
            mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    public void setCollapsedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(mCollapsedBounds, left, top, right, bottom)) {
            mCollapsedBounds.set(left, top, right, bottom);
            mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    private void onBoundsChanged() {
        mDrawTitle = mCollapsedBounds.width() > 0
                && mCollapsedBounds.height() > 0
                && mExpandedBounds.width() > 0
                && mExpandedBounds.height() > 0;
    }

    public void setExpandedTextGravity(int gravity) {
        if (mExpandedTextGravity != gravity) {
            mExpandedTextGravity = gravity;
            recalculate();
        }
    }

    int getExpandedTextGravity() {
        return mExpandedTextGravity;
    }

    public void setCollapsedTextGravity(int gravity) {
        if (mCollapsedTextGravity != gravity) {
            mCollapsedTextGravity = gravity;
            recalculate();
        }
    }

    int getCollapsedTextGravity() {
        return mCollapsedTextGravity;
    }

    public void setCollapsedTextAppearance(int resId) {
        TypedArray a = mView.getContext().obtainStyledAttributes(resId, R.styleable.TextAppearance);
        if (a.hasValue(R.styleable.TextAppearance_android_textColor)) {
            int mzThemeColor = ResourceUtils.getMzThemeColor(mView.getContext());
            mCollapsedTextColor = mzThemeColor;
            if (mzThemeColor == 0) {
                mCollapsedTextColor = 0xFF0000FF;
            }
        }
        if (a.hasValue(R.styleable.TextAppearance_android_textSize)) {
            mCollapsedTextSize = a.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int) mCollapsedTextSize);
        }
        a.recycle();
        recalculate();
    }

    void setExpandedTextAppearance(int resId) {
        TypedArray a = mView.getContext().obtainStyledAttributes(resId, R.styleable.TextAppearance);
        a.hasValue(R.styleable.TextAppearance_android_textColor);
        if (a.hasValue(R.styleable.TextAppearance_android_textSize)) {
            mExpandedTextSize = a.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int) mExpandedTextSize);
        }
        a.recycle();
        recalculate();
    }

    public void setTypeface(Typeface typeface) {
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        if (mTextPaint.getTypeface() != typeface) {
            mTextPaint.setTypeface(typeface);
            recalculate();
        }
    }

    public Typeface getTypeface() {
        return mTextPaint.getTypeface();
    }

    public void setExpandedFraction(float fraction) {
        float fraction2 = MathUtils.constrain(fraction, 0.0f, 1.0f);
        if (Float.compare(fraction2, mExpandedFraction) != 0) {
            mExpandedFraction = fraction2;
            calculateCurrentOffsets();
        }
    }

    public float getExpansionFraction() {
        return mExpandedFraction;
    }

    public float getCollapsedTextSize() {
        return mCollapsedTextSize;
    }

    float getExpandedTextSize() {
        return mExpandedTextSize;
    }

    private void calculateCurrentOffsets() {
        interpolateBounds(mExpandedFraction);
        mCurrentDrawX = lerp(mExpandedDrawX, mCollapsedDrawX, mExpandedFraction, mPositionInterpolator);
        mCurrentDrawY = lerp(mExpandedDrawY, mCollapsedDrawY, mExpandedFraction, mPositionInterpolator);
        setInterpolatedTextSize(lerp(mExpandedTextSize, mCollapsedTextSize, mExpandedFraction, mPositionInterpolator));
        if (mCollapsedTextColor != mExpandedTextColor) {
            mTextPaint.setColor(blendColors(mExpandedTextColor, mCollapsedTextColor, mExpandedFraction));
        } else {
            mTextPaint.setColor(mCollapsedTextColor);
        }
    }

    private void calculateBaseOffsets() {
        mTextPaint.setTextSize(mCollapsedTextSize);
        float width = mTextToDraw != null ? mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()) : 0.0f;
        int collapsedAbsGravity = GravityCompat.getAbsoluteGravity(mCollapsedTextGravity, mIsRtl ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        switch (collapsedAbsGravity & Gravity.FILL_VERTICAL) {
            case Gravity.TOP:
                mCollapsedDrawY = mCollapsedBounds.top - mTextPaint.ascent();
                break;
            case Gravity.BOTTOM:
                mCollapsedDrawY = mCollapsedBounds.bottom;
                break;
            default:
                float textHeight = mTextPaint.descent() - mTextPaint.ascent();
                float textOffset = textHeight / 2.0f  - mTextPaint.descent();
                mCollapsedDrawY = mCollapsedBounds.centerY() + textOffset;
        }
        switch (collapsedAbsGravity & Gravity.FILL_HORIZONTAL) {
            case Gravity.CENTER_HORIZONTAL:
                mCollapsedDrawX = mCollapsedBounds.centerX() - (width / 2.0f);
                break;
            case Gravity.RIGHT:
                mCollapsedDrawX = mCollapsedBounds.right - width;
                break;
            default:
                mCollapsedDrawX = mCollapsedBounds.left;
                break;
        }
        mTextPaint.setTextSize(mExpandedTextSize);
        float width2 = mTextToDraw != null ? mTextPaint.measureText(mTextToDraw, 0 , mTextToDraw.length()) : 0.0f;
        int expandedAbsGravity = GravityCompat.getAbsoluteGravity(mExpandedTextGravity, mIsRtl ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        switch (expandedAbsGravity & Gravity.FILL_VERTICAL) {
            case Gravity.TOP:
                mExpandedDrawY = mExpandedBounds.top - mTextPaint.ascent();
                break;
            case Gravity.BOTTOM:
                mExpandedDrawY = mExpandedBounds.bottom;
                break;
            default:
                float textHeight2 = mTextPaint.descent() - mTextPaint.ascent();
                float textOffset2 = (textHeight2 / 2.0f) - mTextPaint.descent();
                mExpandedDrawY = mExpandedBounds.centerY() + textOffset2;
        }
        switch (expandedAbsGravity & Gravity.FILL_HORIZONTAL) {
            case Gravity.CENTER_HORIZONTAL:
                mExpandedDrawX = mExpandedBounds.centerX() - (width2 / 2.0f);
                break;
            case Gravity.RIGHT:
                mExpandedDrawX = mExpandedBounds.right - width2;
                break;
            default:
                mExpandedDrawX = mExpandedBounds.left;
                break;
        }
        clearTexture();
    }

    private void interpolateBounds(float fraction) {
        mCurrentBounds.left = lerp(mExpandedBounds.left, mCollapsedBounds.left, fraction, mPositionInterpolator);
        mCurrentBounds.top = lerp(mExpandedDrawY, mCollapsedDrawY, fraction, mPositionInterpolator);
        mCurrentBounds.right = lerp(mExpandedBounds.right, mCollapsedBounds.right, fraction, mPositionInterpolator);
        mCurrentBounds.bottom = lerp(mExpandedBounds.bottom, mCollapsedBounds.bottom, fraction, mPositionInterpolator);
    }

    public void draw(Canvas canvas) {
        float ascent;
        float y;
        int saveCount = canvas.save();
        if (mTextToDraw != null && this.mDrawTitle) {
            boolean drawTexture = mUseTexture && mExpandedTitleTexture != null;
            mTextPaint.setTextSize(mCurrentTextSize);
            if (drawTexture) {
                ascent = mTextureAscent * mScale;
            } else {
                ascent = mTextPaint.ascent() * mScale;
            }
            if (!drawTexture) {
                y = mCurrentDrawY;
            } else {
                y = mCurrentDrawY + ascent;
            }
            if (Float.compare(mScale, 1.0f) != 0) {
                canvas.scale(mScale, mScale, mCurrentDrawX, y);
            }
            if (drawTexture) {
                canvas.drawBitmap(mExpandedTitleTexture, mCurrentDrawX, y, mTexturePaint);
            } else {
                canvas.drawText(mTextToDraw, 0, mTextToDraw.length(), mCurrentDrawX, y, mTextPaint);
            }
        }
        canvas.restoreToCount(saveCount);
    }

    private boolean calculateIsRtl(CharSequence text) {
        return ViewCompat.getLayoutDirection(mView) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private void setInterpolatedTextSize(float textSize) {
        float availableWidth;
        float newTextSize;
        if (mText == null) {
            return;
        }
        boolean updateDrawText = false;
        if (isClose(textSize, mCollapsedTextSize)) {
            availableWidth = mCollapsedBounds.width();
            newTextSize = mCollapsedTextSize;
            mScale = 1.0f;
        } else {
            availableWidth = mExpandedBounds.width();
            newTextSize = mExpandedTextSize;
            if (isClose(textSize, mExpandedTextSize)) {
                mScale = 1.0f;
            } else {
                mScale = textSize / this.mExpandedTextSize;
            }
        }
        if (availableWidth > 0.0f) {
            updateDrawText = Float.compare(mCurrentTextSize, newTextSize) != 0 && mBoundsChanged;
            mCurrentTextSize = newTextSize;
            mBoundsChanged = false;
        }
        if (mTextToDraw == null || updateDrawText) {
            mTextPaint.setTextSize(mCurrentTextSize);
            CharSequence title = TextUtils.ellipsize(mText, mTextPaint, availableWidth, TextUtils.TruncateAt.END);
            if (mTextToDraw == null || !mTextToDraw.equals(title)) {
                mTextToDraw = title;
            }
            mIsRtl = calculateIsRtl(mTextToDraw);
        }
        mUseTexture = (!USE_SCALING_TEXTURE || Float.compare(mScale, 1.0f) == 0) ? false : true;
        if (mUseTexture) {
            ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(mView);
    }

    private void ensureExpandedTexture() {
        if (mExpandedTitleTexture != null || mExpandedBounds.isEmpty() || TextUtils.isEmpty(mTextToDraw)) {
            return;
        }
        mTextPaint.setTextSize(mExpandedTextSize);
        mTextPaint.setColor(mExpandedTextColor);
        mTextureAscent = mTextPaint.ascent();
        mTextureDescent = mTextPaint.descent();
        int w = Math.round(mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()));
        int h = Math.round(mTextureDescent - mTextureAscent);
        if (w <= 0 && h <= 0) {
            return;
        }
        mExpandedTitleTexture = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(mExpandedTitleTexture);
        c.drawText(mTextToDraw, 0, mTextToDraw.length(), 0.0f, h - mTextPaint.descent(), mTextPaint);
        if (mTexturePaint == null) {
            mTexturePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        }
    }

    public void recalculate() {
        if (mView.getHeight() > 0 && mView.getWidth() > 0) {
            calculateBaseOffsets();
            calculateCurrentOffsets();
        }
    }

    public void setText(CharSequence text) {
        if (text == null || !text.equals(mText)) {
            mText = text;
            mTextToDraw = null;
            clearTexture();
            recalculate();
        }
    }

    public CharSequence getText() {
        return mText;
    }

    private void clearTexture() {
        if (mExpandedTitleTexture != null) {
            mExpandedTitleTexture.recycle();
            mExpandedTitleTexture = null;
        }
    }

    private boolean isClose(float value, float targetValue) {
        return Math.abs(value - targetValue) < 0.001f;
    }

    int getExpandedTextColor() {
        return mExpandedTextColor;
    }

    public int getCollapsedTextColor() {
        return mCollapsedTextColor;
    }

    private int blendColors(int color1, int color2, float ratio) {
        float inverseRatio = 1.0f - ratio;
        float a = (Color.alpha(color1) * inverseRatio + Color.alpha(color2) * inverseRatio);
        float r = (Color.red(color1) * inverseRatio + Color.red(color2) * inverseRatio);
        float g = (Color.green(color1) * inverseRatio + Color.green(color2) * inverseRatio);
        float b = (Color.blue(color1) * inverseRatio + Color.blue(color2) * inverseRatio);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    private static float lerp(float startValue, float endValue, float fraction, Interpolator interpolator) {
        if (interpolator != null) {
            fraction = interpolator.getInterpolation(fraction);
        }
        return AnimationUtils.lerp(startValue, endValue, fraction);
    }

    private boolean rectEquals(Rect rect, int left, int top, int right, int bottom) {
        return rect.left == left && rect.top == top && rect.right == right && rect.bottom == bottom;
    }
}
