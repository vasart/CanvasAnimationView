package com.wagado.widget;

import android.graphics.Canvas;
import android.view.View;

public abstract class CanvasAnimation {
   public static final int START_ON_FIRST_FRAME = -1;
   public static final int ABSOLUTE = 0;
   public static final int RELATIVE_TO_SELF = 1;
   public static final int RELATIVE_TO_PARENT = 2;

    private final long mDuration;
    private final boolean isFillAfter;

    public boolean isStarted;
    public boolean isExpired;
    public boolean isEnded;

    private long mStartTime;
    private AnimationListener mListener;

    public CanvasAnimation(long duration, boolean fillAfter) {
        mDuration = duration;
        isFillAfter = fillAfter;
    }

    public void reset() {
        isStarted = false;
        isExpired = false;
        isEnded = false;

        mStartTime = START_ON_FIRST_FRAME;
    }

    public void animate(View view, Canvas canvas) {
        if (mStartTime == START_ON_FIRST_FRAME) {
            mStartTime = System.currentTimeMillis();
        }

        final long currentTime = System.currentTimeMillis();
        final float normalizedTime;
        if (getDuration() != 0) {
            normalizedTime = ((float) (currentTime - mStartTime)) / (float) getDuration();
        } else {
            // time is a step-change with a zero duration
            normalizedTime = currentTime < mStartTime ? 0.0f : 1.0f;
        }

        isExpired = normalizedTime >= 1.0f;

        if (normalizedTime >= 0.0f && normalizedTime <= 1.0f) {
            if (!isStarted) {
                fireAnimationStart();
                isStarted = true;
            }

            handle(canvas, normalizedTime);
        }

        if (isExpired) {
            if (isEnded) {
                fireAnimationEnd();
                isEnded = true;
            }

            if (isFillAfter()) {
                handle(canvas, normalizedTime);
            }
        } else {
            view.invalidate();
        }
    }

    public void setListener(AnimationListener listener) {
        mListener = listener;
    }

    public AnimationListener getListener() {
        return mListener;
    }

    public long getDuration() {
        return mDuration;
    }

    public boolean isFillAfter() {
        return isFillAfter;
    }

    protected abstract void handle(Canvas canvas, float normalizedTime);

    protected void fireAnimationStart() {
        if (getListener() != null) {
            getListener().onAnimationStart(this);
        }
    }

    protected void fireAnimationEnd() {
        if (getListener() != null) {
            getListener().onAnimationEnd(this);
        }
    }

    public static interface AnimationListener {
        /**
         * <p>Notifies the start of the animation.</p>
         *
         * @param animation The started animation.
         */
        void onAnimationStart(CanvasAnimation animation);

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which reached its end.
         */
        void onAnimationEnd(CanvasAnimation animation);
    }
}