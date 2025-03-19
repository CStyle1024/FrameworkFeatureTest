package com.example.frameworkfeaturetest.view.textinputlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

public class ValueAnimatorCompatImplHoneycombMr1 extends ValueAnimatorCompat.Impl {
    final ValueAnimator mValueAnimator = new ValueAnimator();

    @Override
    void start() {
        mValueAnimator.start();
    }
    @Override
    void cancel() {
        mValueAnimator.cancel();
    }

    @Override
    void end() {
        mValueAnimator.end();
    }

    @Override
    float getAnimatedFloatValue() {
        return ((Float) mValueAnimator.getAnimatedValue()).floatValue();
    }

    @Override
    float getAnimatedFraction() {
        return mValueAnimator.getAnimatedFraction();
    }

    @Override
    int getAnimatedIntValue() {
        return ((Integer) mValueAnimator.getAnimatedValue()).intValue();
    }

    @Override
    boolean isRunning() {
        return mValueAnimator.isRunning();
    }

    @Override
    void setDuration(int duration) {
        mValueAnimator.setDuration(duration);
    }

    @Override
    void setFloatValues(float from, float to) {
        mValueAnimator.setFloatValues(from, to);
    }

    @Override
    void setIntValues(int from, int to) {
        mValueAnimator.setIntValues(from, to);
    }

    @Override
    void setInterpolator(Interpolator interpolator) {
        mValueAnimator.setInterpolator(interpolator);
    }

    @Override
    void setListener(AnimatorListenerProxy listener) {
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                listener.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {
                listener.onAnimationCancel();
            }
        });
    }

    @Override
    void setUpdateListener(AnimatorUpdateListenerProxy updateListenerProxy) {
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                updateListenerProxy.onAnimationUpdate();
            }
        });
    }
}
