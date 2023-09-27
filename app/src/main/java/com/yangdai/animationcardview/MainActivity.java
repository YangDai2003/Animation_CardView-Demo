package com.yangdai.animationcardview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.view.animation.PathInterpolator;

import com.yangdai.animationcardview.animations.AlphaAnimation;
import com.yangdai.animationcardview.animations.ScaleAnimation;
import com.yangdai.animationcardview.utils.DpConvert;

/**
 * @author 30415
 */
public class MainActivity extends AppCompatActivity {
    float oriX = 0;
    float oriY = 0;
    float oriRawX = 0;
    float oriRawY = 0;
    float realOriX = 0;
    float realOriY = 0;
    int pointerId = 0;
    boolean cardOpen = false;
    Context context;
    ScaleAnimation scaleAnimation;
    AlphaAnimation alphaAnimation;
    ScaleAnimation scaleAnimation2;
    AlphaAnimation alphaAnimation2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        View card = findViewById(R.id.card);

        realOriX = card.getX();
        realOriY = card.getY();
        SpringAnimation sa = new SpringAnimation(card, DynamicAnimation.TRANSLATION_Y);
        SpringForce sf = new SpringForce();
        sf.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        sf.setStiffness(150f);
        sa.setSpring(sf);
        SpringAnimation sa2 = new SpringAnimation(card, DynamicAnimation.TRANSLATION_X);
        SpringForce sf2 = new SpringForce();
        sf2.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        sf2.setStiffness(150f);
        sa2.setSpring(sf2);

        View root = findViewById(R.id.root);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        int screenWidth, screenHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            screenWidth = windowMetrics.getBounds().width() - insets.left - insets.right;
            screenHeight = windowMetrics.getBounds().height() - insets.top - insets.bottom;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;
        }

        card.setOnTouchListener((v, event) -> {
            if (cardOpen) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pointerId = event.getPointerId(0);
                        sa.cancel();
                        sa2.cancel();
                        scaleAnimation.pause();
                        alphaAnimation.pause();
                        oriX = event.getX();
                        oriY = event.getY();
                        oriRawX = event.getRawX();
                        oriRawY = event.getRawY();
                        sf.setFinalPosition(realOriY);
                        sf2.setFinalPosition(realOriX);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (pointerId == event.getPointerId(0)) {
                            card.setX((event.getRawX() - oriX));
                            card.setY((event.getRawY() - oriY));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        sa.start();
                        sa2.start();
                        scaleAnimation2 = new ScaleAnimation(card, (int) DpConvert.toPx(context, 120), (int) DpConvert.toPx(context, 160));
                        alphaAnimation2 = new AlphaAnimation(card.findViewById(R.id.frag_img), 1);
                        scaleAnimation2.setDuration(300);
                        alphaAnimation2.setDuration(300);
                        scaleAnimation2.setInterpolator(new PathInterpolator(0.4f, 0.7f, 0f, 1f));
                        alphaAnimation2.setInterpolator(new PathInterpolator(0.4f, 0.7f, 0f, 1f));
                        scaleAnimation2.start();
                        alphaAnimation2.start();
                        cardOpen = false;
                        break;
                    default:
                        break;
                }
            } else {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 计算目标大小
                    int targetWidth = screenWidth * 2 / 3;
                    int targetHeight = screenHeight * 2 / 3;
                    scaleAnimation = new ScaleAnimation(card, targetWidth, targetHeight);
                    alphaAnimation = new AlphaAnimation(card.findViewById(R.id.frag_img), 0);
                    scaleAnimation.setDuration(400);
                    alphaAnimation.setDuration(400);
                    scaleAnimation.setInterpolator(new PathInterpolator(0.4f, 0.7f, 0f, 1f));
                    alphaAnimation.setInterpolator(new PathInterpolator(0.7f, 0f, 0f, 1f));
                    if (scaleAnimation2 != null) {
                        scaleAnimation2.pause();
                        alphaAnimation2.pause();
                    }
                    scaleAnimation.start();
                    alphaAnimation.start();
                    float centerY = (screenHeight >> 1) - (targetHeight >> 1) - 80;
                    float centerX = (screenWidth >> 1) - (targetWidth >> 1) - 80;
                    sa.animateToFinalPosition(-centerY);
                    sa2.animateToFinalPosition(-centerX);
                    cardOpen = true;
                }
            }
            return true;
        });
    }
}