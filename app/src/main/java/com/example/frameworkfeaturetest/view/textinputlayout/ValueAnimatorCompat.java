package com.example.frameworkfeaturetest.view.textinputlayout;

import android.view.animation.Interpolator;

class ValueAnimatorCompat {
    private final Impl mImpl;

    interface AnimatorListener {
        void onAnimatorCancel(ValueAnimatorCompat valueAnimatorCompat);

        void onAnimatorEnd(ValueAnimatorCompat valueAnimatorCompat);

        void onAnimatorStart(ValueAnimatorCompat valueAnimatorCompat);
    }

    interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat);
    }

    interface Creator {
        ValueAnimatorCompat createAnimator();
    }

    static abstract class Impl {
        interface AnimatorListenerProxy {
            void onAnimationCancel();

            void onAnimationEnd();

            void onAnimationStart();
        }

        interface AnimatorUpdateListenerProxy {
            void onAnimationUpdate();
        }

        abstract void cancel();
        abstract void end();
        abstract float getAnimatedFloatValue();
        abstract float getAnimatedFraction();
        abstract int getAnimatedIntValue();
        abstract boolean isRunning();
        abstract void setDuration(int duration);
        abstract void setFloatValues(float from, float to);
        abstract void setIntValues(int from, int to);
        abstract void setInterpolator(Interpolator interpolator);
        abstract void setListener(AnimatorListenerProxy animatorListenerProxy);
        abstract void setUpdateListener(AnimatorUpdateListenerProxy animatorUpdateListenerProxy);
        abstract void start();
    }

    static class AnimatorListenerAdapter implements AnimatorListener {

        @Override
        public void onAnimatorStart(ValueAnimatorCompat valueAnimatorCompat) {
        }

        @Override
        public void onAnimatorEnd(ValueAnimatorCompat valueAnimatorCompat) {
        }

        @Override
        public void onAnimatorCancel(ValueAnimatorCompat valueAnimatorCompat) {
        }
    }

    public ValueAnimatorCompat(Impl impl) {
        mImpl = impl;
    }

    public void start() {
        mImpl.start();
    }

    public boolean isRunning() {
        return mImpl.isRunning();
    }

    public void setInterpolator(Interpolator interpolator) {
        mImpl.setInterpolator(interpolator);
    }

    public void setUpdateListener(AnimatorUpdateListener updateListener) {
        if (updateListener != null) {
            mImpl.setUpdateListener(new Impl.AnimatorUpdateListenerProxy() {
                @Override
                public void onAnimationUpdate() {
                    updateListener.onAnimationUpdate(ValueAnimatorCompat.this);
                }
            });
        } else {
            mImpl.setUpdateListener(null);
        }
    }

    public void setListener(AnimatorListener listener) {
        if (listener != null) {
            mImpl.setListener(new Impl.AnimatorListenerProxy() {
                @Override
                public void onAnimationCancel() {
                    listener.onAnimatorCancel(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationEnd() {
                    listener.onAnimatorEnd(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationStart() {
                    listener.onAnimatorStart(ValueAnimatorCompat.this);
                }
            });
        } else {
            mImpl.setListener(null);
        }
    }

    public void setIntValues(int from, int to) {
        mImpl.setIntValues(from, to);
    }

    public int getAnimatedIntValue() {
        return mImpl.getAnimatedIntValue();
    }

    public void setFloatValues(float from, float to) {
        mImpl.setFloatValues(from, to);
    }

    public float getAnimatedFloatValues() {
        return mImpl.getAnimatedFloatValue();
    }

    public void setDuration(int duration) {
        mImpl.setDuration(duration);
    }

    public void cancel() {
        mImpl.cancel();
    }

    public float getAnimatedFraction() {
        return mImpl.getAnimatedFraction();
    }

    public void end() {
        mImpl.end();
    }
}
