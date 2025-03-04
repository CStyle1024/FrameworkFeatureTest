package com.example.frameworkfeaturetest.view.textinputlayout;

import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class AnimationUtils {
    static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
    static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    AnimationUtils() {

    }

    public static float lerp(float startValue, float endValue, float fraction) {
        return ((endValue - startValue) * fraction) + startValue;
    }

    public static int lerp(int startValue, int endValue, float fraction) {
        return Math.round(((endValue - startValue) * fraction) + startValue);
    }

    static class AnimatorListenerAdapter implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
