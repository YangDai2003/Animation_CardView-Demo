package com.yangdai.animationcardview.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AlphaAnimation {
    private final View mView;
    private final float mAlpha;
    private final float mStartAlpha;
    private int duration = 1000; // default duration
    private TimeInterpolator interpolator = new LinearInterpolator();
    private ObjectAnimator mAnimator;

    public AlphaAnimation(View view, float alpha) {
        mView = view;
        mAlpha = alpha;
        mStartAlpha = view.getAlpha();
    }

    public void start() {
        if (mView == null) {
            return;
        }
        mAnimator = ObjectAnimator.ofFloat(mView, "alpha", mStartAlpha, mAlpha);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(interpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }
        });
        mAnimator.start();
    }

    public void pause() {
        if (mAnimator != null) {
            mAnimator.pause();
        }
    }

    public void resume() {
        if (mAnimator != null) {
            mAnimator.resume();
        }
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public void setInterpolator(TimeInterpolator interpolator){
        this.interpolator = interpolator;
    }
}