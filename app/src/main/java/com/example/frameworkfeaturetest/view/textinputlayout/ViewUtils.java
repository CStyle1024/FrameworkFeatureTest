package com.example.frameworkfeaturetest.view.textinputlayout;

import android.os.Build;
import android.view.View;

public class ViewUtils {
    static final ValueAnimatorCompat.Creator DEFAULT_ANIMATOR_CREATE = new ValueAnimatorCompat.Creator() {
        @Override
        public ValueAnimatorCompat createAnimator() {
            return new ValueAnimatorCompat(new ValueAnimatorCompatImplHoneycombMr1());
        }
    };
    private static final ViewUtilsImpl IMPL;

    private interface ViewUtilsImpl {
        void setBoundsViewOutlineProvider(View view);
    }

    ViewUtils() {

    }

    static {
        int i = Build.VERSION.SDK_INT;
        IMPL = new ViewUtilsImpLollipop();
    }

    private static class ViewUtilsImpLollipop implements  ViewUtilsImpl {
        private ViewUtilsImpLollipop() {

        }

        @Override
        public void setBoundsViewOutlineProvider(View view) {
            ViewUtilsLollipop.setBoundsViewOutlineProvider(view);
        }
    }

    static void setBoundsViewOutlineProvider(View view) {
        IMPL.setBoundsViewOutlineProvider(view);
    }

    public static ValueAnimatorCompat createAnimator() {
        return DEFAULT_ANIMATOR_CREATE.createAnimator();
    }
}
