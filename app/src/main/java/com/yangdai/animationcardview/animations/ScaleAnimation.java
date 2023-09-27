package com.yangdai.animationcardview.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class ScaleAnimation {
    private final View mView;
    private final int mWidth;
    private final int mHeight;
    private final int mStartWidth;
    private final int mStartHeight;
    private int duration = 1000; // default duration
    private TimeInterpolator interpolator = new LinearInterpolator();
    private Animator mAnimator;

    public ScaleAnimation(View view, int width, int height) {
        mView = view;
        mWidth = width;
        mHeight = height;
        mStartWidth = view.getWidth();
        mStartHeight = view.getHeight();
    }

    public void start() {
        if (mView == null) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator widthAnimator = ValueAnimator.ofInt(mStartWidth, mWidth);
        widthAnimator.addUpdateListener(animation -> {
            mView.getLayoutParams().width = (Integer) animation.getAnimatedValue();
            mView.requestLayout();
        });

        ValueAnimator heightAnimator = ValueAnimator.ofInt(mStartHeight, mHeight);
        heightAnimator.addUpdateListener(animation -> {
            mView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            mView.requestLayout();
        });

        animatorSet.playTogether(widthAnimator, heightAnimator);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(interpolator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }
        });
        animatorSet.start();

        mAnimator = animatorSet;
    }

    public void pause() {
        if (mAnimator != null) {
            mAnimator.pause();
        }
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public void setInterpolator(TimeInterpolator interpolator){
        this.interpolator = interpolator;
    }

    public void resume() {
        if (mAnimator != null) {
            mAnimator.resume();
        }
    }
}